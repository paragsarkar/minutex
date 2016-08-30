package com.minutex.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.minutex.R;
import com.minutex.ui.activity.MainScreen;

public class LoginActivity extends AppCompatActivity {

    private EditText et_mobile_country_code;
    private EditText et_mobile_number;
    private EditText actv_email_ids;
    private Button btn_activate,btn_skip;
    private String mobile_number,mobile_country_code,email_ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_mobile_country_code = (EditText)findViewById(R.id.et_mobile_country_code);
        et_mobile_number = (EditText)findViewById(R.id.et_mobile_number);
        actv_email_ids = (EditText)findViewById(R.id.actv_email_ids);
        btn_activate = (Button)findViewById(R.id.btn_activate);
        btn_skip = (Button)findViewById(R.id.btn_skip);
        btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_activate();
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_skipfornow();
            }
        });
    }
    private void account_activate()
    {
        mobile_number = et_mobile_number.getText().toString();
        mobile_country_code = et_mobile_number.getText().toString();
        email_ids =actv_email_ids.getText().toString();
        startActivity(new Intent(LoginActivity.this, MainScreen.class));

    }
    private void account_skipfornow()
    {
        startActivity(new Intent(LoginActivity.this, MainScreen.class));
    }
}
