package com.stone.mmt.app.notelite.library;

import android.content.Context;
import android.widget.Toast;

public class ToastMsg {
    public static void toastMsg(Context context,String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
