package com.bonafide.BonaFide;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {

    @RequestMapping("/")
    public String indes() {
        return "Hello World";
    }
}
