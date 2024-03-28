package com.example.bilingualb10.entity;

import com.example.bilingualb10.dto.optionDto.OptionResponse;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "options")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "option_gen")
    @SequenceGenerator(
            name = "option_gen",
            sequenceName = "option_seq",
            allocationSize = 1,
            initialValue = 19)
    private Long id;
    private String title;
    private Boolean isTrue;
    private String audioUrl;
    @ManyToOne(cascade = {
            MERGE,
            REFRESH,
            DETACH
    })
    private Question question;
    @ManyToMany(cascade = {
            MERGE,
            REFRESH,
            DETACH
    })
    private List<User> users;

    public static OptionResponse entityToResponse(Option option){
        return OptionResponse.builder()
                .id(option.id)
                .title(option.getTitle())
                .audioUrl(option.getAudioUrl())
                .isTrue(option.getIsTrue())
                .build();
    }
}