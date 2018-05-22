package com.example.flaviomassimo.smartage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivitySigIn extends AppCompatActivity implements View.OnClickListener{
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sig_in);

        mButton=(Button) findViewById(R.id.login_register);
        findViewById(R.id.login_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_register){
            Intent intent = new Intent(MainActivitySigIn.this,LoginActivity.class);
            startActivity(intent);

        }
    }
}
