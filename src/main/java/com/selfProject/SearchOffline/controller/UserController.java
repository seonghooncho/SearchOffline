package com.selfProject.SearchOffline.controller;

import com.selfProject.SearchOffline.dto.UserDTO;
import com.selfProject.SearchOffline.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search-offline")
public class UserController {

    private final UserService userService;
    @PostMapping("/save-user")
    public String save(@ModelAttribute UserDTO.Request request) throws IOException {
        System.out.println("userRequest = " + request);
        userService.saveUser(request);
        return "login";
    }



}
