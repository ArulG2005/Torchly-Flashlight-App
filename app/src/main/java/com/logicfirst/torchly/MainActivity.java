package com.logicfirst.torchly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
ConstraintLayout layout;
CameraManager cm;
String id;
TextView batteryPercentageTextView;
boolean state=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        batteryPercentageTextView = findViewById(R.id.batteryPercentage);
        layout=findViewById(R.id.main);
        IntentFilter batteryStatusIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, batteryStatusIntentFilter);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state==false){
                    try {
                cm= (CameraManager) getSystemService(CAMERA_SERVICE);
                id=cm.getCameraIdList()[0];
                cm.setTorchMode(id,!state);
                layout.setBackgroundResource(R.drawable.on);
                state=true;
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    try {
                        cm= (CameraManager) getSystemService(CAMERA_SERVICE);
                        id=cm.getCameraIdList()[0];
                        cm.setTorchMode(id,!state);
                        layout.setBackgroundResource(R.drawable.off);
                        state=false;
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;

            // Animation to update battery percentage
            batteryPercentageTextView.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                batteryPercentageTextView.setText((int) batteryPct + "%");
                batteryPercentageTextView.animate().alpha(1f).setDuration(500).start();
            }).start();
        }
    };
}