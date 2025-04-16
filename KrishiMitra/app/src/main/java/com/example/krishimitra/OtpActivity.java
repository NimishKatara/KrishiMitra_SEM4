package com.example.krishimitra;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity {

    private EditText[] otpFields;
    private TextView otpTimer;
    private CountDownTimer countDownTimer;
    private static final long TIMER_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Display formatted phone number
        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        String formattedNumber = "+91 - " + phoneNumber;
        TextView phoneNumberText = findViewById(R.id.phoneNumberDisplay);
        phoneNumberText.setText(formattedNumber);

        // Initialize OTP input fields
        otpFields = new EditText[]{
                findViewById(R.id.otp1),
                findViewById(R.id.otp2),
                findViewById(R.id.otp3),
                findViewById(R.id.otp4)
        };

        // Initialize Timer
        otpTimer = findViewById(R.id.otpTimer);
        startOtpCountdown();

        setupOtpInputs();
    }

    private void setupOtpInputs() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        // Move to next OTP box
                        otpFields[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otpFields[index].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (otpFields[index].getText().toString().isEmpty() && index > 0) {
                            // Move to previous OTP box when backspacing on empty field
                            otpFields[index - 1].requestFocus();
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void startOtpCountdown() {
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                otpTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                otpTimer.setText("00:00");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
