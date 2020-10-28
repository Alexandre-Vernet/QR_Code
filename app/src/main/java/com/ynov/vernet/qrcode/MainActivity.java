package com.ynov.vernet.qrcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // A l'ouverture de l'app
        scanner();

        // Au clic du bouton
        Button btnScanner = findViewById(R.id.btnScanner);
        btnScanner.setOnClickListener(v -> scanner());
    }

    public void scanner() {
        try {
            // Ouvrir le lecteur de QR Code
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");       // "PRODUCT_MODE" pour les codes barres
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            // Installer l'application de lecteur de QR Code
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Récupérer le contenu du qr code
            String contents = data.getStringExtra("SCAN_RESULT");

            // Afficher une boite de dialogue pour ouvrir le lien
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Ouvrir le lien ?")
                    .setMessage(contents)
                    .setPositiveButton("Oui", (dialogInterface, i) -> {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(contents));
                        startActivity(intent);
                    })
                    .setNegativeButton("Non", (dialogInterface, i) -> {
                    })
                    .show();
        }
    }
}