package com.example.wsr_pool;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<Categorys> categoriesList = new ArrayList<>();
    private List<Dishes> dishesList = new ArrayList<>();
    private String selectedCategory = null;
    private RecyclerView categoryRecyclerView;
    private RecyclerView elementsRecyclerView;
    private CategoryAdapter categoryAdapter;
    private DishesAdapter dishesAdapter;
    private boolean internetActive = true;

    // Определяем переменные для представлений
    private LinearLayout addressLL;
    private LinearLayout searchLL;
    private EditText AddressET;
    private EditText searchET;
    private LinearLayout resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Инициализация представлений
        addressLL = findViewById(R.id.addressLL);
        searchLL = findViewById(R.id.searchLL);
        AddressET = findViewById(R.id.addressET);
        searchET = findViewById(R.id.searchET);
        resultText = findViewById(R.id.resultMessage);

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        elementsRecyclerView = findViewById(R.id.elements_recycler_view);
        elementsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Два столбца для eda_item

        setupCategoryRecyclerView();
        setupDishesRecyclerView();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        fetchCategories(apiService);
        fetchDishes(apiService);


        // Установка TextWatcher для фильтрации блюд при вводе текста
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDishes(s.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupCategoryRecyclerView() {
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            selectedCategory = category.getName();
            categoryAdapter.setSelectedCategory(selectedCategory);
            setupDishesRecyclerView();
            displayDishes();
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupDishesRecyclerView() {
        // Создание адаптера с обработчиком кликов
        dishesAdapter = new DishesAdapter(new ArrayList<>(), false, dish -> {
            // При нажатии на элемент меняем макет на один столбец и отображаем только выбранное блюдо
            elementsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            dishesAdapter.updateData(Collections.singletonList(dish)); // Показываем только выбранное блюдо
        });
        elementsRecyclerView.setAdapter(dishesAdapter);
    }

    private void fetchCategories(ApiInterface apiService) {
        Call<List<Categorys>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Categorys>>() {
            @Override
            public void onResponse(Call<List<Categorys>> call, Response<List<Categorys>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoriesList = response.body();
                    runOnUiThread(() -> {
                        categoryAdapter.updateData(categoriesList);
                        // Установите первую категорию как выбранную
                        if (!categoriesList.isEmpty()) {
                            selectedCategory = categoriesList.get(0).getName();
                            categoryAdapter.setSelectedCategory(selectedCategory);
                            displayDishes(); // Показываем блюда из первой категории
                        }
                    });
                } else {
                    Log.e("MainScreen", "Failed to fetch categories or response is null");
                }
            }

            @Override
            public void onFailure(Call<List<Categorys>> call, Throwable t) {
                Log.e("MainScreen", "API call failed: " + t.getMessage());
            }
        });
    }

    private void fetchDishes(ApiInterface apiService) {
        Call<List<Dishes>> call = apiService.getDishes();
        call.enqueue(new Callback<List<Dishes>>() {
            @Override
            public void onResponse(Call<List<Dishes>> call, Response<List<Dishes>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dishesList = response.body();
                    // Если уже есть выбранная категория, показываем блюда
                    if (selectedCategory != null) {
                        displayDishes();
                    }
                } else {
                    Log.e("MainScreen", "Failed to fetch dishes or response is null");
                }
            }

            @Override
            public void onFailure(Call<List<Dishes>> call, Throwable t) {
                Log.e("MainScreen", "API call failed: " + t.getMessage());
            }
        });
    }

    private void displayDishes() {
        List<Dishes> filteredDishes = new ArrayList<>();
        int selectedCategoryId = -1;

        for (Categorys category : categoriesList) {
            if (category.getName().equals(selectedCategory)) {
                selectedCategoryId = category.getId();
                break;
            }
        }

        for (Dishes dish : dishesList) {
            if (dish.getCategory() == selectedCategoryId) {
                filteredDishes.add(dish);
            }
        }

        // Возвращаем элементы в два столбца и обновляем данные
        elementsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dishesAdapter.updateData(filteredDishes);
    }


    public void onBackButtonClicked(View view) {
        elementsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        setupDishesRecyclerView();
        displayDishes();
    }
    // Метод фильтрации блюд по введённому тексту
    private void filterDishes(String searchText) {
        List<Dishes> filteredDishes = new ArrayList<>();
        for (Dishes dish : dishesList) {
            if (dish.getName() != null && dish.getName().toLowerCase().contains(searchText)) {
                filteredDishes.add(dish);
            }
        }
        // Возвращаем элементы в два столбца и обновляем данные
        elementsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dishesAdapter.updateData(filteredDishes);
    }

    // Метод для управления видимостью адреса и поиска
    public void setVisibilityForLL(View view) {
        if (!internetActive) {
            AlertDialog("Нет доступа к интернету", "Проверьте подключение к интернету!");
            return;
        }

        if (addressLL.getVisibility() == View.VISIBLE) {
            // Скрыть адрес, показать поиск
            addressLL.setVisibility(View.GONE);
            searchLL.setVisibility(View.VISIBLE);
            categoryRecyclerView.setVisibility(View.GONE);
            resultText.setVisibility(View.VISIBLE);
        } else {
            // Скрыть поиск, показать адрес
            searchLL.setVisibility(View.GONE);
            addressLL.setVisibility(View.VISIBLE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            resultText.setVisibility(View.GONE);
        }
    }

    // Метод для проверки адреса
    public void checkAddress(View view) {
        if (!internetActive) {
            AlertDialog("Нет доступа к интернету", "Проверьте подключение к интернету!");
            return;
        }

        String address = AddressET.getText().toString();
        if (address.isEmpty()) {
            AlertDialog("Ошибка!", "Неверный ввод адреса.");
            return;
        } else {
            // Успешно проверен адрес
            AlertDialog("Уведомление", "Адрес доставки изменен.");
        }
    }

    private void AlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

}