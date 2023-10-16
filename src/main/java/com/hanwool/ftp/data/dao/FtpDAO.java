package com.hanwool.ftp.data.dao;

import com.hanwool.ftp.data.dto.FileListDTO;

public interface FtpDAO {
    
    public FileListDTO getFileListOfSystem(String path);

}
