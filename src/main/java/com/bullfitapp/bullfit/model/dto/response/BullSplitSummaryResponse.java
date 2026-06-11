package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BullSplitSummaryResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String tags;
    private boolean published;
    private int saveCount;
    private int dayCount;
    private String createdBy;
    private LocalDateTime createdAt;
}
