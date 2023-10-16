package com.hanwool.ftp.service;

import java.util.List;

import com.hanwool.ftp.data.dto.FileDetailDTO;

import jakarta.servlet.http.HttpServletResponse;

public interface FtpService {
    
    public List<FileDetailDTO> getFileListOfDirectory(String path);

    public void FileDownload(Long fileId, HttpServletResponse response) throws Exception;

}
