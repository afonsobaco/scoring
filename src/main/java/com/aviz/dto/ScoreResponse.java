package com.aviz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ScoreResponse {
    private Long userId;
    private Long score;
    private Long position;
}
