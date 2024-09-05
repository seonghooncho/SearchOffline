package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.ProductDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.entity.ProductEntity;
import com.selfProject.SearchOffline.repository.MarketRepository;
import com.selfProject.SearchOffline.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductEntity productEntity;
    private final ProductRepository productRepository;
    private final MarketRepository marketRepository;
    private final FileService fileService;

    public ProductService(ProductEntity productEntity, ProductRepository productRepository, MarketRepository marketRepository, FileService fileService) {
        this.productEntity = productEntity;
        this.productRepository = productRepository;
        this.marketRepository = marketRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {//saveEntity

        ProductEntity productEntity1 = productEntity.toSaveEntity(productDTO);


        Optional<MarketEntity> marketEntity = marketRepository.findById(productDTO.getMarketID());

        if (marketEntity.isPresent()) {
            List<FileEntity> fileEntities = fileService.saveFiles(productDTO.getProductImageIDs().stream()
                    .map(id -> new FileDTO(id, null, null, null))
                    .toList());

            ProductEntity productEntity = ProductEntity.toSaveEntity(productDTO);
            productEntity.setMarket(marketEntity.get());
            productEntity.setProductImageIDs(fileEntities.stream()
                    .map(FileEntity::getFileID)
                    .toList());

            ProductEntity savedProduct = productRepository.save(productEntity);
            return ProductDTO.toDTO(savedProduct);
        }

        return null;
    }

    public ProductDTO getProductById(Long productId) {
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        return productEntity.map(ProductDTO::toDTO).orElse(null);
    }

    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            ProductEntity productEntity = optionalProduct.get();
            productEntity.setProductName(productDTO.getProductName());
            productEntity.setProductPrice(productDTO.getProductPrice());
            productEntity.setProductCount(productDTO.getProductCount());
            productEntity.setProductDescription(productDTO.getProductDescription());

            if (!productDTO.getProductImageIDs().isEmpty()) {
                List<FileEntity> fileEntities = fileService.saveFiles(productDTO.getProductImageIDs().stream()
                        .map(id -> new FileDTO(id, null, null, null))
                        .toList());
                productEntity.setProductImageIDs(fileEntities.stream()
                        .map(FileEntity::getFileID)
                        .toList());
            }

            productRepository.save(productEntity);
            return ProductDTO.toDTO(productEntity);
        }

        return null;
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<ProductDTO> getProductsByMarketId(Long marketId) {
        return productRepository.findByMarketId(marketId).stream()
                .map(ProductDTO::toDTO)
                .toList();
    }
    public List<ProductDTO> searchProductsByName(String keyword) {
        List<ProductEntity> products = productRepository.findByProductNameContaining(keyword);
        return products.stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
    }
}
