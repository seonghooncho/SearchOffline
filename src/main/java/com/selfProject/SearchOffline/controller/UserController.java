package com.selfProject.SearchOffline.controller;

import com.selfProject.SearchOffline.domain.member.dto.MemberDTO;
import com.selfProject.SearchOffline.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search-offline")
public class UserController {

    private final MemberService userService;
    @PostMapping("/save-user")
    public String save(@ModelAttribute MemberDTO.Request request) throws IOException {
        System.out.println("userRequest = " + request);
        userService.saveUser(request);
        return "login";
    }



}
