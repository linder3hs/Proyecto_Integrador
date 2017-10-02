package com.linderhassinger.proyecto_integrador;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private TextView txtCorreo,txtPass;
    private Button btnIngresar;
    private static final int SING_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtPass = (EditText)findViewById(R.id.txtPass);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SING_CODE);
            }
        });

        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        //Enviar los datos hacia el Web Service y
                        //Recibir los datos que me envia el Web Service
                        final String res=enviarPost(txtCorreo.getText().toString(),txtPass.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJSON(res);
                                if(r>0){
                                    Intent i=new Intent(getApplicationContext(),MapsnavigationActivity.class);//Prueba del servicio
                                    startActivity(i);
                                }else {
                                    Toast.makeText(getApplicationContext(),"Correo o Contraseña incorrectos",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });

    }

    //Metodo para consumir el WEB SERVICE
    public String enviarPost(String correo, String pass){
        String urlparametros="correo="+correo+"&pass="+pass;
        HttpURLConnection conection=null;
        String respuesta="";
        try {
            //Se va a la Web y se envia los datos
            URL url=new URL("http://192.168.0.31/WebService/valida.php");//Cambiar el ip - ya que no es estable por que es local
            conection=(HttpURLConnection)url.openConnection();
            conection.setRequestMethod("POST");
            conection.setRequestProperty("Content-Length",""+Integer.toString(urlparametros.getBytes().length));

            conection.setDoOutput(true);
            DataOutputStream wr=new DataOutputStream(conection.getOutputStream());
            wr.writeBytes(urlparametros);
            wr.close();

            //Leer el dato que nos devuelve el Web Service
            Scanner inStream=new Scanner(conection.getInputStream());

            //Recorrer el dato que nos devolvio
            while (inStream.hasNextLine()){
                respuesta+=(inStream.nextLine());
            }

        }catch (Exception e){

        }
        return respuesta.toString();
    }

    //Contar cuantos registros hay en la respuesta
    public  int objJSON(String rspta){
        int res=0;//Si el usuario es incorrecto retorna 0
        try {
            JSONArray json=new JSONArray(rspta);
            if(json.length()>0){
                res=1;//Si el usuario es correcto retorna 1
            }
        }catch (Exception e){

        }
        return res;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SING_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            goMainScreen();
        }else{
            Toast.makeText(this, R.string.not_login, Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MapsnavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
