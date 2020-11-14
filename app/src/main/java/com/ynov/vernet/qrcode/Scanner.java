package com.ynov.vernet.qrcode;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Référence
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, scannView);

        // Lorsque le Scanner à détecter un QR Code
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {

            // Détecter s'il s'agit d'un lien
            final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
            Pattern p = Pattern.compile(URL_REGEX);
            Matcher m = p.matcher(result.getText());

            // S'il s'agit d'un lien
            if (m.find()) {
                // Afficher une boîte de dialogue
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Ouvrir le lien ?")
                        .setMessage(result.getText())
                        .setPositiveButton("Oui", (dialogInterface, i) -> {
                            // Ouvrir le lien
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
                            startActivity(browserIntent);
                        })
                        .setNegativeButton("Non", (dialogInterface, i) -> {
                            // Revenir au scanner
                            requestForCamera();
                        })
                        .show();
            }

            // S'il s'agit d'autre chose qu'un lien
            else {
                // Afficher une boîte de dialogue
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Résultat")
                        .setMessage(result.getText())
                        .setPositiveButton("Ok", (dialogInterface, i) -> requestForCamera())
                        .show();
            }
        }));

        // Au clic sur le scanner
        scannView.setOnClickListener(v -> codeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            //  Permission Camera refusée
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Scanner.this, "L'application n'a pas accès à la caméra", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
}
