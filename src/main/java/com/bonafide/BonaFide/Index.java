package com.bonafide.BonaFide;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {

    @RequestMapping("/home")
    public String index() {
        return "Welcome Jenkis";
    }
}
