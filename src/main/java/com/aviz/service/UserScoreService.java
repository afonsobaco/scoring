package com.aviz.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.aviz.model.UserScore;
import com.aviz.repository.UserScoreRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserScoreService {

	@Resource
	private UserScoreRepository repository;

	public Optional<UserScore> getScoreByUserId(Long id) {
		return repository.findByUserId(id);
	}

	public UserScore addPoints(Long userId, Long points) {
		Optional<UserScore> optUserScore = repository.findByUserId(userId);
		UserScore userScore = null;
		if (optUserScore.isPresent()) {
			userScore = optUserScore.get();
			userScore.setScore(userScore.getScore() + points);
		} else {
			userScore = UserScore.builder().score(points).userId(userId).build();
		}
		return repository.save(userScore);
	}

	public List<UserScore> findHighScores() {
		return repository.findTop20000ByOrderByScoreDesc();
	}

	public Long countByScoreGreaterThan(Long score) {
		return repository.countByScoreGreaterThan(score);
	}
}