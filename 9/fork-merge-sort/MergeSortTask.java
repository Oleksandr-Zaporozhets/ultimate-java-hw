package com.bobocode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MergeSortTask <T extends Comparable<? super T>> extends RecursiveTask<List<T>> {
    private final List<T> list;

    public MergeSortTask(List<T> list) {
        this.list = list;
    }

    @Override
    protected List<T> compute() {
        if (list.size() == 1) {
            return list;
        }

        MergeSortTask<T> leftTask = new MergeSortTask<>(new ArrayList<>(list.subList(0, list.size() / 2)));
        leftTask.fork();

        MergeSortTask<T> rightTask = new MergeSortTask<>(new ArrayList<>(list.subList(list.size() / 2, list.size())));
        List<T> rightResult = rightTask.compute();

        List<T> leftResult =  leftTask.join();
        mergeResults(leftResult, rightResult);
        return list;
    }

    private void mergeResults(List<T> leftList, List<T> rightList) {
        int leftIndex = 0;
        int rightIndex = 0;
        int resultIndex = 0;
        int leftSize = leftList.size();
        int rightSize = rightList.size();

        while (leftIndex < leftSize && rightIndex < rightSize) {
            if (leftList.get(leftIndex).compareTo(rightList.get(rightIndex)) <= 0) {
                list.set(resultIndex++, leftList.get(leftIndex++));
            } else {
                list.set(resultIndex++, rightList.get(rightIndex++));
            }
        }

        while (leftIndex < leftSize) {
            list.set(resultIndex++, leftList.get(leftIndex++));
        }

        while (rightIndex < rightSize) {
            list.set(resultIndex++, rightList.get(rightIndex++));
        }
    }
}
