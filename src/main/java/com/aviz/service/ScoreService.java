package com.aviz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.aviz.dto.ScoreResponse;
import com.aviz.model.UserScore;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {

    @Resource
    private final UserScoreService userScoreService;

    public List<ScoreResponse> getScoreList() {
        List<UserScore> highScores = userScoreService.findHighScores();

        List<ScoreResponse> returnList = new ArrayList<>();
        if (!highScores.isEmpty()) {
            long lastScore = highScores.get(0).getScore();
            long lastPosition = 1;
            for (int i = 0; i < highScores.size(); i++) {
                if (lastScore > highScores.get(i).getScore()) {
                    lastScore = highScores.get(i).getScore();
                    lastPosition = (long) i + 1;
                }
                UserScore userScore = highScores.get((int) i);
                returnList.add(ScoreResponse.builder().score(userScore.getScore()).userId(userScore.getUserId())
                        .position(lastPosition).build());
            }
        }
        return returnList;
    }

    public Optional<ScoreResponse> getUserPosition(Long userId) {
        Optional<UserScore> optUserScore = userScoreService.getScoreByUserId(userId);
        if (optUserScore.isPresent()) {
            UserScore userScore = optUserScore.get();
            Long position = userScoreService.countByScoreGreaterThan(userScore.getScore()) + 1;
            ScoreResponse response = ScoreResponse.builder().userId(userScore.getUserId()).score(userScore.getScore())
                    .position(position).build();
            return Optional.of(response);
        } else {
            return Optional.empty();
        }
    }

}