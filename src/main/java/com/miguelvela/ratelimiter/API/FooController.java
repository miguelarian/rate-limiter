package com.miguelvela.ratelimiter.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FooController {

    @GetMapping("/foo")
    public ResponseEntity<String> foo() {

        return ResponseEntity
                .ok()
                .body("bar");
    }
}
