package br.edu.ifsuldeminas.mch.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvText1, tvSubText1;
    Animation atg, btgone, btgtwo;
    Button btnstart;
    ImageView ivSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ocultar ActionBar
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        tvText1 = findViewById(R.id.tvText1);
        tvSubText1  = findViewById(R.id.subText1);
        btnstart = findViewById(R.id.btnstart);
        ivSample = findViewById(R.id.ivSample);

        //carrega img animação
        atg = AnimationUtils.loadAnimation(this,R.anim.atg);
        btgone = AnimationUtils.loadAnimation(this,R.anim.btgone);
        btgtwo = AnimationUtils.loadAnimation(this,R.anim.btgtwo);
        //mostra animação
        ivSample.startAnimation(atg);
        tvText1.startAnimation(btgone);
        tvSubText1.startAnimation(btgone);
        btnstart.startAnimation(btgtwo);

        btnstart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, PomodoroActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(a);
            }
        });



    }
}