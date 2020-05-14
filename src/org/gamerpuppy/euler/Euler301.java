package org.gamerpuppy.euler;

import java.util.*;

public class Euler301 {

    static final int bound = 1 << 30;

    static boolean playerLoses(int n) {
        return n % 2 != 0;
    }

    static int solve() {
        int sum = 0;
        for (int i = 1; i < bound; i++) {
            if (playerLoses(i)) {
                sum++;
            }
        }
        return sum;
    }

    public static void main(String[] args) {

        for (int n = 1; n < 5; n++) {
            getValue(new int[]{n, 2*n, 3*n});
        }

        ArrayList<Integer> keys = new ArrayList<>(values.keySet());
        keys.sort(Comparator.naturalOrder());
        for (Integer key : keys) {
            int value = values.get(key);
            printLine(fromKey(key), value);
        }

//        System.out.println(solve());
    }

    static void printLine(int[] h, Integer value) {
//        if (Arrays.stream(h).anyMatch(x -> x == 0)) {
//            return;
//        }
//        int z = (h[1]-h[0] + h[2]-h[1]);

        System.out.printf("(%d,%d,%d) = %d;\n", h[0], h[1], h[2], value == -1 ? 0 : 1);
    }

    static Map<Integer, Integer> values = new HashMap<>();

    static int getValue(int[] heaps) {
        int key = getKey(heaps);
        Integer value = values.get(key);

        if (value != null) {
            return value;
        }

        value = -1;
        for (int i = 0; i < heaps.length; i++) {
             for (int toTake = 1; toTake <= heaps[i]; toTake++) {
                 heaps[i] -= toTake;
                 int moveValue = -getValue(heaps);
                 heaps[i] += toTake;
                 if (moveValue == 1) {
                     value = 1;
                     break;
                 }
             }

        }

        values.put(key, value);
        return value;
    }

    static int getKey(int[] heap) {
        int[] heaps = Arrays.copyOf(heap, 3);
        Arrays.sort(heaps);
        int key = 0;
        for (int i = 0; i < 3; i++) {
            key = (key << 10) | heaps[i];
        }
        return key;
    }

    static int[] fromKey(int key) {
        int[] heaps = new int[3];
        for (int i = 2; i >= 0; i--) {
            heaps[i] = key & 0x3FF;
            key >>= 10;
        }
        return heaps;
    }


}
