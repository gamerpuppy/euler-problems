package org.gamerpuppy.euler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class Util {

    public static void main(String[] args) throws InterruptedException {
        int bound = (int)1e8;
        String fileName = "prime_factorizations_"+bound+".gz";
        savePrimeFactorizationsToFile(fileName, bound);

        System.out.print(1);
    }

    static long modExp(long base, long exp, long mod) {
        long curExp = 1;
        long curRes = base;
        while (true) {
            if (curExp == exp) {
                break;
            } else if (curExp * 2 <= exp) {
                curExp *= 2;
                curRes = (curRes * curRes) % mod;
            } else {
                curRes = (curRes * modExp(base, exp-curExp, mod)) % mod;
                break;
            }
        }
        return curRes;
    }

    static List<Integer> generatePrimesToBound(long bound) {
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        for (int num = 3; num <= bound; num += 2) {
            boolean isPrime = true;
            int sqrt = (int) Math.sqrt(num);
            for (Integer prime : primes) {
                if (prime > sqrt) {
                    break;
                }
                if (num % prime == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes.add(num);
            }
        }
        return primes;
    }


    public static ArrayList<Integer> getPrimesFast(int bound) {

        boolean[] sieve = new boolean[bound];
        sieve[0] = false;
        sieve[1] = false;
        sieve[2] = true;
        for (int i = 3; i < bound; i++) {
            sieve[i] = true;
        }

        int idx = 0;
        while (idx < bound) {
            if (sieve[idx]) {
                int prime = idx;
                int cur = prime + prime;
                while (cur < bound) {
                    sieve[cur] = false;
                    cur += prime;
                }
            }
            idx++;
        }

        ArrayList<Integer> ret = new ArrayList<>();
        for (int num = 0; num < bound; num++) {
            if (sieve[num]) {
                ret.add(num);
            }
        }

        return ret;
    }

    public static List<List<Integer>> getPrimeFactorizations(List<Integer> primes, int bound) {
        List<List<Integer>> factorizations = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) {
            factorizations.add(new ArrayList<>());
        }

        for (int i = 0; i < primes.size(); i++) {
            int prime = primes.get(i);
            for (int num = prime; num < bound; num += prime) {
                int cur = num;
                while (cur % prime == 0) {
                    factorizations.get(num).add(i);
                    cur /= prime;
                }
            }
        }
        return factorizations;
    }

    static List<Integer> factorNumber(List<Integer> primeList, int num) {
        List<Integer> factorization = new ArrayList<>();
        for (int i = 0; i < primeList.size(); i++) {
            int prime = primeList.get(i);
            if (prime > num) {
                break;
            }
            while (num % prime == 0) {
                num /= prime;
                factorization.add(i);
            }
        }
        return factorization;
    }

    static List<List<Integer>> factorizationsMt(List<Integer> primeList, int bound, int tc) throws InterruptedException {
        List<List<Integer>> factorizations = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) {
            factorizations.add(null);
        }

        Thread[] threads = new Thread[tc];
        for (int tn = 0; tn < tc; tn++) {
            final int threadNum = tn;
            threads[tn] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int num = threadNum; num < bound; num += tc) {
                        factorizations.set(num, factorNumber(primeList, num));
                    }
                }
            });
            threads[tn].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return factorizations;
    }

    static void savePrimeFactorizationsToFile(String fileName, int bound) throws InterruptedException {
        List<Integer> primeList = getPrimesFast(bound);
        List<List<Integer>> factorizations = factorizationsMt(primeList, bound, 16);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(new File(fileName)))))) {
            for (List<Integer> factors : factorizations) {
                for (int i = 0; i < factors.size()-1; i++) {
                    bw.write(String.valueOf(factors.get(i))+",");
                }
                if (!factors.isEmpty()) {
                    bw.write(String.valueOf(factors.get(factors.size()-1)));
                }
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
