package com.example.bilingualb10.dto.testDto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassTestByIdResponse {
    private Long id;
    private String fileUrl;
    private String statement;
    private String passage;
    private String questionType;
    private Integer attempts;
    private Integer duration;
    private List<PassOption> optionList;

    public PassTestByIdResponse(Long id, String file_url, String statement, String passage, Integer attempts, Integer duration) {
    }
}