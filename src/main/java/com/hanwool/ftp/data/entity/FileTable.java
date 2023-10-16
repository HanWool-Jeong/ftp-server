package com.hanwool.ftp.data.entity;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class FileTable {
    
    private String name;

    private String curDir;

    private Long size;

    @ManyToOne
    @JoinColumn(name = "parent_directory")
    private Directory parentDirectory;
}
