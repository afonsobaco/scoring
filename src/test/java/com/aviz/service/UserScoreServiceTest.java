package com.aviz.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.aviz.model.UserScore;
import com.aviz.repository.UserScoreRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserScoreServiceTest {

    @Mock
    private UserScoreRepository repository;

    @InjectMocks
    private UserScoreService service;

    @Test
    public void testShouldGetScoreFromRepository() {
        UserScore expected = UserScore.builder().userId(12L).score(5000L).build();
        when(repository.findByUserId(anyLong())).thenReturn(Optional.of(expected));
        Optional<UserScore> optionalResult = service.getScoreByUserId(12L);
        UserScore actual = optionalResult.get();
        assertNotNull("result shouldn't have been null", actual);
        assertEquals("user id should have been " + expected.getUserId(), expected.getUserId(), actual.getUserId());
        assertEquals("current score should have been " + expected.getScore(), expected.getScore(), actual.getScore());
    }

    @Test
    public void testShouldCreateAndAddUserScore() {
        Long userId = 12345L;
        Long points = 200L;
        when(repository.save(any())).then(i -> i.getArgument(0, UserScore.class));
        UserScore actual = service.addPoints(userId, points);
        assertNotNull("result shouldn't have been null", actual);
        assertEquals("user id should have been " + userId, userId, actual.getUserId());
        assertEquals("current score should have been " + points, points, actual.getScore());
    }

    @Test
    public void testShouldUpdateCurrentScoreFromPreviousCreatedUser() {
        Long userId = 12345L;
        Long points = 200L;
        Long score = 5400L;
        UserScore saved = UserScore.builder().score(score).userId(userId).build();
        when(repository.findByUserId(userId)).thenReturn(Optional.of(saved));
        when(repository.save(any())).then(i -> i.getArgument(0, UserScore.class));

        UserScore actual = service.addPoints(userId, points);
        assertNotNull("result shouldn't have been null", actual);
        assertEquals("user id should have been " + userId, userId, actual.getUserId());
        Long finalScore = score + points;
        assertEquals("current score should have been " + finalScore, finalScore, actual.getScore());
    }

}