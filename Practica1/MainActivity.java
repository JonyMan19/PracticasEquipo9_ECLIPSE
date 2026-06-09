package com.example.transferenciadatos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText etNombre;
    private Button btnEnviar, btnSiguiente;
    private TextView tvSaludoLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = (EditText) findViewById(R.id.etNombre);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        tvSaludoLocal = (TextView) findViewById(R.id.tvSaludoLocal);

        btnEnviar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                
                if (!nombre.isEmpty()) {
                    tvSaludoLocal.setText("¡Hola, " + nombre + "! Bienvenido.");
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, escribe un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSiguiente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString().trim();
                
                if (!nombre.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SegundaActivity.class);
                    intent.putExtra("usuario_key", nombre);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Escribe un nombre antes de continuar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}