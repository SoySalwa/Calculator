package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.calculator.databinding.ActivityMainBinding;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Expression expression;
    private boolean lastNumeric = false;
    private boolean stateError = false;
    private boolean lastDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClearClick(View view) {
        binding.dataText.setText("");
        binding.dataResult.setText("");
        stateError = false;
        lastNumeric = false;
        lastDot = false;
        binding.dataResult.setVisibility(View.GONE);
    }

    public void onEqualClick(View view) {
        onEqual();
        String text = binding.dataResult.getText().toString();
        if (!text.isEmpty()) {
            text = text.substring(1);
            binding.dataText.setText(text);
        }
    }

    public void onOperatorClick(View view) {
        if (!stateError && lastNumeric) {
            Button button = (Button) view;
            binding.dataText.append(button.getText());
            lastDot = false;
            lastNumeric = false;
            onEqual();
        }
    }

    public void onCommonClick(View view) {
        Button button = (Button) view;
        if (stateError) {
            binding.dataText.setText(button.getText());
            stateError = false;
        } else {
            binding.dataText.append(button.getText());
        }

        lastNumeric = true;
        onEqual();
    }

    public void onBackClick(View view) {
        String text = binding.dataText.getText().toString();
        if (!text.isEmpty()) {
            text = text.substring(0, text.length() - 1);
            binding.dataText.setText(text);
        }
        try {
            String textAfterRemoval = binding.dataText.getText().toString();
            char lastChar = textAfterRemoval.charAt(textAfterRemoval.length() - 1);
            if (Character.isDigit(lastChar)) {
                onEqual();
            }
        } catch (Exception ex) {
            binding.dataResult.setText("");
            binding.dataResult.setVisibility(View.GONE);
            Log.e("Last char error", ex.toString());
        }
    }



    public void onEqual() {
        if (lastNumeric && !stateError) {
            String txt = binding.dataText.getText().toString();
            expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                String resultSt = String.valueOf(result);
                String messageResult = getString(R.string.result_format, resultSt);
                binding.dataResult.setVisibility(View.VISIBLE);
                binding.dataResult.setText(messageResult);
            } catch (ArithmeticException ex) {
                Log.e("Evaluate Error", ex.toString());
                binding.dataResult.setText(getString(R.string.error_message));
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}
