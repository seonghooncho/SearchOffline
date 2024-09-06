package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.FileDTO;
import com.selfProject.SearchOffline.dto.UserDTO;
import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.ProductEntity;
import com.selfProject.SearchOffline.entity.UserEntity;
import com.selfProject.SearchOffline.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;

    public UserService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Transactional
    public UserEntity saveUser(UserDTO.Request requestUser) {
        return userRepository.save(requestUser.toEntity());
    }

    @Transactional
    public UserEntity updateUser(Long userId, UserDTO.Request requestUser) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.update(requestUser.getUserPassword(),requestUser.getUserName());

            return userRepository.save(userEntity);
        }
        return null;
    }
    @Transactional(readOnly = true)
    public UserDTO.Response getUserById(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return userEntity.map(UserDTO.Response::new).orElse(null);
    }
    @Transactional
    public void changeUserImage(Long userId, FileDTO.Request requestFile) throws IOException {

        UserEntity user = userRepository.findById(userId)
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
