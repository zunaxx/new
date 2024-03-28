package com.example.bilingualb10.dto.testDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TestRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;

    private TestRequest(@NotNull String title, @NotNull String description) {
        this.title = title;
        this.description = description;
    }
}