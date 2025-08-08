package com.camilonavarrete.evaluacion.dto.responses;

import com.camilonavarrete.evaluacion.dto.PhoneDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phones")
    private List<PhoneDto> phones;
    
    @JsonProperty("created")
    private LocalDateTime created;
    
    @JsonProperty("modified")
    private LocalDateTime modified;
    
    @JsonProperty("last_login")
    private LocalDateTime lastLogin;
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("isactive")
    private Boolean isActive;
}