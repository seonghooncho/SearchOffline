package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.MarketDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.repository.FileRepository;
import com.selfProject.SearchOffline.repository.MarketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;

    public MarketService(MarketRepository marketRepository, FileService fileService, FileRepository fileRepository) {
        this.marketRepository = marketRepository;
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public MarketDTO createMarket(MarketDTO.Request requestMarket, List<FileDTO.Request> requestImages) {
        List<FileEntity> fileEntities = fileService.saveFiles(List.of(new FileDTO(marketDTO.getMarketImageID(), null, null, null)));

        MarketEntity marketEntity = MarketEntity.toSaveEntity(marketDTO);
        marketEntity.setMarketImageID(fileEntities.isEmpty() ? null : fileEntities.get(0).getFileID());

        MarketEntity savedMarket = marketRepository.save(marketEntity);
        return MarketDTO.toDTO(savedMarket);
    }

    public MarketDTO getMarketById(Long marketId) {
        Optional<MarketEntity> marketEntity = marketRepository.findById(marketId);
        return marketEntity.map(MarketDTO::toDTO).orElse(null);
    }

    @Transactional
    public MarketDTO updateMarket(Long marketId, MarketDTO marketDTO) {
        Optional<MarketEntity> optionalMarket = marketRepository.findById(marketId);

        if (optionalMarket.isPresent()) {
            MarketEntity marketEntity = optionalMarket.get();
            marketEntity.setMarketName(marketDTO.getMarketName());
            marketEntity.setBusinessHour(marketDTO.getBusinessHour());
            marketEntity.setPhoneNumber(marketDTO.getPhoneNumber());
            marketEntity.setLocation(marketDTO.getLocation());

            if (marketDTO.getMarketImageID() != null) {
                FileEntity fileEntity = fileService.saveFile(new FileDTO(marketDTO.getMarketImageID(), null, null, null));
                marketEntity.setMarketImageID(fileEntity.getFileID());
            }

            marketEntity.setProductIDList(marketDTO.getProductIDList());
            marketEntity.setReviewIDList(marketDTO.getReviewIDList());

            marketRepository.save(marketEntity);
            return MarketDTO.toDTO(marketEntity);
        }

        return null;
    }

    @Transactional
    public void deleteMarket(Long marketId) {
        marketRepository.deleteById(marketId);
    }
    //가게정보 변경
    @Transactional
    public void updateMarketInfo(MarketDTO.Request request) {
        // marketID로 기존 가게 정보 조회
        MarketEntity marketEntity = marketRepository.findById(request.getMarketID())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        // 요청으로 받은 정보들로 업데이트 진행
        marketEntity.update(request.getMarketName(), request.getBusinessHour(), request.getPhoneNumber(), request.getLocation());

        // 수정된 엔티티 저장
        marketRepository.save(marketEntity);
    }
    //사진 삭제
    @Transactional
    public void removeMarketImage(Long marketId, Long imageId) {
        // 마켓을 영속성 컨텍스트에 넣음
        MarketEntity market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        // 삭제할 이미지를 찾음
        FileEntity fileEntity = market.getMarketImages().stream()
                .filter(image -> image.getFileID().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));

        // 리스트에서 삭제
        market.getMarketImages().remove(fileEntity);

        // 이미지 엔티티 삭제
        fileRepository.delete(fileEntity);
    }
    //사진추가
    @Transactional
    public void addMarketImage(Long marketId, FileEntity newFile) {
        MarketEntity market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 마켓이 존재하지 않습니다."));

        // 새로운 파일 엔티티 설정
        newFile.setMarket(market);  // 파일 엔티티에 마켓 정보를 설정해줘야 함

        // 이미지 리스트에 추가
        market.getMarketImages().add(newFile);

        // 새로운 파일 엔티티 저장
        FileRepository.save(newFile);
    }


    public List<MarketDTO> getAllMarkets() {
        return marketRepository.findAll().stream()
                .map(MarketDTO::toDTO)
                .toList();
    }
}
