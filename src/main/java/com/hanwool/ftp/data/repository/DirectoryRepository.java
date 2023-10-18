package com.hanwool.ftp.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanwool.ftp.data.entity.Directory;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    public Directory findByName(String name);
    
}
