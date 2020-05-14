package org.gamerpuppy.euler;

import java.util.*;

public class Euler203 {


    public static long solve() {
        List<List<Long>> pascalsTriangle = getPascalsTriangle(50);
        List<Long> distinctNumbers = getDistinct(pascalsTriangle);
        List<Integer> primes = Util.generatePrimesToBound((long)1e5);

        long squareFreeSum = 0;
        for (long num : distinctNumbers) {
            boolean squareFree = true;
            for (int prime : primes) {
                long square = prime*prime;
                if (num % square == 0) {
                    squareFree = false;
                    break;
                }
            }
            if (squareFree) {
                squareFreeSum += num;
            }
        }
        return squareFreeSum;
    }

    public static void main(String[] args) {
        System.out.print(solve());
    }

    static void printTriangle(List<List<Long>> lists) {
        for (List<Long> list : lists) {
            for (Long o : list) {
                System.out.print(o.toString()+" ");
            }
            System.out.println();
        }
    }

    static List<Long> getDistinct(List<List<Long>> lists) {
        Set<Long> distinctSet = new HashSet<>();
        for (List<Long> list : lists) {
            distinctSet.addAll(list);
        }
        return new ArrayList<>(distinctSet);
    }

    static List<List<Long>> getPascalsTriangle(int n) {
        List<List<Long>> triangle = new ArrayList<>();
        for (int r = 0; r <= n; r++) {
            if (r == 0) {
                List<Long> firstRow = new ArrayList<>();
                firstRow.add(1L);
                triangle.add(firstRow);
                continue;
            }

            List<Long> prevRow = triangle.get(r-1);
            List<Long> curRow = new ArrayList<>();
            for (int c = 0; c <= r; c++) {
                long left = c > 0 ? prevRow.get(c-1) : 0;
                long right = c < r ? prevRow.get(c) : 0;
                curRow.add(left+right);
            }
            triangle.add(curRow);
        }
        return triangle;
    }

}
