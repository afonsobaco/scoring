package com.aviz.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aviz.dto.ScoreResponse;
import com.aviz.model.UserScore;
import com.aviz.util.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScoreServiceTest {

    @Mock
    private UserScoreService userScoreService;

    @InjectMocks
    private ScoreService service;

    @Test
    public void testShouldGetOrderedScoreResponse() {
        List<UserScore> highScores = generateRandomUserScore(20);
        when(userScoreService.findHighScores()).thenReturn(highScores);
        List<ScoreResponse> list = service.getScoreList();
        assertTrue("list size should have been " + highScores.size(), list.size() == highScores.size());
        assertEquals("first element position should have been 1", (long) 1, list.get(0).getPosition().longValue());
        assertEquals("first elements should have been equals", highScores.get(0).getUserId(),
                list.get(0).getUserId());
        assertEquals("last elements should have been equals", highScores.get(highScores.size() - 1).getUserId(),
                list.get(list.size() - 1).getUserId());
    }

    @Test
    public void testShouldGetEmptyScoreResponse() {
        when(userScoreService.findHighScores()).thenReturn(new ArrayList<>());
        List<ScoreResponse> list = service.getScoreList();
        assertTrue("list size should have been empty ", list.isEmpty());
    }

    @Test
    public void testShouldGetUserScorePosition() {
        List<UserScore> highScores = generateRandomUserScore(100);
        int expectedPosition = 28;
        Long userId = highScores.get(expectedPosition).getUserId();
        Long score = highScores.get(expectedPosition).getScore();
        when(userScoreService.getScoreByUserId(userId)).thenReturn(Optional.of(highScores.get(expectedPosition)));
        when(userScoreService.countByScoreGreaterThan(score)).thenReturn((long) expectedPosition);
        Optional<ScoreResponse> response = service.getUserPosition(userId);
        assertEquals("position should have been " + (expectedPosition + 1), (long) expectedPosition + 1,
                response.get().getPosition().longValue());
    }

    @Test
    public void testShouldGroupPositionWhenSameScore() {
        UserScore user1 = UserScore.builder().userId(TestUtils.randomUInt()).score(100L).build();
        UserScore user2 = UserScore.builder().userId(TestUtils.randomUInt()).score(100L).build();
        UserScore user3 = UserScore.builder().userId(TestUtils.randomUInt()).score(10L).build();
        UserScore user4 = UserScore.builder().userId(TestUtils.randomUInt()).score(1L).build();
        UserScore user5 = UserScore.builder().userId(TestUtils.randomUInt()).score(1L).build();
        List<UserScore> highScores = new ArrayList<>();
        highScores.add(user1);
        highScores.add(user2);
        highScores.add(user3);
        highScores.add(user4);
        highScores.add(user5);

        when(userScoreService.findHighScores()).thenReturn(highScores);
        List<ScoreResponse> list = service.getScoreList();
        System.out.println(list);
        assertTrue("list size should have been " + highScores.size(), list.size() == highScores.size());
        assertEquals("first and second positions should have been the same ", list.get(0).getPosition(),
                list.get(1).getPosition());
        assertEquals("third position should have been 3", (long) 3, list.get(2).getPosition().longValue());
        assertEquals("fourth and fifth positions should have been the same ", list.get(3).getPosition(),
                list.get(4).getPosition());

    }

    private List<UserScore> generateRandomUserScore(int count) {
        List<UserScore> list = new ArrayList<>();
        Long score = TestUtils.randomUInt();
        for (int i = 0; i < count; i++) {
            list.add(UserScore.builder().userId(TestUtils.randomUInt()).score(score + 100).build());
        }
        return list;
    }

   

}