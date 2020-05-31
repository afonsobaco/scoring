package com.aviz.controller;

import java.util.Optional;

import com.aviz.dto.HighScoreListResponse;
import com.aviz.dto.ScoreRequest;
import com.aviz.dto.ScoreResponse;
import com.aviz.service.ScoreService;
import com.aviz.service.UserScoreService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "")
@RequiredArgsConstructor
public class UserScoreController {

    private final UserScoreService service;

    private final ScoreService rankingService;

    @PostMapping(path = "/score", consumes = "application/json")
    public ResponseEntity<String> postScore(@RequestBody ScoreRequest score) {
        service.addPoints(score.getUserId(), score.getPoints());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{userId}/position", produces = "application/json")
    public ResponseEntity<ScoreResponse> getPosition(@PathVariable Long userId) {
        Optional<ScoreResponse> optPos = rankingService.getUserPosition(userId);
        if (optPos.isPresent()) {
            return new ResponseEntity<>(optPos.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/highscorelist", produces = "application/json")
    public ResponseEntity<HighScoreListResponse> getHighScoreList() {
        HighScoreListResponse response = HighScoreListResponse.builder().highscores(rankingService.getScoreList())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}