package com.aviz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ScoreRequest {
    private Long userId;
    private Long points;
}
