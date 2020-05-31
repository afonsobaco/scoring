package com.aviz.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class HighScoreListResponse {
    private List<ScoreResponse> highscores;
}
