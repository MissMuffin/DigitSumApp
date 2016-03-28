package de.muffinworks.quersummenapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private TextView resultIterativeText;
    private EditText inputField;
    private Button submitButton;
    private ImageView clearInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInputField();
        initClearInput();
        initSubmitButton();
        initResultTexts();
    }

    private void initInputField() {
        inputField = (EditText)findViewById(R.id.input_field);

        //sets cursor in edittext visible on click and touch
        inputField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputField.setCursorVisible(true);
            }
        });
        //touch sometimes called without on click
        inputField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputField.setCursorVisible(true);
                return false;
            }
        });

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            //handles visibility of clear input view depending if input exists or not
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearInput.setVisibility(View.VISIBLE);
                } else {
                    clearInput.setVisibility(View.INVISIBLE);
                }
            }
        });

        //listens to done button in soft keyboard
        inputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inputField.setCursorVisible(false);
                    setResult(inputField.getText().toString());
                }
                return false;
            }
        });
    }

    private void initSubmitButton() {
        submitButton = (Button)findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(inputField.getText().toString());
                //closes soft keyboard and sets edittext cursor invisible
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                inputField.setCursorVisible(false);
            }
        });
    }

    private void initClearInput() {
        clearInput = (ImageView)findViewById(R.id.clear_input);

        //clears input and result fields on click, opens soft keyboard and sets cursor visible in edittext
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputField.setText("");
                resultText.setText("");
                resultIterativeText.setText("");
                inputField.setCursorVisible(true);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void initResultTexts() {
        resultText = (TextView)findViewById(R.id.result_text);
        resultIterativeText = (TextView)findViewById(R.id.result_iterative_text);
    }

    private void setResult(String input) {

        resultText.setText(calc(input, false));
        resultIterativeText.setText(calc(input, true));
    }

    private String calc(String number, boolean iterative) {
        int digitSum = 0;
        String result = "";

        for (char c : number.toCharArray()) {
            digitSum += Character.getNumericValue(c);
        }
        result += digitSum;

        if (iterative && result.length()>1) result = calc(result, true);

        return result;
    }
}
