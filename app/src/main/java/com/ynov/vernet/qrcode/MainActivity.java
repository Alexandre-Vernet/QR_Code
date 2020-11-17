package com.ynov.vernet.qrcode;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Demander la permission CAMERA
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Démarrer le scanner
            Intent intent = new Intent(getApplicationContext(), Scanner.class);
            startActivity(intent);
            finish();
        } else {
            // Afficher une boîte de dialogue
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Erreur")
                    .setMessage("L'application a besoin d'une ou plusieurs permissions")
                    .setPositiveButton("Autoriser", (dialogInterface, i) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0))
                    .setNegativeButton("Quitter", (dialogInterface, i) -> finish())
                    .show();
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }

}
