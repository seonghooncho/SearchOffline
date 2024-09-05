package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.ReviewDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.ProductEntity;
import com.selfProject.SearchOffline.entity.ReviewEntity;
import com.selfProject.SearchOffline.repository.ProductRepository;
import com.selfProject.SearchOffline.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, FileService fileService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Optional<ProductEntity> productEntity = productRepository.findById(reviewDTO.getProductID());

        if (productEntity.isPresent()) {
            List<FileEntity> fileEntities = fileService.saveFiles(List.of(new FileDTO(reviewDTO.getReviewImageID(), null, null, null)));

            ReviewEntity reviewEntity = ReviewEntity.toSaveEntity(reviewDTO);
            reviewEntity.setProduct(productEntity.get());
            reviewEntity.setReviewImageID(fileEntities.isEmpty() ? null : fileEntities.get(0).getFileID());

            ReviewEntity savedReview = reviewRepository.save(reviewEntity);
            return ReviewDTO.toDTO(savedReview);
        }

        return null;
    }

    public ReviewDTO getReviewById(Long reviewId) {
        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(reviewId);
        return reviewEntity.map(ReviewDTO::toDTO).orElse(null);
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Optional<ReviewEntity> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent()) {
            ReviewEntity reviewEntity = optionalReview.get();
            reviewEntity.setReviewDetail(reviewDTO.getReviewDetail());

            if (reviewDTO.getReviewImageID() != null) {
                FileEntity fileEntity = fileService.saveFile(new FileDTO(reviewDTO.getReviewImageID(), null, null, null));
                reviewEntity.setReviewImageID(fileEntity.getFileID());
            }

            reviewRepository.save(reviewEntity);
            return ReviewDTO.toDTO(reviewEntity);
        }

        return null;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(ReviewDTO::toDTO)
                .toList();
    }
}
