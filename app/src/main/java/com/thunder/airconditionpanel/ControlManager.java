package com.thunder.airconditionpanel;

import android.os.AsyncTask;
import android.util.Log;

import com.beyond.jni.SerialPort;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by CHENQIAO on 2017/4/28 16:52.
 * E-mail: mrjctech@gmail.com
 */

public class ControlManager {

    private static ControlManager mControlManager = null;

    private String TAG = "ControlManager";
    private SerialPort serialPort;
    private static int commNum;
    private static int baudrate;


    private ControlManager() {
    }

    public static ControlManager getInstance() {

        if (mControlManager == null) {
            mControlManager = new ControlManager();
        }

        return mControlManager;
    }

    public boolean init(int commNum, int baudrate) {

        this.commNum = commNum;
        this.baudrate = baudrate;

        try {
            serialPort = new SerialPort("/dev/ttyUSB" + commNum, baudrate, 8, 'N', 1, 0);

            if (serialPort != null) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 开机
     */
    public void turnOn() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x01};
        sendData(bytes);
    }

    /**
     * 关机
     */
    public void powerOff() {

        byte[] bytes = {(byte) 0xa5, 0x5e, 0x02};
        sendData(bytes);
    }

    /**
     * 送风
     */
    public void airSupply() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x03};
        sendData(bytes);
    }

    /**
     * 制冷
     */
    public void refrigerat() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x04};
        sendData(bytes);
    }

    /**
     * 高风
     */
    public void high() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x05};
        sendData(bytes);
    }

    /**
     * 中风
     */
    public void medium() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x06};
        sendData(bytes);
    }

    /**
     * 低风
     */
    public void low() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x07};
        sendData(bytes);
    }


    /**
     * 状态读取
     */
    public void readStatus() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x08};
//        sendData(bytes);


        new ControlTask(new AirconditionCallable(bytes)) {

            @Override
            protected void onPostExecute(byte[] data) {
                String temp = "";
                for (int i = 0; i < data.length; i++) {
                    temp += Integer.toHexString(data[i]) + " ";

                }
                Log.i("readStatus", temp);

            }
        }.execute();


    }

    /**
     * 软件版本
     */
    public void softwareVersion() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x09};
        sendData(bytes);
    }

    /**
     * 设置温度
     *
     * @param i 18-30
     */
    public void setTemper(int i) {

        byte[] bytes = null;

        switch (i) {

            case 18:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x12};
                break;
            case 19:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x13};
                break;
            case 20:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x14};
                break;
            case 21:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x15};
                break;
            case 22:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x16};
                break;
            case 23:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x17};
                break;
            case 24:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x18};
                break;
            case 25:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x19};
                break;
            case 26:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x1a};
                break;
            case 27:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x1b};
                break;
            case 28:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x1c};
                break;
            case 29:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x1d};
                break;
            case 30:
                bytes = new byte[]{(byte) 0xa5, 0x5d, 0x1e};
                break;
            default:
                break;
        }

        if (bytes != null)
            sendData(bytes);
    }

    /**
     * 保存状态
     */
    public void saveStatus() {
        byte[] bytes = {(byte) 0xa5, 0x5e, 0x0a};
        sendData(bytes);
    }


    protected void sendData(byte[] data) {
        try {
            String temp = "";
            for (int i = 0; i < data.length; i++) {
                temp += Integer.toHexString(data[i]) + " ";
            }
            Log.i(TAG, temp.toUpperCase());
            serialPort.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int readData(byte[] data) {
        try {
            return serialPort.read(data);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    private class AirconditionCallable implements Callable<byte[]> {


        private byte[] bytes;

        public AirconditionCallable(byte[] bytes) {

            this.bytes = bytes;
        }


        @Override
        public byte[] call() throws Exception {

            sendData(bytes);

            byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            int len = readData(data);
            if (len > 0) {
                return data;
            }

            return data;
        }
    }


    class ControlTask extends AsyncTask<String, Void, byte[]> {

        AirconditionCallable callable;


        public ControlTask(AirconditionCallable callable) {
            this.callable = callable;
        }

        @Override
        protected byte[] doInBackground(String... s) {

            ExecutorService executor = Executors.newCachedThreadPool();
            Future<byte[]> future = executor.submit(callable);
            byte[] data = null;
            try {
                data = future.get(5000, TimeUnit.MILLISECONDS);
            } catch (TimeoutException te) {
                executor.shutdownNow();
                te.printStackTrace();
                Log.i(TAG, "executor shutdownNow()");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }
    }

    public void close() {
        serialPort.close();
    }

}
