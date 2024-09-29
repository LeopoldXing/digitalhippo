package com.leopoldhsing.digitalhippo.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leopoldhsing.digitalhippo.common.constants.RedisConstants;
import com.leopoldhsing.digitalhippo.gateway.config.GatewayUrlAuthConfig;
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Order(1)
@Component
public class GlobalAuthFilter implements GlobalFilter {

    @Autowired
    private GatewayUrlAuthConfig gatewayUrlAuthConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 1. get request
        ServerHttpRequest request = exchange.getRequest();

        // 2. authentication
        // 2.1 get path
        String path = request.getURI().getPath();

        // 2.2 trusted uri pattern
        List<String> trustedUriList = gatewayUrlAuthConfig.getTrustedUriPatterns();
        long trustedPatternCount = trustedUriList
                .parallelStream()
                .filter(pattern -> antPathMatcher.match(pattern, path))
                .count();
        if (trustedPatternCount > 0) {
            // trusted url will directly pass authentication
            return chain.filter(exchange);
        }

        // 2.3 inner call request
        List<String> innerUriList = gatewayUrlAuthConfig.getInnerUriPatterns();
        long innerRequestCount = innerUriList
                .parallelStream()
                .filter(pattern -> antPathMatcher.match(pattern, path))
                .count();
        if (innerRequestCount > 0) {
            // inner request called externally will be directly blocked
            return prepareErrorResponse(exchange, request, "Request Denied, you are trying to access restricted resources!");
        }

        // 2.4 handle sign-up | sign-in | sign out requests
        String signInUriPattern = gatewayUrlAuthConfig.getSignInUriPatterns();
        String signUpUriPattern = gatewayUrlAuthConfig.getSignUpUriPatterns();
        String signOutUriPattern = gatewayUrlAuthConfig.getSignOutUriPatterns();
        if (antPathMatcher.match(signInUriPattern, path) || antPathMatcher.match(signUpUriPattern, path) || antPathMatcher.match(signOutUriPattern, path)) {
            // sign-in and sign-up and sign-out requests will be passed directly
            return chain.filter(exchange);
        }

        // 2.5 requests that needed to be accessed after signing in
        // justify if currently in login state
        String token = this.getTokenFromRequest(exchange);
        if (!isTokenValid(token)) {
            return prepareErrorResponse(exchange, request, "Invalid Token, please sign in.");
        }

        return chain.filter(exchange);
    }

    /**
     * prepare error response
     *
     * @param exchange
     * @param request
     * @param errorMessage
     * @return
     */
    private Mono<Void> prepareErrorResponse(ServerWebExchange exchange, ServerHttpRequest request, String errorMessage) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                request.getPath().toString(),
                HttpStatus.UNAUTHORIZED,
                errorMessage,
                LocalDateTime.now()
        );
        // prepare response data
        byte[] errorResponseBytes;
        try {
            objectMapper.registerModule(new JavaTimeModule());
            errorResponseBytes = objectMapper.writeValueAsString(errorResponseDto).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // get response
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // add proper response header
        DataBuffer wrap = response.bufferFactory().wrap(errorResponseBytes);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        // reject request | send response
        return response.writeWith(Mono.just(wrap));
    }

    /**
     * get token from request header
     *
     * @param exchange
     * @return
     */
    private String getTokenFromRequest(ServerWebExchange exchange) {
        // 1. get request
        ServerHttpRequest request = exchange.getRequest();

        // 2. get Authorization header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 3. get token
        String token = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 4. return token;
        return token;
    }

    /**
     * determine if the token exists in redis server
     *
     * @param token
     * @return
     */
    private Boolean isTokenValid(String token) {
        if (!StringUtils.hasLength(token)) return false;
        String key = RedisConstants.USER_PREFIX + RedisConstants.ACCESS_TOKEN_SUFFIX + token;
        return redisTemplate.hasKey(key);
    }
}
