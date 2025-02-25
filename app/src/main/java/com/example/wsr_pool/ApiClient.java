package com.example.wsr_pool;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.0.7/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Увеличиваем время ожидания подключения, чтения и записи
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Время ожидания подключения
                    .readTimeout(30, TimeUnit.SECONDS)    // Время ожидания ответа
                    .writeTimeout(30, TimeUnit.SECONDS);  // Время записи
                    httpClient.connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)); // Логирование HTTP-запросов

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
