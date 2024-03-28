package com.example.bilingualb10.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "tests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_gen")
    @SequenceGenerator(
            name = "test_gen",
            sequenceName = "test_seq",
            allocationSize = 1,
            initialValue = 4)
    private Long id;
    private String title;
    private String description;
    private Boolean enable;
    private Integer duration;
    @OneToMany(mappedBy = "test", cascade = {
            MERGE,
            DETACH,
            REFRESH,
            REMOVE
    })
    private List<Question> questions;
    @ManyToMany(mappedBy = "tests", cascade = {
            MERGE,
            DETACH,
            REFRESH
    })
    private List<User> users;
}