package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.xd.shenxinhelp.R;

public class Huyan extends AppCompatActivity {

    private Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huyan);
        aSwitch = (Switch) findViewById(R.id.huyan_switch);
        SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);

        String huyan_on = sp.getString("huyan_on", "1");
        if (huyan_on.equals("1")) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sp = getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (isChecked) {
                    editor.putString("huyan_on", "1");
                    editor.commit();
                } else {
                    editor.putString("huyan_on", "0");
                    editor.commit();
                }
            }
        });
    }
}
