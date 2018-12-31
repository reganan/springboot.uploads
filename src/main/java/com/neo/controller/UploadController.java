package com.neo.controller;

import com.neo.config.UploadConfig;
import com.neo.tools.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class UploadController {

    @Autowired
    UploadConfig uploadConfig;

    @GetMapping("/")
    public String index() {
        return "upload_page";
    }


    @PostMapping("/uploads") // //new annotation since 4.3
    public String uploadsByAjax(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        String userName = request.getParameter("userName");
        String folderName = request.getParameter("folderName");
        folderName = userName + "/" + folderName + "/";
        System.out.println("uploads");

        Upload.uploadFiles(files, folderName);
        return "upload_page";
    }


    @ResponseBody
    @PostMapping("/{savePath}/upload") // //new annotation since 4.3
    public String uploadByAjax(@RequestParam("file") MultipartFile file, @PathVariable String savePath) {

        if (file.isEmpty()) {
            return "error,the file is empty!";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadConfig.getUploadPath() + savePath +"//" + file.getOriginalFilename());
            System.out.println(path);
            Files.write(path, bytes);

            return "You successfully uploaded '" + file.getOriginalFilename() + "'";

        } catch (IOException e) {
            return "upload error!";
        }
    }


    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }



}

