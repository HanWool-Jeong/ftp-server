package com.hanwool.ftp.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.hanwool.ftp.data.dto.DirectoryListResponseDTO;
import com.hanwool.ftp.data.dto.FileIODTO;

import jakarta.servlet.http.HttpServletResponse;

public interface FtpService {
    
    public DirectoryListResponseDTO getFileListOfDirectory(Long dirId);

    public void FileDownload(Long fileId, HttpServletResponse response) throws IOException;

    public FileIODTO FileUpload(ArrayList<MultipartFile> files, Long dirId) throws IOException;

}
