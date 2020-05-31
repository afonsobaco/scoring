package com.aviz;

import static org.junit.Assert.assertNotNull;

import com.aviz.controller.UserScoreController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserScoreController controller;

	@Test
	void contextLoads() {
		assertNotNull("should not have been null", controller);
	}

}
