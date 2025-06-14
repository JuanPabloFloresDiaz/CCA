package com.api.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AuditoriaAccesosId implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private OffsetDateTime fecha;
}
