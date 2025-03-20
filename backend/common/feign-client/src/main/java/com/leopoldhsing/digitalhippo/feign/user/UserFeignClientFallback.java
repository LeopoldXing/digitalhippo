package com.leopoldhsing.digitalhippo.feign.user;

import com.leopoldhsing.digitalhippo.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFallback implements UserFeignClient {
    @Override
    public User getCurrentUser() {
        return null;
    }
}
