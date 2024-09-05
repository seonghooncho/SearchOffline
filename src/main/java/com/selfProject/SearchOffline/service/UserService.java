package com.selfProject.SearchOffline.service;

import com.selfProject.SearchOffline.dto.UserDTO;
import com.selfProject.SearchOffline.entity.UserEntity;
import com.selfProject.SearchOffline.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getUserImageID() != null) {
            // 파일 처리 로직을 구현할 경우 여기에서 파일 정보를 설정합니다.
        }

        UserEntity userEntity = UserEntity.toSaveEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);

        return UserDTO.toDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setUserEmail(userDTO.getUserEmail());
            userEntity.setUserPassword(userDTO.getUserPassword());
            userEntity.setUserName(userDTO.getUserName());

            if (userDTO.getUserImageID() != null) {
                userEntity.setUserImageID(userDTO.getUserImageID());
            }

            userEntity.setUserMarketIDList(userDTO.getUserMarketIDList());

            userRepository.save(userEntity);
            return UserDTO.toDTO(userEntity);
        }

        return null;
    }

    public UserDTO getUserById(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return userEntity.map(UserDTO::toDTO).orElse(null);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
