package algo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Calculator {
    private Map<String, String> supportedOperators;

    public Calculator() {
        supportedOperators = new HashMap<>();
        supportedOperators.put("add", "+");
        supportedOperators.put("sub", "-");
        supportedOperators.put("mul", "*");
        supportedOperators.put("div", "/");
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        String input1 = "add(1,2)";
        //String input2 = "add(1,add(1,2))";
        String input2 = "add(1,add(1,sub(3,add(4,5))))";
        String input3 = "add(1,add(1,sub(3,add(4,5)))";
        //System.out.println(calculator.calculateResult(input3));
        System.out.println(calculator.calculateResult(input3));

    }

    private int calculateResult(String input){
        Integer number = null;
        Stack<String> stack = new Stack<>();
        Stack<Integer> integerStack = new Stack<>();
        int n = input.length();
        int index = 0;
        StringBuilder operatorBuilder = new StringBuilder();
        int bracketCount = 0;
        while(index < n){
            Character c = input.charAt(index);
            if(Character.isAlphabetic(c)){
                operatorBuilder.append(c);
                index++;
            }

            else if(Character.isDigit(c)){
                if(number == null){
                    number = 0;
                }
                number = number * 10 + (c - '0');
                index++;
            }

             else if(c == ','){
                integerStack.push(number);
                number = null;
                index++;
            }

            else if(c == '('){
                bracketCount++;
                if(!isValidOperator(operatorBuilder.toString())){
                    throw new RuntimeException("Invalid input");
                }
                String operator = operatorBuilder.toString();
                if(number != null){
                    integerStack.push(number);
                    number = null;
                }
                stack.push(supportedOperators.get(operator));
                operatorBuilder = new StringBuilder();
                index++;
            }

            else if(c == ')'){
                bracketCount--;
                /*int tempResult = 0;
                int number2 = integerStack.pop();
                integerStack.push(computeResult(number2, number, stack.pop()));*/
                if(index == n-1 && number != null){
                    integerStack.push(number);
                }
                index++;
            }
        }

        if(stack.isEmpty() || integerStack.isEmpty() || bracketCount != 0){
            throw new RuntimeException("Invalid input");
        }
        while(!stack.isEmpty() && !integerStack.isEmpty()){
            int number1 = integerStack.pop();
            int number2 = integerStack.pop();

            integerStack.push(computeResult(number2, number1, stack.pop()));
        }
        return integerStack.pop();
    }

    private boolean isValidOperator(String operator){
        return supportedOperators.containsKey(operator);
    }

    private int computeResult(int a, int b, String operator){
        switch (operator){
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                return 0;
        }
    }
}
