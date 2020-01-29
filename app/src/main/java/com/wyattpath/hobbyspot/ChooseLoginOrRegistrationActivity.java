package com.wyattpath.hobbyspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ChooseLoginOrRegistrationActivity extends AppCompatActivity {

    private Button mLogin, mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_or_registration);

        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);

        mLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseLoginOrRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        });

        mRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseLoginOrRegistrationActivity.this, RegistrationActivity.class);
            startActivity(intent);
            return;
        });
    }
}
