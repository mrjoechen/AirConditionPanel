/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.beyond.jni;

import android.util.Log;

import com.thunder.ktv.thunderjni.thunderapi.TDMainConfig;
import com.thunder.ktv.thunderjni.thunderapi.TDPermissionHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    public static boolean isWrite = true;

    public SerialPort(String path, int baudrate, int nBits, char nEvent, int nStop, int flags) throws SecurityException, IOException {
        File device = new File(path);
//        /* Check access permission */
//        if (!device.canRead() && !device.canWrite()) {
//            try {
//                if (!device.canRead() && !device.canWrite()) {
//                    String cmd = "chmod -R 777 " + device.getAbsolutePath();
//                    execCommand(cmd);
//                    Log.e(TAG, "canRead............................");
//                }
//
//
//				/* Missing read/write permission, trying to chmod the file */
////				Process su = Runtime.getRuntime().exec("/system/bin/su");
////				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
////						+ "exit\n";
////				su.getOutputStream().write(cmd.getBytes());
////				if ((su.waitFor() != 0) || !device.canRead()
////						|| !device.canWrite()) {
////					throw new SecurityException();
////				}
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        mFd = open(device.getAbsolutePath(), baudrate, nBits, nEvent, nStop, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public int read(byte[] buffer) throws IOException {
        try {
            if (mFileInputStream.available() > 0) {
                return mFileInputStream.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void write(byte[] buffer) throws IOException {
        if (isWrite) {
            mFileOutputStream.write(buffer);
            mFileOutputStream.flush();
        }
    }

    public void write(byte buffer) throws IOException {
        if (isWrite) {
            mFileOutputStream.write(buffer);
            mFileOutputStream.flush();
        }
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int nBits, char nEvent, int nStop, int flags);

    public native void close();

    static {
        System.loadLibrary("serial_port");
    }

    public void closePort() {
        if (mFileInputStream != null) {
            try {
                mFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mFileOutputStream != null) {
            try {
                mFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void execCommand(String command) throws IOException {
//        Runtime runtime = Runtime.getRuntime();
//        Process proc = runtime.exec(command);
//        try {
//            if (proc.waitFor() != 0) {
//                Log.i("SerialPort", "exit value = " + proc.exitValue());
//            }
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    proc.getInputStream()));
//            StringBuffer stringBuffer = new StringBuffer();
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                stringBuffer.append(line + "-\n");
//            }
//            Log.i("SerialPort", stringBuffer.toString());
//
//        } catch (InterruptedException e) {
//            System.err.println(e);
//        }


        TDMainConfig.getInstance();
        try {
            boolean result = TDPermissionHelper.execShellWithoutResponse(command);
            Log.e("chmodDriver", "result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("chmodDriver", e.toString());
        }

    }
}
