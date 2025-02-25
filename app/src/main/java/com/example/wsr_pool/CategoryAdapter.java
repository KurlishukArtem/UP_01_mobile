package com.example.wsr_pool;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Categorys> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private String selectedCategory;


    public interface OnCategoryClickListener {
        void onCategoryClick(Categorys category);
    }

    public CategoryAdapter(List<Categorys> categoryList, OnCategoryClickListener onCategoryClickListener) {
        this.categoryList = categoryList;
        this.onCategoryClickListener = onCategoryClickListener;
        this.selectedCategory = null;  // Начальное состояние
    }
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
        notifyDataSetChanged();  // Перерисовать все элементы при изменении
    }

    public void updateData(List<Categorys> newCategories) {
        this.categoryList.clear();
        this.categoryList.addAll(newCategories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categorys category = categoryList.get(position);
        holder.categoryText.setText(category.getName());

        // Установка цвета в зависимости от выбора
        if (category.getName().equals(selectedCategory)) {
            holder.categoryText.setTextColor(Color.parseColor("#FA4A0C"));
            holder.categoryLine.setBackgroundColor(Color.parseColor("#FA4A0C"));
        } else {
            holder.categoryText.setTextColor(Color.BLACK);
            holder.categoryLine.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> onCategoryClickListener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        View categoryLine;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.category_text);
            categoryLine = itemView.findViewById(R.id.category_line);
        }
    }
}
