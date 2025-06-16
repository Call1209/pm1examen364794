package com.example.pm1examen364794;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PantallaLlamadas extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;

    private EditText txtTelefono;
    private ImageButton btnLlamar;
    private Button btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamadas);

        txtTelefono = findViewById(R.id.txtTelefono);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnAtras = findViewById(R.id.btnAtras);

        btnLlamar.setOnClickListener(v -> {
            String numero = txtTelefono.getText().toString().trim();
            if (numero.isEmpty()) {
                Toast.makeText(this, "Ingrese un número", Toast.LENGTH_SHORT).show();
            } else {
                mostrarDialogoConfirmacion(numero);
            }
        });

        btnAtras.setOnClickListener(v -> finish());
    }

    private void mostrarDialogoConfirmacion(String numero) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar llamada")
                .setMessage("¿Desea llamar al número: " + numero + "?")
                .setPositiveButton("Sí", (dialog, which) -> realizarLlamada(numero))
                .setNegativeButton("No", null)
                .show();
    }

    private void realizarLlamada(String numero) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + numero));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PERMISSION);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String numero = txtTelefono.getText().toString().trim();
                realizarLlamada(numero);
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
