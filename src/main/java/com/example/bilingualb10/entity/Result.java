package com.example.bilingualb10.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "results")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "result_gen")
    @SequenceGenerator(
            name = "result_gen",
            sequenceName = "result_seq",
            allocationSize = 1,
            initialValue = 10)
    private Long id;
    private ZonedDateTime dateOfSubmission;
    private Boolean checked;
    private double score;
    @OneToOne(cascade = {
            PERSIST,
            MERGE,
            DETACH,
            REFRESH
    })
    private Answer answer;
}