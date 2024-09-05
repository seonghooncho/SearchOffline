package com.selfProject.SearchOffline.dto;

import com.selfProject.SearchOffline.entity.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.FileWriter;

public class FileDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class Request {
        private MultipartFile file;
        private String filePath;
        private ImageFileEnum fileCategory; // 이미지 카테고리 (유저, 마켓, 제품, 리뷰)
        private Long entityID;

        public FileEntity toEntity() {
            FileEntity file = new FileEntity();
            return FileEntity.builder()
                            .filePath(filePath)
                            .fileCategory(fileCategory)
                            .entityId(entityID).build();
        }
    }

    @Getter
    public static class Response {

        private final Long id;
        private final String filePath;
        private final ImageFileEnum fileCategory;
        private final Long entityID;

        /* Entity -> DTO */
        public Response(FileEntity fileEntity) {
            this.id = fileEntity.getFileID();
            this.filePath = fileEntity.getFilePath();
            this.fileCategory = fileEntity.getFileCategory();
            this.entityID = fileEntity.getEntityId();
        }
    }
}
