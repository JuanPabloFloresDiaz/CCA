package com.api.api.dto.ResponseDTO;

import java.util.UUID;

public interface LoginResponseDTO {
    UUID getId();
    String getNombres();
    String getApellidos();
    String getEmail();
    String getToken();
}
