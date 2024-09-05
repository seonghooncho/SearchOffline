package com.selfProject.SearchOffline.controller;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO.Response> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("fileCategory") FileDTO.Request fileDTORequest,
                                                       @RequestParam("entityID") Long entityID) {
        try {
            // Create a request DTO
            FileDTO.Request requestDTO = FileDTO.Request.builder()
                    .file(file)
                    .filePath(file.getOriginalFilename()) // Original file name
                    .fileCategory(fileDTORequest.getFileCategory())
                    .entityID(entityID)
                    .build();

            // Save the file and get the response DTO
            FileDTO.Response responseDTO = fileService.saveFile(file, requestDTO);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
