package org.gamerpuppy.euler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Euler493 {

    static Map<Integer, Double> memos = new HashMap<>();

    static int getKey(int[] ballCounts) {
        int[] balls = Arrays.copyOf(ballCounts, ballCounts.length);
        Arrays.sort(balls);
        int key = 0;
        for (int i = 0; i < balls.length; i++) {
            key = (key << 4) | balls[i];
        }
        return key;
    }

    static double getExpectedMemoized(int[] ballCounts, int ballsInUrn) {
        int key = getKey(ballCounts);
        Double storedValue = memos.get(key);
        if (storedValue == null) {
            double calculatedValue = getExpected(ballCounts, ballsInUrn);
            memos.put(key, calculatedValue);
            return calculatedValue;
        } else {
            return memos.get(key);
        }
    }

    static double getExpected(int[] ballCounts, int ballsInUrn) {
        if (ballsInUrn == 50) {
            int distinct = 0;
            for (int balls : ballCounts) {
                if (balls < 10) {
                    distinct++;
                }
            }
            return distinct;
        }

        double expectedTotal = 0;
        for (int i = 0; i < ballCounts.length; i++) {
            int balls = ballCounts[i];
            if (balls <= 0) {
                continue;
            }
            double prob = (double)balls/ballsInUrn;
            ballCounts[i] -= 1;
            double expected = getExpectedMemoized(ballCounts, ballsInUrn-1);
            ballCounts[i] += 1;
            expectedTotal += prob * expected;
        }

        return expectedTotal;
    }

    public static void main(String[] args) {
        int[] startingBalls = new int[]{10,10,10,10,10,10,10};
        int ballsInUrn = Arrays.stream(startingBalls).sum();
        System.out.printf("%.9f", getExpected(startingBalls, ballsInUrn));
    }

}
