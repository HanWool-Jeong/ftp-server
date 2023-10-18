package com.hanwool.ftp.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanwool.ftp.data.entity.RegularFile;

public interface RegularFileRepository extends JpaRepository<RegularFile, Long> {
    
}
