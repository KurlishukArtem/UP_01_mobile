package com.example.wsr_pool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  SignInActivity extends AppCompatActivity {
    private Message message = new Message();
    private Optional<Users> currentUser;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        message = new Message();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,3}$";
        return email.matches(emailPattern);
    }

    public void Login(View view){
        EditText email;
        EditText pass;

        email = findViewById(R.id.EtEmail);
        pass = findViewById(R.id.EtPassword);

        if(email.getText().toString().isEmpty()) message.AlertDialog("Уведомление", "Введите почту!", this);
        else if (!isValidEmail(email.getText().toString().trim())) message.AlertDialog("Уведомление", "Неверный формат почты!", this);
        else if (pass.getText().toString().isEmpty()) message.AlertDialog("Уведомление", "Введите пароль!", this);
        else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<AuthResponse> call = apiService.login(email.getText().toString(), pass.getText().toString());

            call.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();
                        if ("success".equals(authResponse.status)) {
                            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("token", authResponse.getToken());
                            editor.apply();
                            message.AlertDialog("Login", "Authorization successful", SignInActivity.this);
                            ToMain();
                        } else {
                            message.AlertDialog("Login", authResponse.message, SignInActivity.this);
                        }
                    } else {
                        message.AlertDialog("Login", "Authorization failed", SignInActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    message.AlertDialog("System", "Authorization failed: " + t.getMessage(), SignInActivity.this);
                }
            });
        }
    }

    public void ToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}