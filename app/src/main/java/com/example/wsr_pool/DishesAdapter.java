package com.example.wsr_pool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DishesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Dishes> dishesList;
    private boolean isSingleColumn;
    private Dishes selectedDish;
    private OnDishClickListener onDishClickListener;

    // Интерфейс для обработки нажатия на элемент
    public interface OnDishClickListener {
        void onDishClick(Dishes dish);
    }

    // Конструктор, принимающий список блюд, состояние разметки и слушатель нажатия
    public DishesAdapter(List<Dishes> dishesList, boolean isSingleColumn, OnDishClickListener listener) {
        this.dishesList = dishesList;
        this.isSingleColumn = isSingleColumn;
        this.onDishClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return (selectedDish != null && selectedDish.equals(dishesList.get(position))) ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) { // Детальный элемент
            View view = inflater.inflate(R.layout.food_detail_item, parent, false);
            return new DetailViewHolder(view);
        } else { // Обычный элемент
            View view = inflater.inflate(R.layout.food_item, parent, false);
            return new DishViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Dishes dish = dishesList.get(position);

        if (holder instanceof DishViewHolder) {
            DishViewHolder dishHolder = (DishViewHolder) holder;
            dishHolder.bind(dish);
            dishHolder.itemView.setOnClickListener(v -> {
                selectedDish = dish;
                isSingleColumn = true; // Переход на один столбец
                onDishClickListener.onDishClick(dish);
                notifyDataSetChanged(); // Уведомляем об изменении данных
            });
        } else if (holder instanceof DetailViewHolder) {
            DetailViewHolder detailHolder = (DetailViewHolder) holder;
            detailHolder.bind(dish);
        }
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    public void updateData(List<Dishes> dishesList) {
        this.dishesList = dishesList;
        notifyDataSetChanged();
    }

    // ViewHolder для eda_item
    public class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishName;
        TextView dishPrice;
        ImageView dishImage;

        public DishViewHolder(View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.dish_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
            dishImage = itemView.findViewById(R.id.dish_image);
        }

        public void bind(Dishes dish) {
            dishName.setText(dish.getName());
            dishPrice.setText(dish.getPrice() + " Р");

            // Загрузка изображения из drawable по названию из базы данных
            int imageResource = itemView.getContext().getResources().getIdentifier(
                    dish.getIcon(), "drawable", itemView.getContext().getPackageName()
            );

            if (imageResource != 0) {
                dishImage.setImageResource(imageResource);
            } else {
                dishImage.setImageResource(R.drawable.close);
            }
        }
    }

    // ViewHolder для food_details_item
    public class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView detailName;
        TextView detailPrice;
        ImageView detailImage;
        TextView tvQuantity;
        Button btnPlus, btnMinus;
        LinearLayout addToCartLL; // Добавьте этот элемент
        Button continueShopBtn; // Кнопка продолжить покупки
        Button toCartBtn; // Кнопка перейти в корзину
        Button btnMore;

        public DetailViewHolder(View itemView) {
            super(itemView);
            detailName = itemView.findViewById(R.id.dish_name);
            detailPrice = itemView.findViewById(R.id.dish_price);
            detailImage = itemView.findViewById(R.id.dish_detail_image);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            addToCartLL = itemView.findViewById(R.id.addToCartLL); // Инициализация
            continueShopBtn = itemView.findViewById(R.id.continueShopBtn); // Инициализация
            toCartBtn = itemView.findViewById(R.id.toCartBtn); // Инициализация
            btnMore = itemView.findViewById(R.id.btnMore); // Инициализация кнопки More

            // Добавляем обработчик нажатия для addToCartLL
            addToCartLL.setOnClickListener(v -> {
                // Скрываем addToCartLL, btnMinus, tvQuantity, btnPlus
                addToCartLL.setVisibility(View.GONE);
                btnMinus.setVisibility(View.GONE);
                tvQuantity.setVisibility(View.GONE);
                btnPlus.setVisibility(View.GONE);
                // Показываем continueShopBtn и toCartBtn
                continueShopBtn.setVisibility(View.VISIBLE);
                toCartBtn.setVisibility(View.VISIBLE);
            });

            btnMore.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), OneItemScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("dish_name", detailName.getText().toString());
                bundle.putString("dish_price", detailPrice.getText().toString());
                // Предположим, что dish.getIcon() возвращает имя изображения
                bundle.putString("dish_icon", selectedDish.getIcon());
                intent.putExtras(bundle);
                itemView.getContext().startActivity(intent);
            });

            if (addToCartLL == null) {
                Log.e("ViewHolder", "addToCartLL is null");
            }
            if (btnMore == null) {
                Log.e("ViewHolder", "btnMore is null");
            }
        }

        public void bind(Dishes dish) {
            detailName.setText(dish.getName());

            int basePrice = dish.getPrice();
            detailPrice.setText(basePrice + " Р"); // Измененная строка

            // Загрузка изображения из drawable по названию из базы данных
            int imageResource = itemView.getContext().getResources().getIdentifier(
                    dish.getIcon(), "drawable", itemView.getContext().getPackageName()
            );

            if (imageResource != 0) {
                detailImage.setImageResource(imageResource);
            } else {
                detailImage.setImageResource(R.drawable.close);
            }

            // Установка начального значения количества
            final int[] quantity = {1};
            tvQuantity.setText(String.valueOf(quantity[0]));

            // Обновление цены с учетом количества
            detailPrice.setText(basePrice * quantity[0] + " Р"); // Измененная строка

            // Обработчик для кнопки "+"
            btnPlus.setOnClickListener(v -> {
                quantity[0]++;
                tvQuantity.setText(String.valueOf(quantity[0]));
                detailPrice.setText(basePrice * quantity[0] + " Р"); // Измененная строка
            });

            // Обработчик для кнопки "-"
            btnMinus.setOnClickListener(v -> {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    tvQuantity.setText(String.valueOf(quantity[0]));
                    detailPrice.setText(basePrice * quantity[0] + " Р"); // Измененная строка
                }
            });
        }
    }
}
