import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Main {
    static int n;
    static String eq;
    static long ans = Long.MIN_VALUE;
    
    public static void main(String[] args) throws IOException {
        input();
        
        for (int bit = 0; bit < (1 << n); ++bit) {
            if ((bit & (bit << 1)) != 0) continue; // check if overlapping

            List<Character> charList = new LinkedList<>();
            for (char ch : eq.toCharArray()) {
                charList.add(ch);
            }

            // insert in reverse order to keep index
            for (int i = n-1; i > -1; --i) {
                if ((bit & (1 << i)) != 0) { // if insertion position found
                    int ldx = i * 2;
                    int rdx = i * 2 + 3;
                    charList.add(rdx, ')');
                    charList.add(ldx, '(');
                }
            }

            StringBuilder expr = new StringBuilder();
            for (char ch : charList) {
                expr.append(ch);
            }
            long val = eval(expr.toString());
            ans = ans < val ? val : ans;
        }

        System.out.println(ans);
    }

    // calculate expression
    public static long eval(String expr) {
        Deque<Long> numQ = new ArrayDeque<>();
        Deque<Character> opQ = new ArrayDeque<>();
        
        int i = 0;
        while (i < expr.length()) {
            char ch = expr.charAt(i);
            
            if (Character.isDigit(ch)) { // if 0 ~ 9
                long num = 0;
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    num = num*10 + (expr.charAt(i++)-'0');
                }
                numQ.push(num);
                continue;
            }
            else if (ch == '(') {
                opQ.push(ch);
            }
            else if (ch == ')') {
                while (opQ.peek() != '(') {
                    numQ.push(calc(opQ.pop(), numQ.pop(), numQ.pop()));
                }
                opQ.pop(); // remove '('
            }
            else if (ch == '+' || ch == '-' || ch == '*') {
                while (!opQ.isEmpty() && precedence(ch) <= precedence(opQ.peek())) {
                    numQ.push(calc(opQ.pop(), numQ.pop(), numQ.pop()));
                }
                opQ.push(ch);
            }
            
            ++i;
        }

        while (!opQ.isEmpty()) {
            numQ.push(calc(opQ.pop(), numQ.pop(), numQ.pop()));
        }

        return numQ.pop();
    }

    public static long calc(char op, long b, long a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
        }
        return 0;
    }

    public static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*') return 2;
        return 0;
    }
    
    public static void input() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        n >>= 1; // number of operators
        eq = br.readLine();
    }
}