package com.leopoldhsing.digitalhippo.common.utils;

import com.leopoldhsing.digitalhippo.common.utils.constants.GatewayConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {
    public static HttpServletRequest getRequestFromContext() {
        HttpServletRequest request = null;

        // get request from RequestContextHolder
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            request = attributes.getRequest();
        }
        return request;
    }

    public static long getUid() {
        long uid = 0L;
        HttpServletRequest request = RequestUtil.getRequestFromContext();
        if (!ObjectUtils.isEmpty(request)) {
            String header = request.getHeader(GatewayConstants.USERID_HEADER_KEY);
            if (StringUtils.hasLength(header)) {
                uid = Long.parseLong(header);
            }
        }

        return uid;
    }

    private RequestUtil() {
    }
}
