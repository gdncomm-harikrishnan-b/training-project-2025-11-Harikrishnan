package com.blibi.apigateway.feign;

import com.blibi.apigateway.dto.GenericResponse;
import com.blibi.apigateway.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "member", url = "http://localhost:8083/api/member")
public interface MemberFeignClient {
    @PostMapping("/login")
    GenericResponse<?> login(LoginRequest request);
}
