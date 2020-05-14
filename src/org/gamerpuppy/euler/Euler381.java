package org.gamerpuppy.euler;

import java.time.LocalDateTime;
import java.util.*;

public class Euler381 {

    static final int bound = (int) 2e5;
    static final List<Integer> primeList = Util.getPrimesFast(bound);
    static final Map<Integer, Integer> primeIdxMap = new HashMap<>();

    static List<List<Integer>> factorizations;

    static {
        for (int i = 0; i < primeList.size(); i++) {
            primeIdxMap.put(primeList.get(i), i);
        }
    }

    final List<Integer> primeCounts;

    public Euler381() {
        primeCounts = new ArrayList<>(primeList.size());
        for (int i = 0; i < primeList.size(); i++) {
            primeCounts.add(0);
        }
    }

    int subProb(int prime) {
        long x = 1;
        for (int i = 0; i < primeCounts.size(); i++) {
            if (primeCounts.get(i) == 0) {
                break;
            }
            int p = primeList.get(i);
            int count = primeCounts.get(i);
            x = (x * Util.modExp(p, count, prime)) % prime;
        }

        long sum = 0;
        for (int i = prime-5; i < prime; i++) {
            x = (x * i) % prime;
            sum += x;
        }
        return (int) (sum % prime);
    }

    long solvePart(int tn, int tc) {
        Set<Integer> subSet = new HashSet<>(primeList.size()/tc);
        for (int i = tn; i < primeList.size(); i+=tc) {
            subSet.add(primeList.get(i));
        }

        long sum = 0;
        for (int num = 2; num < bound; num++) {
//            if (num % (int) 2e6 == 0) {
//                System.out.println(LocalDateTime.now()+" "+num+" "+sum+" "+tn);
//            }

            int futureNum = num+5;
            if (subSet.contains(futureNum)) {
                sum += subProb(futureNum);
            }

            List<Integer> factors = getFactors(num);
            for (int x : factors) {
                primeCounts.set(x, primeCounts.get(x)+1);
            }
        }
        return sum;
    }

    static List<Integer> getFactors(int num) {
        return factorizations.get(num);
//        List<Integer> ret = Util2.getFactorization(num);
//        for (int i = 0; i < ret.size(); i++) {
//            ret.set(i, primeIdxMap.get(ret.get(i)));
//        }
//        return ret;
    }

    static long solveMt(int tc) throws InterruptedException {
        long[] results = new long[tc];
        Thread[] threads = new Thread[tc];
        for (int i = 0; i < tc; i++) {
            final int finalI = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Euler381 euler381 = new Euler381();
                    results[finalI] = euler381.solvePart(finalI, tc);
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        return Arrays.stream(results).sum() + 4;
    }

    public static void main(String[] args) throws InterruptedException {
        Util2.init(bound);
        factorizations = Util2.factorizationsMt(primeList, bound, 16);

        long answer;
        long start = System.currentTimeMillis();

        answer = solveMt(8);

        long elapsed = System.currentTimeMillis()-start;
        System.out.println(answer);
        System.out.println(elapsed);
    }

}
