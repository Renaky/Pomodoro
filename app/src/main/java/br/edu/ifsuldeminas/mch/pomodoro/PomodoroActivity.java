package br.edu.ifsuldeminas.mch.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class PomodoroActivity extends AppCompatActivity {

    Button btnini, btnstop, btnresume;
    ImageButton backButton;
    ImageView icanchor;
    Animation rodaloca;
    Chronometer timer;
    TextView rushOrSleep;
    Toolbar toolbar;
    private boolean isSnackbarShown = false;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "PomodoroPrefs";
    private static final String KEY_BASE_TIME = "BaseTime";
    private long baseTime;
    private long pausedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnini = findViewById(R.id.btnini);
        icanchor = findViewById(R.id.icanchor);
        btnstop = findViewById(R.id.btnstop);
        timer = findViewById(R.id.timer);
        rushOrSleep = findViewById(R.id.rushText);
        btnresume = findViewById(R.id.btnresume);
        backButton = findViewById(R.id.backButton);

        btnstop.setAlpha(0);
        btnresume.setAlpha(0);
        btnresume.setEnabled(false);
        btnstop.setEnabled(false);
        backButton.setImageAlpha(0);

        rushOrSleep.setText("Hora do Rush");

        // Carrega animação
        rodaloca = AnimationUtils.loadAnimation(this, R.anim.rodaloca);
        rodaloca.setFillAfter(true);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        baseTime = sharedPreferences.getLong(KEY_BASE_TIME, 0);

        btnini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pomodoro Inicializado", Snackbar.LENGTH_SHORT).show();
                icanchor.startAnimation(rodaloca);
                btnstop.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnini.animate().alpha(0).setDuration(300).start();
                btnresume.animate().alpha(0).translationY(80).setDuration(300).start();
                btnini.setEnabled(false);
                btnstop.setEnabled(true);
                btnresume.setEnabled(false);
                baseTime = SystemClock.elapsedRealtime();
                timer.setBase(baseTime);
                timer.start();
            }
        });

        btnresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Pomodoro Retomado", Snackbar.LENGTH_SHORT).show();
                btnstop.setEnabled(true);
                btnini.setEnabled(false);
                btnresume.setEnabled(false);
                icanchor.startAnimation(rodaloca);
                btnini.animate().alpha(0).translationY(-80).setDuration(300).start();
                btnstop.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnresume.animate().alpha(0).translationY(-80).setDuration(300).start();
                baseTime += SystemClock.elapsedRealtime() - pausedTime;
                timer.setBase(baseTime);
                timer.start();
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Pomodoro Encerrado", Snackbar.LENGTH_SHORT).show();
                btnstop.setEnabled(false);
                btnini.setEnabled(true);
                btnresume.setEnabled(true);
                icanchor.clearAnimation();
                btnini.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnstop.animate().alpha(0).translationY(80).setDuration(300).start();
                btnresume.animate().alpha(1).translationY(10).setDuration(300).start();
                pausedTime = SystemClock.elapsedRealtime();
                timer.stop();
                rushOrSleep.setText("Pomodoro pausado");

            }

        });

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                int elapsedMinutes = (int) (elapsedMillis / (60 * 1000));

                if (elapsedMinutes == 25) {
                    // Altera a cor do texto para vermelho
                    chronometer.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    rushOrSleep.setText("5 Minutos de Descanso ");
                } else if (elapsedMinutes == 30 && !isSnackbarShown) {
                    isSnackbarShown = true;
                    rushOrSleep.setText("Hora do Rush");
                    Snackbar.make(chronometer, "Retorne ao Rush", Snackbar.LENGTH_SHORT).show();
                    chronometer.setTextColor(getResources().getColor(android.R.color.white));
                }

                if (elapsedMinutes == 25 && !isSnackbarShown) {
                    isSnackbarShown = true;
                    Snackbar.make(chronometer, "25 minutos de foco", Snackbar.LENGTH_SHORT).show();
                }
                if (elapsedMinutes <25 || elapsedMinutes>30) {
                    rushOrSleep.setText("Hora do Rush");
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBaseTime();
                Intent intent = new Intent(PomodoroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveBaseTime();
    }

    private void saveBaseTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_BASE_TIME, baseTime);
        editor.apply();
    }
}