package com.bonafide.BonaFide;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {

    private final static String SERVICE_NAME = "Service Name";
    private final static String BUILD_VERSION = "Build Version";
    private final static String BUILD_TIME = "Build Time";
    private final static String ARTIFACT = "Artifact";

    @Autowired
    private BuildProperties buildProperties;

    @RequestMapping("/bona-fide/base/version")
    public Map<String, String> baseVersion() {
        Map<String, String> baseVersionMap = new LinkedHashMap<>();
        baseVersionMap.put(SERVICE_NAME, buildProperties.getName());
        baseVersionMap.put(BUILD_VERSION, buildProperties.getVersion());
        baseVersionMap.put(BUILD_TIME, buildProperties.getTime().toString());
        baseVersionMap.put(ARTIFACT, buildProperties.getArtifact());
        return baseVersionMap;
    }
}
