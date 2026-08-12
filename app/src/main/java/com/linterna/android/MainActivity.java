package com.linterna.android;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {

    private CameraManager cameraManager;
    private String cameraId;
    private TextView estado;
    private boolean linternaEncendida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        estado = findViewById(R.id.estado);

        Button encender = findViewById(R.id.encender);
        Button apagar = findViewById(R.id.apagar);

        cameraManager =
                (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            estado.setText("No se encontró la cámara");
            return;
        }

        encender.setOnClickListener(v -> cambiarLinterna(true));

        apagar.setOnClickListener(v -> cambiarLinterna(false));
    }

    private void cambiarLinterna(boolean encender) {
        try {
            cameraManager.setTorchMode(cameraId, encender);

            linternaEncendida = encender;

            if (encender) {
                estado.setText("🔦 Linterna ENCENDIDA");
                estado.setTextColor(Color.GREEN);
            } else {
                estado.setText("💡 Linterna APAGADA");
                estado.setTextColor(Color.WHITE);
            }

        } catch (CameraAccessException e) {
            estado.setText("No se pudo controlar la linterna");
        }
    }

    @Override
    protected void onDestroy() {
        if (linternaEncendida) {
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException ignored) {
            }
        }

        super.onDestroy();
    }
}
