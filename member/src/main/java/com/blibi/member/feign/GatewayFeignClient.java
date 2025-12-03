package com.blibi.member.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "api-gateway", url = "http://localhost:8080")
public interface GatewayFeignClient {

}
