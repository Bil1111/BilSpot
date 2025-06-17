package com.pet.eventservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Дані для створення або оновлення події")
public record EventRequest(

        @Schema(description = "Назва події", example = "Atlas United 2025")
        @NotBlank(message = "Назва не може бути порожньою")
        @Size(min = 3, max = 100, message = "Назва має містити від 3 до 100 символів")
        String name,

        @Schema(description = "Дата проведення події", example = "2025-07-26")
        @NotNull(message = "Дата не може бути порожньою")
        @Future(message = "Дата повинна бути в майбутньому")
        LocalDate date,

        @Schema(description = "Місце проведення", example = "Blockbuster Mall, Київ")
        @NotBlank(message = "Місце проведення не може бути порожнім")
        @Size(min = 3, max = 150, message = "Назва місця має містити від 3 до 150 символів")
        String venue,

        @Schema(description = "Головний артист або гурт", example = "Okean Elzy")
        @NotBlank(message = "Ім'я артиста не може бути порожнім")
        @Size(min = 2, max = 100, message = "Ім'я артиста має містити від 2 до 100 символів")
        String artist,

        @Schema(description = "Детальний опис події", example = "Найбільший музичний фестиваль Східної Європи повертається з новим лайнапом.")
        @NotBlank(message = "Опис не може бути порожнім")
        @Size(min = 10, max = 1000, message = "Опис має містити від 10 до 1000 символів")
        String description,

        @Schema(description = "URL-адреса постера або зображення події", example = "https://example.com/images/atlas-2025.jpg")
        @NotBlank(message = "URL зображення не може бути порожнім")
        String imageURL
) {
}