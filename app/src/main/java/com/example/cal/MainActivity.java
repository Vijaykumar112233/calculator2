package com.example.cal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class
MainActivity extends AppCompatActivity {

    private EditText editText, editText2;
    private final int[] array = {R.id.but0, R.id.but1, R.id.but2, R.id.but3, R.id.but4, R.id.but5, R.id.but6, R.id.but7, R.id.but8, R.id.but9, R.id.butdot};
    private final Button[] buttons = new Button[array.length];
    private TextView textView;

    //variables
    private String pendingOperation = "=";
    private Double operand1 = null;
    private Double operand2 = null;

    //save variables
    private static final String SAVE_PENDING_OPERATION = "pendingOperation";
    private static final String SAVE_OPERAND1 = "Operand1";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.result);
        editText2 = findViewById(R.id.result2);
        textView = findViewById(R.id.operator);

        Button divide = findViewById(R.id.butdivide);
        Button plus = findViewById(R.id.butplus);
        Button minus = findViewById(R.id.butminus);
        Button multi = findViewById(R.id.butmulti);
        Button equal = findViewById(R.id.butequal);

        for (int i = 0; i < array.length; i++) {
            buttons[i] = findViewById(array[i]);
        }

        View.OnClickListener listener = view -> {
            Button b = (Button) view;

            editText2.append(b.getText().toString());
        };

        for (int i = 0; i < array.length; i++) {
            buttons[i].setOnClickListener(listener);
        }

        View.OnClickListener operator = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                String operation = b.getText().toString();
                String value = editText2.getText().toString();
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue,operation);
                }catch (NumberFormatException e){
                    editText2.setText("");
                }
                pendingOperation = operation;
                textView.setText(pendingOperation);
            }

            @SuppressLint("SetTextI18n")
            private void performOperation(Double value, String operation) {
                if (null == operand1) {
                    operand1 = value;
                } else {
                    operand2 = value;
                    if (pendingOperation.equals("=")) {
                        pendingOperation = operation;
                    }
                    switch (pendingOperation) {
                        case "=":
                            operand1 = operand2;
                            break;
                        case "/":
                            if (operand2 == 0) {
                                operand1 = 0.0;
                            } else {
                                operand1 /= operand2;
                            }
                            break;
                        case "*":
                            operand1 *= operand2;
                            break;
                        case "-":
                            operand1 -= operand2;
                            break;
                        case "+":
                            operand1 += operand2;
                            break;
                    }
                }
                editText.setText(operand1.toString());
                editText2.setText("");
            }
        };

        equal.setOnClickListener(operator);
        plus.setOnClickListener(operator);
        minus.setOnClickListener(operator);
        divide.setOnClickListener(operator);
        multi.setOnClickListener(operator);

        Button butneg = findViewById(R.id.butneg);

        butneg.setOnClickListener(view -> {
            if(editText2.length()==0){
                editText2.setText("-");
            }else{
                Editable edit = editText2.getText();
                int b = Integer.parseInt(edit.toString())*-1;
                editText2.setText(Integer.toString(b));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SAVE_PENDING_OPERATION,pendingOperation);
        if(operand1 != null){
            outState.putDouble(SAVE_OPERAND1,operand1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(SAVE_PENDING_OPERATION);
        operand1 = savedInstanceState.getDouble(SAVE_OPERAND1);
        textView.setText(pendingOperation);
    }
}