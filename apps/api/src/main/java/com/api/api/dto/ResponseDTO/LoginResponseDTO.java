package com.api.api.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class LoginResponseDTO {
    private UUID id;
    private String nombres;
    private String apellidos;
    private String email;
    private String token; 
}
