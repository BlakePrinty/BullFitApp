package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BullSplitResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String tags;
    private boolean published;
    private LocalDateTime publishedAt;
    private int saveCount;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<SplitDayResponse> days;
}
