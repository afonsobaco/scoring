package com.aviz.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aviz.dto.ScoreRequest;
import com.aviz.dto.ScoreResponse;
import com.aviz.model.UserScore;
import com.aviz.service.ScoreService;
import com.aviz.service.UserScoreService;
import com.aviz.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserScoreController.class)
public class UserScoreControllerTest {

    @MockBean
    private UserScoreService userScoreService;

    @MockBean
    private ScoreService rankingService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testShouldPostUserScore() throws Exception {

        ScoreRequest request = ScoreRequest.builder().userId(1L).points(500L).build();
        UserScore score = UserScore.builder().score(request.getPoints()).userId(request.getUserId()).build();
        when(userScoreService.addPoints(anyLong(), anyLong())).thenReturn(score);

        mockMvc.perform(
                post("/score").content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$").doesNotExist());
        verify(userScoreService, times(1)).addPoints(anyLong(), anyLong());
    }

    @Test
    public void testShouldGetUserScore() throws Exception {
        Long userId = 12345L;
        Long position = 51L;
        Long score = 10500L;
        ScoreResponse response = ScoreResponse.builder().userId(userId).score(score).position(position).build();
        when(rankingService.getUserPosition(anyLong())).thenReturn(Optional.of(response));

        mockMvc.perform(get("/" + userId + "/position")).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId)).andExpect(jsonPath("$.score").value(score))
                .andExpect(jsonPath("$.position").value(position));
        verify(rankingService, times(1)).getUserPosition(anyLong());
    }

    @Test
    public void testShouldGetEmptyWhenNoScore() throws Exception {
        Long userId = 12345L;
        when(rankingService.getUserPosition(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/" + userId + "/position")).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(rankingService, times(1)).getUserPosition(anyLong());
    }

    @Test
    public void testShouldGetHighScoreList() throws Exception {
        List<ScoreResponse> expectedList = new ArrayList<>();
        expectedList.add(ScoreResponse.builder().position(1L).score(20000L).userId(TestUtils.randomUInt()).build());
        expectedList.add(ScoreResponse.builder().position(2L).score(10000L).userId(TestUtils.randomUInt()).build());
        expectedList.add(ScoreResponse.builder().position(3L).score(5000L).userId(TestUtils.randomUInt()).build());

        when(rankingService.getScoreList()).thenReturn(expectedList);

        mockMvc.perform(get("/highscorelist")).andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.highscores").exists()).andExpect(jsonPath("$.highscores", Matchers.hasSize(3)));
        verify(rankingService, times(1)).getScoreList();
    }

    @Test
    public void testShouldGetEmptyHighScoreList() throws Exception {
        List<ScoreResponse> expectedList = new ArrayList<>();
        when(rankingService.getScoreList()).thenReturn(expectedList);

        mockMvc.perform(get("/highscorelist")).andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.highscores").exists()).andExpect(jsonPath("$.highscores").isEmpty());
        verify(rankingService, times(1)).getScoreList();
    }
}