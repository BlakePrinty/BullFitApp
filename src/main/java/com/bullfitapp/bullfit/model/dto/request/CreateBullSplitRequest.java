package com.bullfitapp.bullfit.model.dto.request;

import com.bullfitapp.bullfit.model.enums.SplitCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateBullSplitRequest {

    @NotBlank(message = "Split name is required")
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Category is required")
    private SplitCategory category;

    @Size(max = 500)
    private String tags;

    @Valid
    private List<SplitDayRequest> days = new ArrayList<>();
}
