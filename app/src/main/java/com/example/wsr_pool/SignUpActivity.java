package com.example.wsr_pool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private Message message = new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,3}$";
        return email.matches(emailPattern);
    }

    public void Regist(View view){
        EditText email;
        EditText pass;
        EditText name;
        EditText phone;

        email = findViewById(R.id.EtEmail);
        pass = findViewById(R.id.EtPassword);
        name = findViewById(R.id.EtFullName);
        phone = findViewById(R.id.EtPhone);

        if(email.getText().toString().isEmpty()) message.AlertDialog("Уведомление", "Введите почту!", this);
        else if (!isValidEmail(email.getText().toString().trim())) message.AlertDialog("Уведомление", "Неверный формат почты!", this);
        else if (pass.getText().toString().isEmpty()) message.AlertDialog("Уведомление", "Введите пароль!", this);
        else if (name.getText().toString().isEmpty()) message.AlertDialog("Уведомление", "Введите имя!", this);
        else if (phone.getText().toString().isEmpty()) message.AlertDialog("Уведомление", "Введите номер телефона!", this);
        else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<ResponseBody> call = apiService.registerUser(email.getText().toString(), pass.getText().toString(), name.getText().toString(), phone.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            System.out.println("Response: " + responseBody);
                            message.AlertDialog("Login", "Registration successful", SignUpActivity.this);
                            ToMain();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Request failed. Error code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    System.out.println("Error: " + t.getMessage());
                }
            });
        }

    }

    public void Cancle(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void ToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}