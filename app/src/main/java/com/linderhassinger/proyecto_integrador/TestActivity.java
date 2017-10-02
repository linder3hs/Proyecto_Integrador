package com.linderhassinger.proyecto_integrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    private TextView txtCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        txtCorreo = (TextView) findViewById(R.id.txtCorreo);
        MostrarDatos();
    }

    public void MostrarDatos(){
        String correo = this.getIntent().getExtras().getString("correo");
        txtCorreo.setText(correo);
    }
}
