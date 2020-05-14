package org.gamerpuppy.euler;

import java.util.*;

public class Euler387 {

    final long ub = (long) 1e14;
    final int primeBound = (int) Math.sqrt(ub);
    Set<Integer> primeSet = new TreeSet<>(Util.generatePrimesToBound(primeBound));
    Set<Long> harshadPrimes = new HashSet<>();

    void visit(long num, int digitSum) {
        if (num % digitSum != 0 || num > ub) {
            return;
        }

        if (checkPrimality(num / digitSum)) {
            for (int i = 1; i <= 9; i += 2) {
                long newNum = num * 10 + i;
                if (newNum > ub) {
                    break;
                }
                if (checkPrimality(newNum)) {
                    harshadPrimes.add(newNum);
                }
            }
        }

        for (int i = 0; i <= 9; i++) {
            long newNum = num * 10 + i;
            visit(newNum, digitSum + i);
        }
    }

    boolean checkPrimality(long x) {
        if (x <= primeBound) {
            return primeSet.contains((int)x);
        }

        int sqrt = (int) Math.sqrt(x);
        for (Integer prime : primeSet) {
            if (prime > sqrt) {
                break;
            }
            if (x % prime == 0) {
                return false;
            }
        }
        return true;
    }

    long solve() {
        for (int i = 1; i <= 9; i++) {
            visit(i, i);
        }

        long sum = 0;
        for (Long x : harshadPrimes) {
            sum += x;
        }
        return sum;
    }


    public static void main(String[] args) {
        System.out.println(new Euler387().solve());
    }

}
