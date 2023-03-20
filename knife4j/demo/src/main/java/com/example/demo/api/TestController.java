package com.example.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zc
 * @date 2023/3/9 18:32
 */
@RequestMapping("/api")
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
