package com.example.bilingualb10.dto.questionDto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    private String title;
    private String audioUrl;
    private Boolean isTrue;
}