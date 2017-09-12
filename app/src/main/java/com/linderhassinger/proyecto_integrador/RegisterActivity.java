package com.linderhassinger.proyecto_integrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText name, mail, contra, contaAgian;
    Button finaRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.nameRegister);
        mail = (EditText) findViewById(R.id.emailRegister);
        contra = (EditText) findViewById(R.id.passwordRegister);
        contaAgian = (EditText) findViewById(R.id.passwordRegisteragain);

        finaRegister = (Button) findViewById(R.id.finalRegister);

        finaRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = name.getText().toString();
                String email = mail.getText().toString();
                String pass = contra.getText().toString();
                String pasa = contaAgian.getText().toString();

                if (nombre.isEmpty()){
                    name.setError("Campo requerido");
                }
                if (email.isEmpty()){
                    mail.setError("Campo requerido");
                }
            }
        });


    }
}
