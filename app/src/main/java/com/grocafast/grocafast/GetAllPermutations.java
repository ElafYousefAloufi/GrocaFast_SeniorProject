package com.grocafast.grocafast;

public class GetAllPermutations {
    static int[][] possibilities;
    public static int[][] getPermutations(int[] array,int factorial){
        row = 0;
        possibilities = new int[factorial][array.length];
        possibilities = helper(array, 0,factorial);
        return possibilities;
    }
    static int row = 0;
    private static int[][] helper(int[] array, int pos,int factorial){
        if(pos >= array.length - 1){
            for(int i = 0; i < array.length - 1; i++){
                possibilities[row][i] = array[i];
            }
            if(array.length > 0) {
                possibilities[row][array.length - 1] = array[array.length - 1];
            }
            row++;
            return possibilities;
        }
        for(int i = pos; i < array.length; i++){
            int t = array[pos];
            array[pos] = array[i];
            array[i] = t;
            helper(array, pos+1,factorial);
            t = array[pos];
            array[pos] = array[i];
            array[i] = t;
        }

        return possibilities;
    }

}