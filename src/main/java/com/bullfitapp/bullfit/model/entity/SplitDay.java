package com.bullfitapp.bullfit.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "split_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SplitDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bull_split_id", nullable = false)
    private BullSplit bullSplit;

    @Builder.Default
    @OneToMany(mappedBy = "splitDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<SplitDayExercise> exercises = new ArrayList<>();
}
