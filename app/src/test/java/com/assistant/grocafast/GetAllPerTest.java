package com.assistant.grocafast;

import static org.junit.Assert.assertArrayEquals;
import com.grocafast.grocafast.GetAllPermutations;

import org.junit.Test;

public class GetAllPerTest {


    @Test
    public void testGetPer() {
        int[] inputArray = {1,2};
        int factorial = 2; // 2! = 4, since there are 2 elements in the input array

        int[][] expectedOutput = {
                {1,2},
                {2,1}
        };
        int[][] actualOutput = GetAllPermutations.getPermutations(inputArray, factorial);
        assertArrayEquals(expectedOutput, actualOutput);
    }

}
