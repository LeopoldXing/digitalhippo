package com.leopoldhsing.digitalhippo.cart.interceptor

import com.leopoldhsing.digitalhippo.common.constants.AuthConstants
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

@Component
class CartFeignInterceptor : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate?) {
        val userId: Long = RequestUtil.getUid()
        requestTemplate?.header(AuthConstants.USERID_HEADER_KEY, userId.toString())
    }
}