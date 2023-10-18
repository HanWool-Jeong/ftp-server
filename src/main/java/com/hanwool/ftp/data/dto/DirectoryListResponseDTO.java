package com.hanwool.ftp.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryListResponseDTO {

    private FileDetailDTO curDirectory;

    private FileDetailDTO parentDirectory;

    private List<FileDetailDTO> files;
}
