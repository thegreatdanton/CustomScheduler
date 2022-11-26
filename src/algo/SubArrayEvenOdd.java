package algo;

import java.util.HashMap;
import java.util.Map;

public class SubArrayEvenOdd {

    public int getCountOfEqualOddAndEven(int[] arr){

        int result = 0;
        int sum = 0;
        int k = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for(int i=0; i<arr.length; i++){
            if(arr[i] % 2 == 0){
                arr[i] = 1;
            } else if(arr[i] % 2 == 1){
                arr[i] = -1;
            }
            sum = sum + arr[i];

            if(sum == k){
                result++;
            }
            if(map.containsKey(sum - k)){
                result = result + map.get(sum - k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }

        return result;
    }

    public int countSubArrays(int[] arr){
        int difference = 0;
        int result = 0;

        int[] positive = new int[arr.length+1];
        int[] negative = new int[arr.length + 1];

        positive[0] = 1;

        for(int i=0; i<arr.length; i++){
            if(arr[i] % 2 == 1){
                difference++;
            } else {
                difference--;
            }

            if(difference < 0){
                result = result + negative[-difference];
                negative[-difference]++;
            } else {
                result = result + positive[difference];
                positive[difference]++;
            }
        }

        return result;

    }

    public static void main(String[] args) {
        SubArrayEvenOdd subArrayEvenOdd = new SubArrayEvenOdd();
        int[] arr = {3, 4, 6, 8, 1, 10, 5, 7};
        int result = subArrayEvenOdd.getCountOfEqualOddAndEven(arr);
        System.out.println(result);

        int[] arr2 = {3, 4, 6, 8, 1, 10, 5, 7};
        int result2 = subArrayEvenOdd.countSubArrays(arr2);
        System.out.println(result2);
    }
}
