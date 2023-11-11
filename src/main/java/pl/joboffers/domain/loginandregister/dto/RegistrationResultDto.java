package pl.joboffers.domain.loginandregister.dto;

import lombok.Builder;

public record RegistrationResultDto(String id, boolean isCreated, String username) {
}
