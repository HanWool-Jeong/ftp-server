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
import com.hanwool.ftp.data.entity.Directory;
import com.hanwool.ftp.data.entity.RegularFile;
import com.hanwool.ftp.data.repository.DirectoryRepository;
import com.hanwool.ftp.data.repository.RegularFileRepository;
import com.hanwool.ftp.service.FtpService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class FtpServiceImpl implements FtpService {

    private final String baseDirectory = "/home/hanwool";
    private final DirectoryRepository directoryRepository;
    private final RegularFileRepository regularFileRepository;

    private void initFiles(String path) {
        Directory baseDirectoryEntity = makeDirectories(baseDirectory);
        baseDirectoryEntity.setParentDirectory(null);
    }

    private Directory makeDirectories(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();

        Directory directory = new Directory();
        directory.setCurDir(path);
        directory.setName(path);
        directory.setSize(dir.length());
        directory.setChildDir(new ArrayList<>());
        directory.setChildFiles(new ArrayList<>());
        directoryRepository.save(directory);

        for (int i = 0; i < files.length; i++) {

            if (files[i].isDirectory()) {
                Directory childDirectory = makeDirectories(files[i].getAbsolutePath());

                childDirectory.setParentDirectory(directory);
                childDirectory.setCurDir(path);
                childDirectory.setName(files[i].getAbsolutePath());
                childDirectory.setSize(files[i].length());

                directory.getChildDir().add(childDirectory);
            } else {
                RegularFile regularFile = new RegularFile();

                regularFile.setParentDirectory(directory);
                regularFile.setCurDir(path);
                regularFile.setName(files[i].getName());
                regularFile.setSize(files[i].length());

                regularFileRepository.save(regularFile);

                directory.getChildFiles().add(regularFile);
            }
        }

        return directory;
    }

    @Autowired
    public FtpServiceImpl(DirectoryRepository directoryRepository, RegularFileRepository regularFileRepository) {
        this.directoryRepository = directoryRepository;
        this.regularFileRepository = regularFileRepository;
        initFiles(baseDirectory);
    }

    @Override
    public List<FileDetailDTO> getFileListOfDirectory(String path) {
        Directory directory = directoryRepository.findByName(path);
        List<FileDetailDTO> files = new ArrayList<>();

        List<Directory> childDir = directory.getChildDir();
        for (int i = 0; i < childDir.size(); i++) {
            Long id = childDir.get(i).getId();
            String name = childDir.get(i).getName();
            String curDir = childDir.get(i).getCurDir();
            Long size = childDir.get(i).getSize();

            FileDetailDTO fileDetailDTO = new FileDetailDTO(id, name, curDir, size);
            files.add(fileDetailDTO);
        }

        List<RegularFile> childFiles = directory.getChildFiles();
        for (int i = 0; i < childFiles.size(); i++) {
            Long id = childFiles.get(i).getId();
            String name = childFiles.get(i).getName();
            String curDir = childFiles.get(i).getCurDir();
            Long size = childFiles.get(i).getSize();

            FileDetailDTO fileDetailDTO = new FileDetailDTO(id, name, curDir, size);
            files.add(fileDetailDTO);
        }

        return files;
    }

    @Override
    public void FileDownload(Long fileid, HttpServletResponse response) throws Exception {
        Optional<RegularFile> file = regularFileRepository.findById(fileid);

        String curDir = file.get().getCurDir();
        String name = file.get().getName();
        String path = curDir + "/" + name;

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
