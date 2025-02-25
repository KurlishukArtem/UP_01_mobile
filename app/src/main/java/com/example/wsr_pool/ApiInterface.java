package com.example.wsr_pool;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("Categorys.php")  // Убедитесь, что путь соответствует вашему серверу
    Call<List<Categorys>> getCategories();  // Получение списка категорий

    @GET("Dishes.php")  // Запрос для получения всех блюд
    Call<List<Dishes>> getDishes();

    @GET("Dishes.php")  // Запрос для получения блюд по категории
    Call<List<Dishes>> getDishesByCategory(@Query("category_id") int categoryId);

    @FormUrlEncoded
    @POST("Login.php")
    Call<AuthResponse> login(@Field("login") String login,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("Regin.php")
    Call<ResponseBody> registerUser(
            @Field("login") String login,
            @Field("password") String password,
            @Field("name") String name,
            @Field("phone") String phone
    );
}


