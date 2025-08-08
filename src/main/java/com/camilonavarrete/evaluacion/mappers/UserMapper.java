package com.camilonavarrete.evaluacion.mappers;

import org.springframework.stereotype.Component;

import com.camilonavarrete.evaluacion.dto.PhoneDto;
import com.camilonavarrete.evaluacion.dto.requests.UserRequestDto;
import com.camilonavarrete.evaluacion.dto.responses.UserResponseDto;
import com.camilonavarrete.evaluacion.entities.Phone;
import com.camilonavarrete.evaluacion.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setIsActive(true);
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());

        if (userRequestDto.getPhones() != null) {
            List<Phone> phones = userRequestDto.getPhones().stream()
                    .map(phoneDto -> {
                        Phone phone = new Phone();
                        phone.setNumber(phoneDto.getNumber());
                        phone.setCitycode(phoneDto.getCitycode());
                        phone.setContrycode(phoneDto.getContrycode());
                        phone.setUser(user);
                        return phone;
                    })
                    .collect(Collectors.toList());
            user.setPhones(phones);
        }

        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setCreated(user.getCreated());
        responseDto.setModified(user.getModified());
        responseDto.setLastLogin(user.getLastLogin());
        responseDto.setToken(user.getToken());
        responseDto.setIsActive(user.getIsActive());

        if (user.getPhones() != null) {
            List<PhoneDto> phoneDtos = user.getPhones().stream()
                    .map(phone -> new PhoneDto(
                            phone.getNumber(),
                            phone.getCitycode(),
                            phone.getContrycode()
                    ))
                    .collect(Collectors.toList());
            responseDto.setPhones(phoneDtos);
        }

        return responseDto;
    }
}