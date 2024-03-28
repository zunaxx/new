package com.example.bilingualb10.dto.optionDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class OptionResponse {
        private Long id;
        private String title;
        private String audioUrl;
        private Boolean isTrue;

    public OptionResponse(Long id, Boolean isTrue) {
        this.id = id;
        this.isTrue = isTrue;
    }

    public OptionResponse(Long id, String title, String audioUrl, Boolean isTrue) {
        this.id = id;
        this.title = title;
        this.audioUrl = audioUrl;
        this.isTrue = isTrue;
    }
}