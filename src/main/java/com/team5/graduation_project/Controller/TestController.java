package com.team5.graduation_project.Controller;

import com.team5.graduation_project.Models.Account;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping
    public String test() {
        return "API is working!";
    }

    @GetMapping("/me")
    public Account me(@AuthenticationPrincipal Account account) {
        return account;
    }
}
