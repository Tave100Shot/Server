package com.api.TaveShot.domain.compiler.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bog_problems")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class BojProblem {

    @Id
    @Column(name = "ID", columnDefinition = "TEXT")
    private String id;

    @Column(name = "Title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Input Description", columnDefinition = "TEXT")
    private String inputDescription;

    @Column(name = "Output Description", columnDefinition = "TEXT")
    private String outputDescription;

    @Column(name = "Sample Input", columnDefinition = "TEXT")
    private String sampleInput;

    @Column(name = "Sample Output", columnDefinition = "TEXT")
    private String sampleOutput;

}
