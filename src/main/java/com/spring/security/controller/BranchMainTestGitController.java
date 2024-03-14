package com.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BranchMainTestGitController   {
    
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    
    

    
}


