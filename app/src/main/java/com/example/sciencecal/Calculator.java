package com.example.sciencecal;

import java.util.Stack;

public class Calculator {

    public double evaluate(String expression) throws Exception {
        Stack<Double> values = new Stack<>();
        Stack<String> ops = new Stack<>();
        boolean lastWasOperator = true;  // To handle unary minus

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sbuf = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sbuf.append(expression.charAt(i++));
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
                lastWasOperator = false;
            } else if (ch == '(') {
                ops.push(String.valueOf(ch));
                lastWasOperator = true;
            } else if (ch == ')') {
                while (!ops.peek().equals("(")) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop(); // Remove '(' from ops
                if (!ops.isEmpty() && isFunction(ops.peek())) {
                    String func = ops.pop();
                    double arg = values.pop();
                    values.push(applyFunction(func, arg));
                }
                lastWasOperator = false;
            } else if (isOperator(ch)) {
                // Handle unary minus
                if (ch == '-' && (lastWasOperator || i == 0)) {
                    StringBuilder sbuf = new StringBuilder();
                    sbuf.append('-');
                    i++;
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        sbuf.append(expression.charAt(i++));
                    }
                    values.push(Double.parseDouble(sbuf.toString()));
                    i--;
                    lastWasOperator = false;
                } else {
                    while (!ops.empty() && hasPrecedence(ch, ops.peek().charAt(0))) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(String.valueOf(ch));
                    lastWasOperator = true;
                }
            } else if (ch == '√') {
                StringBuilder sbuf = new StringBuilder();
                sbuf.append(ch);
                i++;
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sbuf.append(expression.charAt(i++));
                }
                i--;
                if (sbuf.length() > 1) {
                    String func = sbuf.toString().substring(0, 1);
                    double arg = Double.parseDouble(sbuf.toString().substring(1));
                    values.push(applyFunction(func, arg));
                } else {
                    ops.push(sbuf.toString());
                }
                lastWasOperator = false;
            } else if (Character.isLetter(ch)) {
                StringBuilder sbuf = new StringBuilder();
                while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                    sbuf.append(expression.charAt(i++));
                }
                i--;
                ops.push(sbuf.toString());
                lastWasOperator = true;
            }
        }

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean isFunction(String str) {
        switch (str) {
            case "sin":
            case "cos":
            case "tan":
            case "sqrt":
            case "log":
            case "ln":
            case "√": // Added √ here
                return true;
            default:
                return false;
        }
    }

    private double applyFunction(String func, double arg) {
        switch (func) {
            case "sin":
                return Math.sin(Math.toRadians(arg));
            case "cos":
                return Math.cos(Math.toRadians(arg));
            case "tan":
                return Math.tan(Math.toRadians(arg));
            case "sqrt":
            case "√": // Handling √ here as well
                return Math.sqrt(arg);
            case "log":
                return Math.log10(arg);
            case "ln":
                return Math.log(arg);
            default:
                throw new IllegalArgumentException("Unsupported function: " + func);
        }
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if (op1 == '^' && op2 != '^') return false;  // Exponentiation has the highest precedence
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOp(String op, double b, double a) {
        switch (op.charAt(0)) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                return a / b;
            case '^': return Math.pow(a, b);
            default: return 0;
        }
    }

    public boolean isValidExpression(String expression) {
        return areParenthesesBalanced(expression) && !containsInvalidCharacters(expression);
    }

    private boolean areParenthesesBalanced(String expression) {
        int balance = 0;
        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                balance++;
            } else if (ch == ')') {
                balance--;
                if (balance < 0) {
                    return false;
                }
            }
        }
        return balance == 0;
    }

    private boolean containsInvalidCharacters(String expression) {
        for (char ch : expression.toCharArray()) {
            if (!Character.isDigit(ch) && ch != '.' && !isOperator(ch) && ch != '(' && ch != ')' && !Character.isLetter(ch) && ch != '√') {
                return true;
            }
        }
        return false;
    }
}
