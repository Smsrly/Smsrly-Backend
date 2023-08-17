package com.example.smsrly.dto;

import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.SaveRepository;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    private final UserRealEstateDTOMapper userRealEstateDTOMapper;
    private final SaveDTOMapper saveDTOMapper;
    private final RequestDTOMapper requestDTOMapper;
    private final SaveRepository saveRepository;
    private final RealEstateRepository realEstateRepository;
    private final RequestRepository requestRepository;
    @Setter
    private Pageable pageable;

    @Override
    public UserDTO apply(User user) {
        requestDTOMapper.setSavedRealEstatesIds(saveRepository.findSavesByUserId(user.getId()));
        userRealEstateDTOMapper.setPageable(pageable);
        return new UserDTO(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getLatitude(),
                user.getLongitude(),
                user.getImageURL(),
                realEstateRepository.findUserUploads(user.getId(), pageable).stream()
                        .map(userRealEstateDTOMapper)
                        .toList(),
                saveRepository.findSavesByUserId(user.getId(), pageable).stream()
                        .map(saveDTOMapper)
                        .toList(),
                requestRepository.findRequestsByUserId(user.getId(), pageable).stream()
                        .map(requestDTOMapper)
                        .toList()
        );
    }

    public UserDTOMapper(UserRealEstateDTOMapper userRealEstateDTOMapper, SaveDTOMapper saveDTOMapper, RequestDTOMapper requestDTOMapper, SaveRepository saveRepository, RealEstateRepository realEstateRepository, RequestRepository requestRepository) {
        this.userRealEstateDTOMapper = userRealEstateDTOMapper;
        this.saveDTOMapper = saveDTOMapper;
        this.requestDTOMapper = requestDTOMapper;
        this.saveRepository = saveRepository;
        this.realEstateRepository = realEstateRepository;
        this.requestRepository = requestRepository;
    }
}
