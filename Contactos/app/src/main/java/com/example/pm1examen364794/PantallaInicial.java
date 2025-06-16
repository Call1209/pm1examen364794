package com.example.pm1examen364794;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaInicial extends AppCompatActivity {

    private EditText txtNombre, txtTelefono, txtNota;
    private Spinner spinnerPais;
    Button btnGuardar, btnSalvados;
    FloatingActionButton btnCamara;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLConexion conexion = new SQLConexion(this);

        setContentView(R.layout.activity_inicial);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtNota = findViewById(R.id.txtNota);
        spinnerPais = findViewById(R.id.spinner);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSalvados = findViewById(R.id.btnSalvados);
        btnCamara = findViewById(R.id.btnCamara);
        imageView = findViewById(R.id.imageView);

        String[] paises = {"Honduras (504)", "Nicaragua (505)", "Costa Rica (506)", "Guatemala (502)", "El Salvador (503)", "Panama (507)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paises);
        spinnerPais.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString();
            String telefono = txtTelefono.getText().toString();
            String nota = txtNota.getText().toString();
            String pais = spinnerPais.getSelectedItem().toString();

            if (nombre.isEmpty() || telefono.isEmpty() || nota.isEmpty()) {
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                boolean exito = conexion.insertarContacto(pais, nombre, telefono, nota, null);
                if (exito) {
                    Toast.makeText(this, "Contacto guardado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar el contacto", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSalvados.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PantallaLista.class);
            startActivity(intent);
        });

        btnCamara.setOnClickListener(v -> {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
            } else {
                abrirCamara();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }



    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }


}

