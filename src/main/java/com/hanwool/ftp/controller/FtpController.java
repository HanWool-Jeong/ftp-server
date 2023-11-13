package com.hanwool.ftp.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hanwool.ftp.data.dto.DirectoryListResponseDTO;
import com.hanwool.ftp.data.dto.FileIODTO;
import com.hanwool.ftp.service.FtpService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ftp")
public class FtpController {

    private final FtpService ftpService;

    @Autowired
    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/get_list")
    public ResponseEntity<DirectoryListResponseDTO> getList(@RequestParam Long dirId) {
        DirectoryListResponseDTO ls = ftpService.getFileListOfDirectory(dirId);
        return ResponseEntity.status(HttpStatus.OK).body(ls);
    }

    @GetMapping("/download")
    public void download(@RequestParam Long fileId, HttpServletResponse response) throws IOException {
        ftpService.FileDownload(fileId, response);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileIODTO> upload(@RequestParam ArrayList<MultipartFile> files, @RequestParam Long dirId) throws IOException {
        FileIODTO status = ftpService.FileUpload(files, dirId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    
}
