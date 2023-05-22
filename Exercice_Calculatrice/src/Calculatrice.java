import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculatrice
{
    private Map<Character, IOperation> operations;

    public Calculatrice()
    {
        operations = new HashMap<>();
        operations.put('+', new Addition());
        operations.put('-', new Soustraction());
        operations.put('*', new Multiplication());
        operations.put('/', new Division());
    }
    

   public double Evaluer(String expression)
   {
       Stack<Double> operandStack = new Stack<>();
       Stack<Character> operatorStack = new Stack<>();

       for (int i = 0; i < expression.length(); i++) {
           char c = expression.charAt(i);

           if (Character.isWhitespace(c)) {
               continue;
           }

           if (Character.isDigit(c) || c == '.' || (c == '-' && (i+1 < expression.length() && Character.isDigit(expression.charAt(i+1))))) {
               boolean negative = false;
               if (c == '-') {
                   negative = true;
                   i++;
                   c = expression.charAt(i);
               }
               int start = i;
               while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                   i++;
               }
               double operand = Double.parseDouble(expression.substring(start, i));
               operandStack.push(negative ? -operand : operand);
               i--;
           } else if (operations.containsKey(c)) {
               while (!operatorStack.isEmpty() && precedence(c) <= precedence(operatorStack.peek())) {
                   performOperation(operandStack, operatorStack);
               }
               operatorStack.push(c);
           } else {
               throw new IllegalArgumentException("Invalid character: " + c);
           }
       }

       while (!operatorStack.isEmpty()) {
           performOperation(operandStack, operatorStack);
       }

       return operandStack.pop();
   }

    private void performOperation(Stack<Double> operandStack, Stack<Character> operatorStack) {
        if (operandStack.size() < 2) {
            throw new IllegalArgumentException("Insufficient values for operation");
        }
        double b = operandStack.pop();
        double a = operandStack.pop();
        char op = operatorStack.pop();
        operandStack.push(operations.get(op).Apply(a, b));
    }



    private int precedence(char operator)
    {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}
