package br.edu.ifsuldeminas.mch.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class PomodoroActivity extends AppCompatActivity {

    Button btnini, btnstop;
    ImageView icanchor;
    Animation rodaloca;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);




        btnini = findViewById(R.id.btnini);
        icanchor = findViewById(R.id.icanchor);
        btnstop = findViewById(R.id.btnstop);
        timer = findViewById(R.id.timer);

        btnstop.setAlpha(0);


        //carrega animação
        rodaloca = AnimationUtils.loadAnimation(this, R.anim.rodaloca);

        rodaloca.setFillAfter(true);

        btnini.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Pomodoro Inicializado", Snackbar.LENGTH_SHORT).show();
                icanchor.startAnimation(rodaloca);
                btnstop.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnini.animate().alpha(0).setDuration(300).start();
                btnini.setEnabled(false);
                btnstop.setEnabled(true);
                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnstop.setEnabled(false);
                btnini.setEnabled(true);
                icanchor.clearAnimation();
                btnini.animate().alpha(1).translationY(-80).setDuration(300).start();
                btnstop.animate().alpha(0).translationY(80).setDuration(300).start();
                timer.stop();
            }
        });
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                int elapsedMinutes = (int) (elapsedMillis / (60 * 1000));

                if (elapsedMinutes == 1) {
                    Toast.makeText(getApplicationContext(), "Passaram-se 1 miniutos!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}