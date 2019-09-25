package br.com.nutrifood.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.nutrifood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal

                try {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }catch (Exception e){
                    Toast.makeText(SplashActivity.this, R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                // Fecha esta activity
                finish();
            }
        }, 1000);

    }
}
