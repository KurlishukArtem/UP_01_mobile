package com.example.wsr_pool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static boolean isConnectedToNetwork(Context context)
    {
        // получаем ссылку на системный сервис
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        // определяем, доступно ли подключение к Интернету или нет
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
