package com.thunder.airconditionpanel;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by CHENQIAO on 2017/4/28 17:04.
 * E-mail: mrjctech@gmail.com
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_status;
    private Button close;
    private Button open;
    private ControlManager controlManager;
    private Button airsupply;
    private Button refrigerat;
    private Button high;
    private Button medium;
    private Button low;
    private Button save;
    private Button read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_status = (TextView) findViewById(R.id.tv_status);
        close = (Button) findViewById(R.id.close);
        open = (Button) findViewById(R.id.open);
        airsupply = (Button) findViewById(R.id.airsupply);
        refrigerat = (Button) findViewById(R.id.refrigerat);
        high = (Button) findViewById(R.id.high);
        medium = (Button) findViewById(R.id.medium);
        low = (Button) findViewById(R.id.low);
        save = (Button) findViewById(R.id.save);
        read = (Button) findViewById(R.id.read);

        open.setOnClickListener(this);
        close.setOnClickListener(this);
        airsupply.setOnClickListener(this);
        refrigerat.setOnClickListener(this);
        high.setOnClickListener(this);
        medium.setOnClickListener(this);
        low.setOnClickListener(this);
        save.setOnClickListener(this);
        read.setOnClickListener(this);



        controlManager = ControlManager.getInstance();

        boolean init = controlManager.init(0, 9600);
        if (init){
            tv_status.setText("初始化成功");
        }else {
            tv_status.setText("初始化失败，请重新打开！");
        }


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.open:
                controlManager.turnOn();
                break;
            case R.id.close:
                controlManager.powerOff();
                break;
            case R.id.airsupply:
                controlManager.airSupply();
                break;
            case R.id.refrigerat:
                controlManager.refrigerat();
                break;
            case R.id.high:
                controlManager.high();
                break;
            case R.id.medium:
                controlManager.medium();
                break;
            case R.id.low:
                controlManager.low();
                break;
            case R.id.save:
                controlManager.saveStatus();
                break;
            case R.id.read:
                controlManager.readStatus();
                break;
            default:
                break;
        }

    }
}
