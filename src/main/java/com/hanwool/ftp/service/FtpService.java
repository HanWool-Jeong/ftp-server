package com.hanwool.ftp.service;

import com.hanwool.ftp.data.dto.DirectoryListResponseDTO;

import jakarta.servlet.http.HttpServletResponse;

public interface FtpService {
    
    public DirectoryListResponseDTO getFileListOfDirectory(Long dirId);

    public void FileDownload(Long fileId, HttpServletResponse response) throws Exception;

}
