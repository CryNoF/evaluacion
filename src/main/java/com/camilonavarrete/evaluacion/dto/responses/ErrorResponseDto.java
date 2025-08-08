package com.camilonavarrete.evaluacion.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    
    @JsonProperty("message")
    private String message;
    
    public static ErrorResponseDto of(String message) {
        return new ErrorResponseDto(message);
    }
}