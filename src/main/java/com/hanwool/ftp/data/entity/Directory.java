package com.hanwool.ftp.data.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long size;

    @ManyToOne
    @JoinColumn(name = "parent_directory")
    private Directory parentDirectory;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_dir")
    private List<Directory> childDir;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_files")
    private List<RegularFile> childFiles;

}
