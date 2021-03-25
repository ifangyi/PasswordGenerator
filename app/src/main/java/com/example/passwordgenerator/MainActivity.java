package com.example.passwordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int passwordLength = 20;
    private boolean alphabet = true, number = false, symbol = false;
    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private TextView passwordTextView, passwordLengthTextView;
    private Spinner passwordSpinner;
    private ImageButton generatePasswordImageButton;
    private Button copyPasswordButton;
    private SeekBar passwordLengthSeekbar;
    private CheckBox numberCheckBox, symbolCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordTextView = findViewById(R.id.passwordTextView);
        passwordSpinner = findViewById(R.id.passwordSpinner);
        generatePasswordImageButton = findViewById(R.id.generatePasswordImageButton);
        copyPasswordButton = findViewById(R.id.copyPasswordButton);
        passwordLengthTextView = findViewById(R.id.passwordLengthTextView);
        passwordLengthSeekbar = findViewById(R.id.passwordLengthSeekbar);
        numberCheckBox = findViewById(R.id.numberCheckBox);
        symbolCheckBox = findViewById(R.id.symbolCheckBox);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.password_spinner, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        passwordSpinner.setAdapter(arrayAdapter);
        passwordSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch(pos) {
                    case 0:
                        numberCheckBox.setVisibility(View.VISIBLE);
                        symbolCheckBox.setVisibility(View.VISIBLE);
                        passwordLengthSeekbar.setMin(8);
                        passwordLengthSeekbar.setMax(100);
                        passwordLengthSeekbar.setProgress(20);
                        alphabet = true;
                        number = numberCheckBox.isChecked();
                        symbol = symbolCheckBox.isChecked();
                        break;
                    case 1:
                        numberCheckBox.setVisibility(View.GONE);
                        symbolCheckBox.setVisibility(View.GONE);
                        passwordLengthSeekbar.setMin(3);
                        passwordLengthSeekbar.setMax(12);
                        passwordLengthSeekbar.setProgress(6);
                        alphabet = false;
                        number = true;
                        symbol = false;
                        break;
                    default:
                }
                passwordTextView.setText(generate(passwordLength, alphabet, number, symbol));
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        generatePasswordImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordTextView.setText(generate(passwordLength, alphabet, number, symbol));
            }
        });

        copyPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipData = ClipData.newPlainText("password", passwordTextView.getText().toString());
                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "已将密码复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });

        passwordLengthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordLength = progress;
                passwordLengthTextView.setText("" + passwordLength);
                passwordTextView.setText(generate(passwordLength, alphabet, number, symbol));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        numberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                number = isChecked;
                passwordTextView.setText(generate(passwordLength, alphabet, number, symbol));
            }
        });

        symbolCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                symbol = isChecked;
                passwordTextView.setText(generate(passwordLength, alphabet, number, symbol));
            }
        });
    }

    String generate(int passwordLength, boolean alphabet, boolean number, boolean symbol) {
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
                numbers = "0123456789",
                symbols = "~!@#$%^&*-=_+,.?",
                string = "";
        if(alphabet) {
            string += alphabets;
        }
        if(number) {
            string += numbers;
        }
        if(symbol) {
            string += symbols;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < passwordLength; i++) {
            stringBuilder.append(string.charAt(random.nextInt(string.length())));
        }
        return stringBuilder.toString();
    }
}