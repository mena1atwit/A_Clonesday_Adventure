package com.game.game1;

import java.lang.reflect.Array;

public class MergeSort {


    public static void mergeSort(int[] array) {
        int ArrayLength = array.length;
        int midPoint = ArrayLength / 2;
        if (ArrayLength == 1) {
            return;
        }

        int[] left = new int[midPoint];
        int[] right = new int[ArrayLength - midPoint];


        for (int i = 0; i < midPoint; i++) {
            left[i] = array[i];
        }
        for (int i = midPoint; i < ArrayLength; i++) {
            right[i - midPoint] = array[i];
        }

        mergeSort(left);
        mergeSort(right);

        merge(array, left, right);

    }

    public static void merge(int[] array, int[] left, int[] right) {
        int leftLength = left.length;
        int rightLength = right.length;

        int i = 0, j = 0, k = 0;

        while (i < leftLength && j < rightLength) {
            if (left[i] <= right[j]) {
                array[k] = left[i];
                i++;
            } else {
                array[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < leftLength) {
            array[k] = left[i];
            i++;
            k++;
        }
        while (j < rightLength) {
            array[k] = right[j];
            j++;
            k++;
        }
    }
}
