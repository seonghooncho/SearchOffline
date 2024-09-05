package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.repository.FileRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final FileRepository fileRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    @Transactional
    public FileEntity saveFile(FileDTO.Request requestDTO) throws IOException {
        // 없는 디렉토리라면 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = uploadDir + requestDTO.getFile().getOriginalFilename();
        //파일주소 변경
        requestDTO.setFilePath(filePath);
        //파일저장
        requestDTO.getFile().transferTo(new File(filePath));
        FileEntity fileEntity = requestDTO.toEntity();
        return fileRepository.save(fileEntity);

    }

    @Transactional
    public List<FileEntity> saveFiles(List<FileDTO.Request> fileDTOs){
        return fileDTOs.stream()
                .map(dto -> {
                    try {
                        return saveFile(dto);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save file", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileDTO.Response getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .map(FileDTO.Response::new)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<FileDTO.Response> getFilesByIds(List<Long> fileIds) {
        return fileRepository.findAllById(fileIds).stream()
                .map(FileDTO.Response::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(FileEntity fileEntity){
        fileRepository.delete(fileEntity);
    }
}
