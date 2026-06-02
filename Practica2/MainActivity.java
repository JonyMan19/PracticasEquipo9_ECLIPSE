package com.example.memorama;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;

import com.example.endlessrunner.R;

public class MainActivity extends Activity {

    // Emojis usados como pares de cartas
    String[] simbolos = {"🍎","🍎","🍌","🍌","🍇","🍇","🍓","🍓",
            "🍒","🍒","🍑","🍑","🥝","🥝","🍍","🍍"};

    ArrayList<String> cartas = new ArrayList<String>();

    // Adaptador del GridView
    ArrayAdapter<String> adaptador;

    // Control de cartas volteadas
    int primerIndex  = -1;
    int segundoIndex = -1;
    boolean esperando = false;

    // Cartas ya encontradas
    boolean[] encontrada;

    // Contador de intentos
    int intentos = 0;
    int paresEncontrados = 0;

    TextView tvIntentos, tvPares;
    GridView gridView;
    Button btnReiniciar;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIntentos  = (TextView) findViewById(R.id.tvIntentos);
        tvPares     = (TextView) findViewById(R.id.tvPares);
        gridView    = (GridView) findViewById(R.id.gridView);
        btnReiniciar = (Button) findViewById(R.id.btnReiniciar);

        iniciarJuego();

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJuego();
            }
        });
    }

    void iniciarJuego() {
        // Preparar cartas mezcladas
        cartas.clear();
        for (String s : simbolos) cartas.add(s);
        Collections.shuffle(cartas);

        encontrada    = new boolean[cartas.size()];
        primerIndex   = -1;
        segundoIndex  = -1;
        esperando     = false;
        intentos      = 0;
        paresEncontrados = 0;

        actualizarHUD();

        // Adaptador: muestra "?" si no está volteada/encontrada
        adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cartas) {

            @Override
            public View getView(int pos, View convertView, android.view.ViewGroup parent) {
                Button btn;
                if (convertView == null) {
                    btn = new Button(getContext());
                    btn.setTextSize(28);
                } else {
                    btn = (Button) convertView;
                }

                if (encontrada[pos]) {
                    // Carta encontrada: mostrar símbolo, fondo verde
                    btn.setText(cartas.get(pos));
                    btn.setBackgroundColor(Color.rgb(80, 180, 80));
                    btn.setEnabled(false);
                } else if (pos == primerIndex || pos == segundoIndex) {
                    // Carta volteada: mostrar símbolo, fondo amarillo
                    btn.setText(cartas.get(pos));
                    btn.setBackgroundColor(Color.rgb(255, 220, 50));
                    btn.setEnabled(true);
                } else {
                    // Carta oculta
                    btn.setText("?");
                    btn.setBackgroundColor(Color.rgb(70, 130, 200));
                    btn.setEnabled(true);
                }

                final int index = pos;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alTocarCarta(index);
                    }
                });

                return btn;
            }
        };

        gridView.setAdapter(adaptador);
    }

    void alTocarCarta(int index) {
        // Ignorar si ya está esperando, ya encontrada, o es la misma carta
        if (esperando) return;
        if (encontrada[index]) return;
        if (index == primerIndex) return;

        if (primerIndex == -1) {
            // Primera carta
            primerIndex = index;
            adaptador.notifyDataSetChanged();

        } else {
            // Segunda carta
            segundoIndex = index;
            adaptador.notifyDataSetChanged();
            intentos++;
            actualizarHUD();
            esperando = true;

            // Revisar par después de 800ms
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    verificarPar();
                }
            }, 800);
        }
    }

    void verificarPar() {
        if (cartas.get(primerIndex).equals(cartas.get(segundoIndex))) {
            // ¡Par encontrado!
            encontrada[primerIndex]  = true;
            encontrada[segundoIndex] = true;
            paresEncontrados++;
            actualizarHUD();

            // ¿Ganó?
            if (paresEncontrados == cartas.size() / 2) {
                Toast.makeText(this,
                    "¡Ganaste en " + intentos + " intentos! 🎉",
                    Toast.LENGTH_LONG).show();
            }
        }

        primerIndex  = -1;
        segundoIndex = -1;
        esperando    = false;
        adaptador.notifyDataSetChanged();
    }

    void actualizarHUD() {
        tvIntentos.setText("Intentos: " + intentos);
        tvPares.setText("Pares: " + paresEncontrados + " / " + (simbolos.length / 2));
    }
}