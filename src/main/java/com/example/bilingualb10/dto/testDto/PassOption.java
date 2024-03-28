package com.example.bilingualb10.dto.testDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PassOption {
    private Long id;
    private String title;
    private String audio_url;
    private Boolean isTrue;
}