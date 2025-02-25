// OneItemScreen.java
package com.example.wsr_pool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OneItemScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_item_screen); // Убедитесь, что у вас есть соответствующий layout

        TextView dishName = findViewById(R.id.dishName);
        TextView dishPrice = findViewById(R.id.dishPrice);
        ImageView dishImage = findViewById(R.id.dish_image); // Убедитесь, что идентификатор соответствует вашему layout

        // Получение данных из Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dishName.setText(extras.getString("dish_name"));
            dishPrice.setText(extras.getString("dish_price"));

            // Загрузка изображения из drawable
            String iconName = extras.getString("dish_icon");
            int imageResource = getResources().getIdentifier(iconName, "drawable", getPackageName());
            if (imageResource != 0) {
                dishImage.setImageResource(imageResource);
            } else {
                dishImage.setImageResource(R.drawable.close); // Запасное изображение
            }
        }
    }
    public void onBackButtonClicked(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
