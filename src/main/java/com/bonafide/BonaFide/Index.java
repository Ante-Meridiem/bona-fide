package com.bonafide.BonaFide;

import static org.springframework.http.HttpMethod.GET;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Index {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/home")
    public String index() {
        return "Welcome Jenkins";
    }

    @RequestMapping("/communication")
    public String home() throws JSONException {
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("Home","Nadakkane");
        jsonpObject.put("Appurathe-ninne varunnathe",
                restTemplate.exchange("http://localhost:9009/user-list",
                        GET,null, List.class).getBody());
        return jsonpObject.toString();
    }
}
