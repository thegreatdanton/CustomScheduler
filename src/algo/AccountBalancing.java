package algo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccountBalancing {

    int miniumTransactions = Integer.MAX_VALUE;

    public static void main(String[] args) {
        int[][] input = {
                {0, 1, 10}, {2, 0, 5}
        };
        AccountBalancing accountBalancing = new AccountBalancing();
        int result = accountBalancing.minimiseTransactions(input);
        System.out.println(result);
    }

    /**
     * First loop over transactions and obtain each person’s remaining amount of money (either 0, positive or negative). Then do backtrack.
     *
     * Starting from index 0 and transaction count 0, loop over each remaining amount after the start index.
     * If a next amount has a different sign with the amount at the start index,
     * then add the amount of the start index by the current amount with the number of transactions increased by 1,
     * and do backtrack using the current index as the start index.
     * If the end is reached, update the minimum number of transactions. Finally, return the minimum number of transactions.
     * @param transactions
     * @return
     */
    public int minimiseTransactions(int[][] transactions){
        Map<Integer, Integer> ledger = new HashMap<>();
        for(int[] transaction : transactions){
            int lender = transaction[0];
            int borrower = transaction[1];
            int amount = transaction[2];

            int balance1 = ledger.getOrDefault(lender, 0) - amount;
            int balance2 = ledger.getOrDefault(borrower, 0) + amount;

            ledger.put(lender, balance1);
            ledger.put(borrower, balance2);
        }

        int groupSize = ledger.size();
        int[] balances = new int[groupSize];
        Set<Integer> people = ledger.keySet();

        int index = 0;
        for(int person : people){
            int balance = ledger.get(person);
            balances[index++] = balance;
        }

        backTrack(balances, 0, 0);

        return miniumTransactions;

    }

    private void backTrack(int[] balances, int start, int count) {
        int length = balances.length;

        while (start < length && balances[start] == 0){
            start++;
        }
        if(start == length){
            miniumTransactions = Math.min(miniumTransactions, count);
        } else {
            boolean isPositive = balances[start] > 0;
            for(int i=start + 1; i < length; i++){
                if((balances[i] > 0) ^ isPositive){
                    balances[i] = balances[i] + balances[start];
                    backTrack(balances, start + 1, count + 1);
                    balances[i] = balances[i] - balances[start];
                }
            }
        }
    }
}
