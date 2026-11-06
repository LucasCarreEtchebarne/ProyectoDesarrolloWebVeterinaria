package com.metropetclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    //http://localhost/
    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}