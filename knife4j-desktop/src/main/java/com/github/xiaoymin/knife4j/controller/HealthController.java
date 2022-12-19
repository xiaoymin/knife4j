package com.github.xiaoymin.knife4j.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/19 22:15
 * @since:knife4j-desktop
 */
@RestController
public class HealthController {

    /**
     * 健康检查接口
     * @return
     */
    @GetMapping("/health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("SUCCESS,TIME:"+System.currentTimeMillis());
    }
}
