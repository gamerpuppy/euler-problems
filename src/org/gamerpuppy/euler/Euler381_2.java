package org.gamerpuppy.euler;

import java.util.*;

public class Euler381_2 {

    static final int bound = (int) 2e5;
    static List<Integer> primeList = Util.getPrimesFast(bound);

    static int subProblem(int prime, int x) {
        long sum = 0;
        for (int i = prime-5; i < prime; i++) {
            x = (x * i) % prime;
            sum += x;
        }
        return (int) (sum % prime);
    }

    static long solve() {
        int[] solutions = new int[primeList.size()];
        Arrays.fill(solutions, 1);

        int start = 3;
        for (int num = 2; start < primeList.size(); num++) {
            for (int i = start; i < primeList.size(); i++) {
                int prime = primeList.get(i);
                if (num + 5 == prime) {
                    start++;
                    solutions[i] = subProblem(prime, solutions[i]);
                } else {
                    solutions[i] = (int)(((long)num * solutions[i]) % prime);
                }
            }
        }

        long sum = 4;
        for (int i = 3; i < solutions.length; i++) {
            sum += solutions[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        long answer;
        long start = System.currentTimeMillis();

        answer = solve();

        long elapsed = System.currentTimeMillis()-start;
        System.out.println(answer);
        System.out.println(elapsed);
    }

}
