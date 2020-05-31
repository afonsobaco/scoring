package com.aviz.util;

import java.util.Random;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
    public Long randomUInt() {
        Random random = new Random();
        Long nextUserId = random.nextLong();
        if (nextUserId < 0) {
            nextUserId *= -1;
        }
        return nextUserId;
    }
}