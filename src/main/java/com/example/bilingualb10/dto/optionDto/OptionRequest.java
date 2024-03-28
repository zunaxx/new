package com.example.bilingualb10.dto.optionDto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    private String title;
    private Boolean isTrue;
    private String audioUrl;
}