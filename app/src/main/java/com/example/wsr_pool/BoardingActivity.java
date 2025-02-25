package com.example.wsr_pool;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boarding_screen_1);
    }

    // переменная для хранения начальной координаты нажатия
    public int start_x = 0;
    public int start_y = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        // определяем действия пользователя
        switch (event.getAction()) {
            // если пользователь нажал на экран
            case MotionEvent.ACTION_DOWN:
                // запоминание точку нажатия
                start_x = (int)event.getX();
                start_y = (int)event.getY();
                break;
            case MotionEvent.ACTION_UP:
                // проверяем, что пользователь сделал действительно свайп, а не просто нажал
                if (Math.abs((int)event.getX() - start_x) > 50){
                    // если начальная координа меньше конечной
                    if (start_x < (int)event.getX()){

                        setContentView(R.layout.boarding_screen_1);
                    }
                    else {
                        if (NetworkUtils.isConnectedToNetwork(this)) {
                            setContentView(R.layout.boarding_screen_2);
                        } else {
                            setContentView(R.layout.without_internet_screen);

                        }

                    }
                }
        }
        return false;
    }

    public void SignIn(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void SignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void ToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}