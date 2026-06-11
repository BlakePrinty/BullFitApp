package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitDayResponse {
    private Long id;
    private int dayNumber;
    private String name;
    private List<SplitDayExerciseResponse> exercises;
}
