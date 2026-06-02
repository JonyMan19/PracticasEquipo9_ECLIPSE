package com.example.frenesideclics; // Asegúrate de que coincida con el nombre de tu paquete

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Variables globales para los componentes visuales
    private TextView tvContador;
    private Button btnClick;

    // Variable global para almacenar el progreso del juego
    private int contadorClics = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Paso 2.1: Enlazar las variables de Java con los componentes del XML
        tvContador = (TextView) findViewById(R.id.tvContador);
        btnClick = (Button) findViewById(R.id.btnClick);

        // Paso 2.2: Configurar el evento de clic para el botón gigante
        btnClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Incrementar la variable del contador en 1
                contadorClics++;

                // Actualizar el TextView en tiempo real convirtiendo el int a String
                tvContador.setText(String.valueOf(contadorClics));
            }
        });
    }
}