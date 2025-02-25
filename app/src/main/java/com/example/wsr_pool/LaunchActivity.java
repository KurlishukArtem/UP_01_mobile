package com.example.wsr_pool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LaunchActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    public static boolean internetActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        progressBar = findViewById(R.id.PbLoad);
        if (NetworkUtils.isConnectedToNetwork(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetActive = true;
                    onBoard();
                }
            }, 3000);
        } else {
            internetActive = false;
            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBoard();
                }
            }, 1000);
        }
    }

    private void onBoard() {
        Intent intent = new Intent(this, BoardingActivity.class);
        startActivity(intent);
    }
}