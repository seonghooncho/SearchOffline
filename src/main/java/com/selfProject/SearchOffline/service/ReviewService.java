package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.ProductDTO;
import com.selfProject.SearchOffline.dto.ReviewDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.ProductEntity;
import com.selfProject.SearchOffline.entity.ReviewEntity;
import com.selfProject.SearchOffline.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FileService fileService;

    @Transactional
    public ReviewDTO.Response saveReview(ReviewDTO.Request reviewRequest, List<FileDTO.Request> fileDTOs) throws IOException {
        //ReviewEntity 생성, 저장
        ReviewEntity reviewEntity = reviewRequest.toEntity();
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        //파일저장
        if(!fileDTOs.isEmpty()){
            List<FileEntity> fileEntities= fileService.saveFiles(fileDTOs);

            //ReviewEntity에 파일 정보 추가
            savedReview.setReviewImages(fileEntities);
            reviewRepository.save(savedReview);
        }
        return new ReviewDTO.Response(savedReview);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewDTO.Request request, List<FileDTO.Request> newImageRequest) {
        //영속성 저장
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        //내용 업데이트
        reviewEntity.update(request.getReviewDetail(),request.getScore());

        //이전이미지
        List<FileEntity> existingImages = reviewEntity.getReviewImages();
        //바뀐이미지
        List<FileEntity> newImages = request.getReviewImages();


        //기존 이미지 중 바뀐이미지에 없는 이미지 삭제
        existingImages.stream()
                .filter(existingImage -> !newImages.contains(existingImage))//포함 안되있으면 false
                .forEach(fileEntity -> {
                    reviewEntity.getReviewImages().remove(fileEntity);
                    fileService.delete(fileEntity);
                });

        // 새로운 이미지를 추가
        newImageRequest.stream()
                .filter(newImage -> !existingImages.contains(newImage))
                .forEach(requestFile -> {
                    reviewEntity.getReviewImages().add(requestFile.toEntity());
                    try {
                        fileService.saveFile(requestFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        // 변경 감지에 의해 변경된 부분만 저장
        reviewRepository.save(reviewEntity);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewDTO.Response> getReviewsByMarketId(Long marketId) {
        return reviewRepository.findByMarketId(marketId).stream()
                .map(ReviewDTO.Response::new)
                .toList();
    }
}
