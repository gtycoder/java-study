package com.gty.controller;

import com.gty.utils.FreeMarkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class FileZipDownloadController {
    @Autowired
    private FreeMarkerUtils freeMarkerUtils;
    private String zipFileName = "testZipFileName";

    @RequestMapping("/download")
    public void downloadZipFile(HttpServletResponse response) {
        try {
            freeMarkerUtils.generateJavaCodeFile2Client(response,zipFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
