package com.hanwool.ftp.data.dto;

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
public class FileDetailDTO {

    private Long id;
    private boolean directoryFlag;
    private String name;
    private Long size;
    
}
