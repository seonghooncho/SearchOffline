package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.MarketDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final FileService fileService;

    @Transactional
    public MarketDTO.Response saveMarket(MarketDTO.Request marketRequest, List<FileDTO.Request> fileDTOs) throws IOException {
        //MarketEntity 생성, 저장
        MarketEntity marketEntity = marketRequest.toEntity();
        MarketEntity savedMarket = marketRepository.save(marketEntity);
        //파일저장
        List<FileEntity> fileEntities= fileService.saveFiles(fileDTOs);

        // 3. MarketEntity에 파일 정보 추가
        savedMarket.setMarketImages(fileEntities);
        marketRepository.save(savedMarket);

        return new MarketDTO.Response(savedMarket);
    }

    @Transactional(readOnly = true)
    public MarketDTO.Response findById(Long marketId) {
        Optional<MarketEntity> marketEntity = marketRepository.findById(marketId);
        return marketEntity.map(MarketDTO.Response::new).orElse(null);
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

    //사진 추가 및 삭제는 파일 핸들러로 따로 만들 방안 고려
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
        fileService.delete(fileEntity);
    }
    //사진추가
    @Transactional
    public void addMarketImage(Long marketId, FileDTO.Request requestFile) throws IOException {
        MarketEntity market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 마켓이 존재하지 않습니다."));
        FileEntity fileEntity = fileService.saveFile(requestFile);
        market.getMarketImages().add(fileEntity);
        marketRepository.save(market);
    }

}
