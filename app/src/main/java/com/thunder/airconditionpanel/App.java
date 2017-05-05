package com.thunder.airconditionpanel;

import android.app.Application;
import android.util.Log;

import com.thunder.ktv.thunderjni.thunderapi.TDMainConfig;
import com.thunder.ktv.thunderjni.thunderapi.TDPermissionHelper;

import java.io.File;

/**
 * Created by CHENQIAO on 2017/5/2 15:44.
 * E-mail: mrjctech@gmail.com
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        File device = new File("/dev/ttyUSB0");
        /* Check access permission */
//        if (!device.canRead() && !device.canWrite()) {
            try {
//                if (!device.canRead() && !device.canWrite()) {
                    String cmd = "chmod -R 777 /dev/ttyUSB*";
                    TDMainConfig.getInstance();
                    try {
                        boolean result = TDPermissionHelper.execShellWithoutResponse(cmd);
                        Log.e("chmodDriver", "result:" + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("chmodDriver", e.toString());
                    }
//                }


				/* Missing read/write permission, trying to chmod the file */
//				Process su = Runtime.getRuntime().exec("/system/bin/su");
//				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
//						+ "exit\n";
//				su.getOutputStream().write(cmd.getBytes());
//				if ((su.waitFor() != 0) || !device.canRead()
//						|| !device.canWrite()) {
//					throw new SecurityException();
//				}
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }
}
