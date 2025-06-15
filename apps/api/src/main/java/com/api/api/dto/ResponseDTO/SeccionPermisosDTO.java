package com.api.api.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeccionPermisosDTO {
    private String nombreSeccion;
    private String descripcionSeccion;
    private List<PermisoAccionDTO> acciones;
}
