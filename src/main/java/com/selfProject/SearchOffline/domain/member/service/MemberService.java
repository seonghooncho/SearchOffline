package com.selfProject.SearchOffline.domain.member.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.domain.member.dto.MemberDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.domain.member.entity.MemberEntity;
import com.selfProject.SearchOffline.domain.member.repository.MemberRepository;
import com.selfProject.SearchOffline.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository userRepository;
    private final FileService fileService;

    public MemberService(MemberRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Transactional
    public MemberEntity saveUser(MemberDTO.Request requestUser) {
        return userRepository.save(requestUser.toEntity());
    }

    @Transactional
    public MemberEntity updateUser(Long userId, MemberDTO.Request requestUser) {
        Optional<MemberEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            MemberEntity userEntity = optionalUser.get();
            userEntity.update(requestUser.getUserPassword(),requestUser.getUserName());

            return userRepository.save(userEntity);
        }
        return null;
    }
    @Transactional(readOnly = true)
    public MemberDTO.Response getUserById(Long userId) {
        Optional<MemberEntity> userEntity = userRepository.findById(userId);
        return userEntity.map(MemberDTO.Response::new).orElse(null);
    }
    @Transactional
    public void changeUserImage(Long userId, FileDTO.Request requestFile) throws IOException {

        MemberEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        FileEntity fileEntity = fileService.saveFile(requestFile);
        user.setUserImage(fileEntity);
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
