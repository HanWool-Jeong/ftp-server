package com.hanwool.ftp.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanwool.ftp.data.entity.FileTable;

public interface FileTableReopository extends JpaRepository<FileTable, Long> {

    List<FileTable> findByDirectory(String directory);

}
