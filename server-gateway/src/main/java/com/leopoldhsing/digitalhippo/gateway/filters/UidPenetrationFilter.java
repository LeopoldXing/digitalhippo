package com.leopoldhsing.digitalhippo.gateway.filters;

import com.leopoldhsing.digitalhippo.common.constants.AuthConstants;
import com.leopoldhsing.digitalhippo.common.constants.RedisConstants;
import com.leopoldhsing.digitalhippo.gateway.config.GatewayUrlAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Order(2)
@Component
public class UidPenetrationFilter implements GlobalFilter {

    @Autowired
    private GatewayUrlAuthConfig gatewayUrlAuthConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 1. get request
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. pass trusted uri pattern
        List<String> trustedUriList = gatewayUrlAuthConfig.getTrustedUriPatterns();
        long trustedPatternCount = trustedUriList
                .parallelStream()
                .filter(pattern -> antPathMatcher.match(pattern, path))
                .count();
        if (trustedPatternCount > 0) {
            // trusted url will directly pass authentication
            return chain.filter(exchange);
        }

        // 3. pass sign-up sign-in requests
        String signInUriPattern = gatewayUrlAuthConfig.getSignInUriPatterns();
        String signUpUriPattern = gatewayUrlAuthConfig.getSignUpUriPatterns();
        if (antPathMatcher.match(signInUriPattern, path) || antPathMatcher.match(signUpUriPattern, path)) {
            // sign-in and sign-up requests will be passed directly
            return chain.filter(exchange);
        }

        return userIdPenetration(exchange, chain, token);
    }

    /**
     * pass userId to other microservices
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> userIdPenetration(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        // 1. get request and response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 2. get userId
        String key = RedisConstants.USER_PREFIX + RedisConstants.ACCESS_TOKEN_SUFFIX + token;
        String userId = redisTemplate.opsForValue().get(key);

        // 3. add userId to the request
        ServerHttpRequest newRequest = request.mutate().header(AuthConstants.USERID_HEADER_KEY, userId).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).response(response).build();

        // 4. add token into the request
        ServerHttpRequest finalRequest = newRequest.mutate().header(AuthConstants.ACCESS_TOKEN_HEADER_KEY, token).build();
        ServerWebExchange finalExchange = newExchange.mutate().request(finalRequest).response(response).build();

        return chain.filter(finalExchange);
    }
}
