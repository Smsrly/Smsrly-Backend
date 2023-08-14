package com.example.smsrly.dto;

import com.example.smsrly.entity.User;
import com.example.smsrly.repository.SaveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDTOMapper implements Function<User, UserDTO> {

    private final UserRealEstateDTOMapper userRealEstateDTOMapper;
    private final SaveDTOMapper saveDTOMapper;
    private final RequestDTOMapper requestDTOMapper;
    private final SaveRepository saveRepository;

    @Override
    public UserDTO apply(User user) {
        requestDTOMapper.setSavedRealEstatesIds(saveRepository.findSavesByUserId(user.getId()));
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getLatitude(),
                user.getLongitude(),
                user.getImageURL(),
                user.getUploads().stream()
                        .map(userRealEstateDTOMapper)
                        .collect(Collectors.toList()),
                user.getSave().stream()
                        .map(saveDTOMapper)
                        .collect(Collectors.toList()),
                user.getUserRequests().stream()
                        .map(requestDTOMapper)
                        .collect(Collectors.toList())
        );
    }
}
