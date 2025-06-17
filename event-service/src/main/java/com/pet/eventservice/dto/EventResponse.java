package com.pet.eventservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Дані про подію, що повертаються клієнту")
public record EventResponse(
        @Schema(description = "Унікальний ідентифікатор події", example = "101")
        Long id,

        @Schema(description = "Назва події", example = "Atlas United 2025")
        String name,

        @Schema(description = "Дата проведення події", example = "2025-07-26")
        LocalDate date,

        @Schema(description = "Місце проведення", example = "Blockbuster Mall, Київ")
        String venue,

        @Schema(description = "Головний артист або гурт", example = "Okean Elzy")
        String artist,

        @Schema(description = "Детальний опис події", example = "Найбільший музичний фестиваль Східної Європи повертається з новим лайнапом.")
        String description,

        @Schema(description = "URL-адреса постера або зображення події", example = "https://example.com/images/atlas-2025.jpg")
        String imageURL
) {
}
