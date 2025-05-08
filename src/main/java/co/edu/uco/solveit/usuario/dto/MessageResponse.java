package co.edu.uco.solveit.usuario.dto;

import lombok.Builder;

@Builder
public record MessageResponse(String message, boolean success) {
}