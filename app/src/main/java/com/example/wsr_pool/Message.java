package com.example.wsr_pool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Message {
    public void AlertDialog(String title, String message, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
