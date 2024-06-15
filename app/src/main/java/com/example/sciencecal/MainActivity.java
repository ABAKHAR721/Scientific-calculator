package com.example.sciencecal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView expressionTextView;
    private Calculator calculator;
    private StringBuilder currentExpression; // Store the current expression

    private String lastExpression; // Store the last expression
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator = new Calculator();
        expressionTextView = findViewById(R.id.expressionTextView);
        currentExpression = new StringBuilder(); // Initialize the StringBuilder
        lastExpression = ""; // Initialize lastExpression as empty
        initializeButtons();
    }

    private void initializeButtons() {
        int[] buttonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
                R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMul, R.id.buttonDiv,
                R.id.buttonDot, R.id.buttonEqual, R.id.buttonClear, R.id.buttonBackspace,
                R.id.buttonSqrt, R.id.buttonLog, R.id.buttonLn, R.id.buttonSin,
                R.id.buttonCos, R.id.buttonTan, R.id.buttonOpenParen, R.id.buttonCloseParen,
                R.id.buttonPower // Add the Power button
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    String buttonText = b.getText().toString();
                    handleButtonClick(buttonText);
                }
            });
        }

        // Handle Backspace button click
        Button buttonBackspace = findViewById(R.id.buttonBackspace);
        buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackspace();
            }
        });

        // Handle Restore Last Expression button click
        Button buttonRestore = findViewById(R.id.buttonRestore);
        buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentExpression.setLength(0); // Clear the current expression
                currentExpression.append(lastExpression); // Append last expression to current expression
                expressionTextView.setText(currentExpression.toString());
            }
        });
    }

    private void handleButtonClick(String buttonText) {
        if (buttonText.equals("=")) {
            lastExpression = currentExpression.toString(); // Store the current expression before evaluation
            evaluateExpression(); // Evaluate the expression when "=" is clicked
        } else if (buttonText.equals("C")) {
            expressionTextView.setText("");
            currentExpression.setLength(0); // Clear the StringBuilder
        } else if (buttonText.equals("Restore")) {
            expressionTextView.setText("");
            currentExpression.setLength(0); // Clear the current expression
            currentExpression.append(lastExpression); // Update the current expression to match the last expression
            expressionTextView.setText(currentExpression.toString()); // Update the TextView
        } else {
            currentExpression.append(buttonText); // Append the button text to the current expression
            expressionTextView.setText(currentExpression.toString());
            expressionTextView.setTextColor(Color.BLACK); // Reset text color to black
        }
    }

    private void handleBackspace() {
        String currentText = expressionTextView.getText().toString();
        if (!currentText.isEmpty()) {
            // Remove the last character from the StringBuilder
            currentExpression.deleteCharAt(currentExpression.length() - 1);
            expressionTextView.setText(currentExpression.toString());
        }
    }

    private void evaluateExpression() {
        String expression = currentExpression.toString();
        try {
            double result = calculator.evaluate(expression);
            expressionTextView.setText(String.valueOf(result));
            currentExpression.setLength(0);
            currentExpression.append(result);
            expressionTextView.setTextColor(Color.BLACK); // Reset text color to black
        } catch (Exception e) {
            expressionTextView.setText("Error");
            expressionTextView.setTextColor(Color.RED); // Set text color to red
        }
    }
}
