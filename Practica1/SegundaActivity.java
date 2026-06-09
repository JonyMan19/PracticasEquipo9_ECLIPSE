package com.example.transferenciadatos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SegundaActivity extends Activity {

    private TextView tvMensajeRecibido;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        tvMensajeRecibido = (TextView) findViewById(R.id.tvMensajeRecibido);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);

        Bundle datosRecibidos = getIntent().getExtras();
        
        if (datosRecibidos != null) {
            String nombreRecuperado = datosRecibidos.getString("usuario_key");
            tvMensajeRecibido.setText("¡Bienvenido a la Pantalla 2, " + nombreRecuperado + "!");
        }

        btnRegresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); 
            }
        });
    }
}