package com.leopoldhsing.digitalhippo.feign.user;

import com.leopoldhsing.digitalhippo.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user", fallback = UserFeignClientFallback.class)
public interface UserFeignClient {

    @GetMapping("/api/inner/user")
    User getCurrentUser();

}
