package com.thunder.airconditionpanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by CHENQIAO on 2017/5/4 13:22.
 * E-mail: mrjctech@gmail.com
 */

public class USBBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.hardware.usb.action.USB_STATE")) {
            if (intent.getExtras().getBoolean("connected")) {
                // usb 插入
                Log.i("chenqiao", intent.getAction()+"插入");
                Toast.makeText(context, "插入", Toast.LENGTH_LONG).show();
            } else {
                Log.i("chenqiao", intent.getAction()+"拔出");
                //   usb 拔出
                Toast.makeText(context, "拔出", Toast.LENGTH_LONG).show();
            }
        }
    }
}
