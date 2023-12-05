package com.example;

import java.util.*;

/**
 * Created by 刘千山 on 2023/9/15/015-15:33
 */
public class Test {


    @org.junit.jupiter.api.Test
    public void test() {
        int[] nums = {10, 20, 20, 99, 20, 99, 100, 100};
        findQ(8, nums);
    }

    private void findQ(int n, int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int[] new_arr = new int[50];
        int count;
        for (int i : nums) {
            count = 0;
            if (map.containsKey(nums[i])) {
                Integer integer = map.get(nums[i]);
                integer += 1;
                map.remove(nums[i]);
                map.put(nums[i], integer);
            } else {
                map.put(nums[i], 0);
            }
        }
        for (int i : nums) {
            Integer amount = map.get(nums[i]);
            if (amount == 1) {
                new_arr[i] = nums[i];
            } else {
                amount--;
                map.remove(nums[i]);
                map.put(nums[i], amount);
            }
        }
        for (int i : new_arr) {
            System.out.println(new_arr[i]);
        }
    }


    @org.junit.jupiter.api.Test
    public void arr(){
        int[][] a = new int[1][1];
        System.out.println(a);
    }
}
