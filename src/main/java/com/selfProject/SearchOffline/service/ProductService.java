package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.MarketDTO;
import com.selfProject.SearchOffline.dto.ProductDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.entity.ProductEntity;
import com.selfProject.SearchOffline.repository.MarketRepository;
import com.selfProject.SearchOffline.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    @Transactional
    public ProductDTO.Response saveProduct(ProductDTO.Request productRequest, List<FileDTO.Request> fileDTOs) throws IOException {
        //ProductEntity 생성, 저장
        ProductEntity productEntity = productRequest.toEntity();
        ProductEntity savedProduct = productRepository.save(productEntity);
        //파일저장
        List<FileEntity> fileEntities= fileService.saveFiles(fileDTOs);

        // 3. ProductEntity에 파일 정보 추가
        savedProduct.setProductImages(fileEntities);
        productRepository.save(savedProduct);

        return new ProductDTO.Response(savedProduct);
    }
    @Transactional(readOnly = true)
    public ProductDTO.Response getProductById(Long productId) {
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        return productEntity.map(ProductDTO.Response::new).orElse(null);
    }

    @Transactional
    public void updateProductInfo(ProductDTO.Request request) {
        ProductEntity productEntity = productRepository.findById(request.getProductID())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        // 요청으로 받은 정보들로 업데이트 진행
        productEntity.update(request.getProductName(), request.getProductPrice(), request.getProductCount(), request.getProductDescription());

        // 수정된 엔티티 저장
        productRepository.save(productEntity);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    //사진 삭제
    @Transactional
    public void removeProductImage(Long productId, Long imageId) {
        // 마켓을 영속성 컨텍스트에 넣음
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        // 삭제할 이미지를 찾음
        FileEntity fileEntity = product.getProductImages().stream()
                .filter(image -> image.getFileID().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));

        // 리스트에서 삭제
        product.getProductImages().remove(fileEntity);

        // 이미지 엔티티 삭제
        fileService.delete(fileEntity);
    }
    //사진추가
    @Transactional
    public void addProductImage(Long productId, FileDTO.Request requestFile) throws IOException {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 마켓이 존재하지 않습니다."));
        FileEntity fileEntity = fileService.saveFile(requestFile);
        product.getProductImages().add(fileEntity);
        productRepository.save(product);
    }
    //마켓에서 전체제품조회
    @Transactional(readOnly = true)
    public List<ProductDTO.Response> getProductsByMarketId(Long marketId) {
        return productRepository.findByMarketId(marketId).stream()
                .map(ProductDTO.Response::new)
                .toList();
    }
    //검색기능(키워드로 검색)  추후에 거리기준 정렬
    public List<ProductDTO.Response> searchProductsByName(String keyword) {
        List<ProductEntity> products = productRepository.findByProductNameContaining(keyword);
        return products.stream()
                .map(ProductDTO.Response::new)
                .collect(Collectors.toList());
    }
}
