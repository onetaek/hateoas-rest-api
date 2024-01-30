package com.devframe.common;

public class LongGenerator {

    /**
     * 매개변수로 들어온 excludedValues 를 제외한 값으로 랜덤 long 타입의 숫자를 생성한다.
     */
    public static long createRandomLongExcluding(long... excludedValues) {
        long randomLong;
        do {
            randomLong = (long) (Math.random() * (Long.MAX_VALUE - Long.MIN_VALUE + 1) + Long.MIN_VALUE);
        } while (contains(excludedValues, randomLong));

        return randomLong;
    }

    private static boolean contains(long[] array, long value) {
        for (long item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }
}
