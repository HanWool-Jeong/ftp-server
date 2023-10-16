package com.hanwool.ftp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.hanwool.ftp.data.dto.FileDetailDTO;
import com.hanwool.ftp.data.entity.FileTable;
import com.hanwool.ftp.data.repository.FileTableReopository;
import com.hanwool.ftp.service.FtpService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class FtpServiceImpl implements FtpService {

    private final String baseDirectory = "/home/hanwool";
    private final FileTableReopository fileTableReopository;

    private void initFiles(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            FileTable fileTable = new FileTable();
            fileTable.setDirectory(path);
            fileTable.setName(files[i].getName());

            fileTableReopository.save(fileTable);
        }
    }

    @Autowired
    public FtpServiceImpl(FileTableReopository fileTableReopository) {
        this.fileTableReopository = fileTableReopository;
        initFiles(baseDirectory);
    }

    @Override
    public List<FileDetailDTO> getFileListOfDirectory(String path) {
        List<FileTable> fileTables = fileTableReopository.findByDirectory(path);
        List<FileDetailDTO> files = new ArrayList<>();

        for (int i = 0; i < fileTables.size(); i++) {
            Long id = fileTables.get(i).getId();
            String name = fileTables.get(i).getName();
            String directory = fileTables.get(i).getDirectory();

            FileDetailDTO fileDetailDTO = new FileDetailDTO(id, name, directory);
            files.add(fileDetailDTO);
        }

        return files;
    }

    @Override
    public void FileDownload(Long fileid, HttpServletResponse response) throws Exception {
        Optional<FileTable> file = fileTableReopository.findById(fileid);

        String directory = file.get().getDirectory();
        String name = file.get().getName();
        String path = directory + "/" + name;

        File downloadingFile = new File(path);

        response.setContentType("application/download");
        response.setContentLength((int)downloadingFile.length());
        response.setHeader("Content=Disposition", "attachment;filename=" + name);

        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(downloadingFile);
        FileCopyUtils.copy(fileInputStream, outputStream);

        outputStream.close();
        fileInputStream.close();
    }
    
}
