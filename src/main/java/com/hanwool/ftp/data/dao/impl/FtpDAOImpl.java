package com.hanwool.ftp.data.dao.impl;

//import java.io.File;
//
//import org.springframework.stereotype.Repository;
//
//import com.hanwool.ftp.data.dao.FtpDAO;
//import com.hanwool.ftp.data.dto.FileListDTO;
//import com.hanwool.ftp.data.entity.FileTable;
//
//@Repository
//public class FtpDAOImpl implements FtpDAO {
//
//    public FtpDAOImpl() {
//        FileListDTO fileListDTO = getFileListOfSystem("/home/hanwool");
//        String[] list = fileListDTO.getFiles();
//
//        for (int i = 0; i < list.length; i++) {
//            FileTable fileTable = new FileTable();
//            fileTable.setDirectory("/home/hanwool");
//            fileTable.setName(list[i]);
//            
//        }
//
//    }
//
//    @Override
//    public FileListDTO getFileListOfSystem(String path) {
//        File dir = new File(path);
//        String[] files = dir.list();
//
//        FileListDTO fileListDTO = new FileListDTO(path, files);
//
//        return fileListDTO;
//    }
//}
