package org.gamerpuppy.euler;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class Util2 {

    static List<Integer> spf;

    static void init(int bound) {
        spf = getSpf(bound);
    }

    public static List<Integer> getSpf(int bound) {
        List<Integer> spf = new ArrayList<>(bound);
        spf.add(0);
        spf.add(1);
        for (int i = 2; i < bound; i++) {
            if (i % 2 == 0) {
                spf.add(2);
            } else {
                spf.add(i);
            }
        }

        for (int i=3; i*i<bound; i++)
        {
            // checking if i is prime
            if (spf.get(i) == i)
            {
                // marking SPF for all numbers divisible by i
                for (int j=i*i; j<bound; j+=i)
                    // marking spf[j] if it is not
                    // previously marked
                    if (spf.get(j)==j)
                        spf.set(j,i);
            }
        }
        return spf;
    }

    static List<Integer> getFactorization(int x) {
        List<Integer> ret = new ArrayList<>();
        while (x != 1)
        {
            int pf = spf.get(x);
            ret.add(pf);
            x = x / pf;
        }
        return ret;
    }

    static List<Integer> transformFactors(Map<Integer, Integer> primeIdxMap, List<Integer> ret) {
        for (int i = 0; i < ret.size(); i++) {
            ret.set(i, primeIdxMap.get(ret.get(i)));
        }
        return ret;
    }

    static List<List<Integer>> factorizationsMt(final List<Integer> primeList, int bound, int tc) throws InterruptedException {
        final List<List<Integer>> factorizations = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) {
            factorizations.add(null);
        }

        final Map<Integer, Integer> primeIdxMap = new HashMap<>(primeList.size());
        for (int i = 0; i < primeList.size(); i++) {
            primeIdxMap.put(primeList.get(i), i);
        }

        Thread[] threads = new Thread[tc];
        for (int tn = 0; tn < tc; tn++) {
            final int threadNum = tn;
            threads[tn] = new Thread(new Runnable() {
                @Override
                public void run() {

                    int inc = (int) 1e7;
                    int nextBound = inc;

                    for (int num = 2+threadNum; num < bound; num += tc) {
                        if (num >= nextBound) {
                            nextBound += inc;
                            System.out.println("factorization: "+LocalDateTime.now()+" "+num+" "+threadNum);
                        }
                        factorizations.set(num, transformFactors(primeIdxMap, getFactorization(num)));
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
//
//    static void savePrimeFactorizationsToFile(String fileName, int bound) throws InterruptedException {
//        List<List<Integer>> factorizations = factorizationsMt(bound, 16);
//
//        List<Integer> primeList = Util.getPrimesFast(bound);
//        Map<Integer, Integer> primeIdxMap = new HashMap<>();
//        for (int i = 0; i < primeList.size(); i++) {
//            primeIdxMap.put(primeList.get(i), i);
//        }
//
//        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(new File(fileName)))))) {
//            for (List<Integer> factors : factorizations) {
//                if (factors != null) {
//                    for (int i = 0; i < factors.size() - 1; i++) {
//                        bw.write(String.valueOf(primeIdxMap.get(factors.get(i))) + ",");
//                    }
//                    if (!factors.isEmpty()) {
//                        bw.write(String.valueOf(primeIdxMap.get(factors.get(factors.size() - 1))));
//                    }
//                }
//                bw.newLine();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        int bound = (int)1e8;
//        String fileName = "2_prime_factorizations_"+bound+".gz";
//        savePrimeFactorizationsToFile(fileName, bound);
//
//    }

}
