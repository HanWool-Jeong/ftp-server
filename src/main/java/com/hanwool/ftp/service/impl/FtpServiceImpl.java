package com.hanwool.ftp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hanwool.ftp.data.dto.DirectoryListResponseDTO;
import com.hanwool.ftp.data.dto.FileDetailDTO;
import com.hanwool.ftp.data.dto.FileIODTO;
import com.hanwool.ftp.data.entity.Directory;
import com.hanwool.ftp.data.entity.RegularFile;
import com.hanwool.ftp.data.repository.DirectoryRepository;
import com.hanwool.ftp.data.repository.RegularFileRepository;
import com.hanwool.ftp.service.FtpService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class FtpServiceImpl implements FtpService {

    private final String baseDirectory = "/home/hanwool/공개/samba";
    private final DirectoryRepository directoryRepository;
    private final RegularFileRepository regularFileRepository;

    private void initFiles(String path) {
        Directory baseDirectoryEntity = makeDirectories(baseDirectory);
        baseDirectoryEntity.setParentDirectory(null);
        directoryRepository.save(baseDirectoryEntity);
    }

    private Directory makeDirectories(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();

        Directory directory = new Directory();
        directory.setName(path);
        directory.setSize(dir.length());
        directory.setChildDir(new ArrayList<>());
        directory.setChildFiles(new ArrayList<>());

        for (int i = 0; i < files.length; i++) {

            if (files[i].isDirectory()) {
                Directory childDirectory = makeDirectories(files[i].getAbsolutePath());

                childDirectory.setParentDirectory(directory);
                childDirectory.setName(files[i].getAbsolutePath());
                childDirectory.setSize(files[i].length());

                directory.getChildDir().add(childDirectory);
            } else {
                RegularFile regularFile = new RegularFile();

                regularFile.setCurDir(path);
                regularFile.setName(files[i].getName());
                regularFile.setSize(files[i].length());

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
    public DirectoryListResponseDTO getFileListOfDirectory(Long dirId) {
        Optional<Directory> opdirectory = directoryRepository.findById(dirId);
        Directory directory = opdirectory.get();
        DirectoryListResponseDTO directoryListResponseDTO = new DirectoryListResponseDTO();
        List<FileDetailDTO> files = new ArrayList<>();

        // 부모 디렉토리 먼저 저장
        if (directory.getParentDirectory() != null) {
            FileDetailDTO fileDetailDTO = new FileDetailDTO();
            fileDetailDTO.setId(directory.getParentDirectory().getId());
            fileDetailDTO.setDirectoryFlag(true);
            fileDetailDTO.setName(directory.getParentDirectory().getName());
            fileDetailDTO.setSize(directory.getParentDirectory().getSize());
            directoryListResponseDTO.setParentDirectory(fileDetailDTO);
        }

        // 현재 디렉토리 저장
        FileDetailDTO curDirDetailDTO = new FileDetailDTO();
        curDirDetailDTO.setId(directory.getId());
        curDirDetailDTO.setDirectoryFlag(true);
        curDirDetailDTO.setName(directory.getName());
        curDirDetailDTO.setSize(directory.getSize());
        directoryListResponseDTO.setCurDirectory(curDirDetailDTO);

        // 하위 디렉토리 나열
        List<Directory> childDir = directory.getChildDir();
        for (int i = 0; i < childDir.size(); i++) {
            FileDetailDTO fileDetailDTO = new FileDetailDTO();
            fileDetailDTO.setId(childDir.get(i).getId());
            fileDetailDTO.setDirectoryFlag(true);
            fileDetailDTO.setName(childDir.get(i).getName());
            fileDetailDTO.setSize(childDir.get(i).getSize());
            files.add(fileDetailDTO);
        }

        // 하위 파일들 나열
        List<RegularFile> childFiles = directory.getChildFiles();
        for (int i = 0; i < childFiles.size(); i++) {
            FileDetailDTO fileDetailDTO = new FileDetailDTO();
            fileDetailDTO.setId(childFiles.get(i).getId());
            fileDetailDTO.setDirectoryFlag(false);
            fileDetailDTO.setName(childFiles.get(i).getName());
            fileDetailDTO.setSize(childFiles.get(i).getSize());
            files.add(fileDetailDTO);
        }

        directoryListResponseDTO.setFiles(files);
        return directoryListResponseDTO;
    }

    @Override
    public void FileDownload(Long fileid, HttpServletResponse response) throws IOException {
        Optional<RegularFile> f = regularFileRepository.findById(fileid);
        RegularFile file;

        if (f.isPresent())
            file = f.get();
        else
            throw new IOException("다운로드할 파일이 없습니다.");

        String curDir = file.getCurDir();
        String name = file.getName();
        String path = curDir + "/" + name;

        File downloadingFile = new File(path);

        response.setContentType("application/download");
        response.setContentLength((int)downloadingFile.length());

        // utf-8로 파일이름 인코딩 안하면 한글 파일 다운시 Dispositon 헤더와 invalid하다는 오류 발생함.
        String encodedName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content=Disposition", "attachment;filename=" + encodedName);

        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(downloadingFile);
        FileCopyUtils.copy(fileInputStream, outputStream);

        outputStream.close();
        fileInputStream.close();
    }

    @Override
    public FileIODTO FileUpload(ArrayList<MultipartFile> files, Long dirId) throws IOException {
        Optional<Directory> d = directoryRepository.findById(dirId);
        Directory directory;

        if (d.isPresent())
            directory = d.get();
        else
            throw new IOException("해당 디렉토리가 없습니다");

        String dir = directory.getName();

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            File newFile = new File(dir + "/" + originalFileName);
            file.transferTo(newFile);

            RegularFile newRegularFile = new RegularFile();
            newRegularFile.setCurDir(dir);
            newRegularFile.setName(originalFileName);
            newRegularFile.setSize(newFile.length());
            
            directory.getChildFiles().add(newRegularFile);
            newRegularFile = regularFileRepository.save(newRegularFile);
        }

        return new FileIODTO(true);
    }
    
}
