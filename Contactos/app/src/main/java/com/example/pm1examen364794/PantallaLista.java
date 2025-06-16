package com.example.pm1examen364794;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PantallaLista extends AppCompatActivity {

    private EditText searchBar;
    private ListView contactList;
    private Button buttonBack, buttonDelete;
    private SQLConexion dbHelper;
    private ArrayList<String> contactArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        // Inicialización de componentes
        searchBar = findViewById(R.id.search_bar);
        contactList = findViewById(R.id.contact_list);
        buttonBack = findViewById(R.id.button_back);
        // 1. Inicializar la base y la lista
        dbHelper = new SQLConexion(this);
        contactArray = new ArrayList<>();

// 2. Llenar la lista desde SQLite
        cargarContactos(); // primero llena contactArray

// 3. Crear adaptador con los datos
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactArray);
        contactList.setAdapter(adapter);


        // Filtro de búsqueda en tiempo real
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Manejo de selección en la lista
        contactList.setOnItemClickListener((parent, view, position, id) -> {
            String contactoSeleccionado = contactArray.get(position);
            mostrarDialogoAccion(contactoSeleccionado);
        });

        // Botón "Atrás" corregido
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaLista.this, PantallaInicial.class);
            startActivity(intent);
            finish();
        });
    }

    private void cargarContactos() {
        contactArray.clear(); // Limpiar la lista antes de cargar nuevos datos
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT nombre, telefono FROM contacts", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(0);
                    String telefono = cursor.getString(1);
                    contactArray.add(nombre + " | " + telefono);
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "No hay contactos guardados.", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Error al acceder a la base de datos.", Toast.LENGTH_SHORT).show();
        }


    }

    private void mostrarDialogoAccion(String contacto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acción")
                .setMessage("¿Qué desea hacer con " + contacto + "?")
                .setPositiveButton("Llamar", (dialog, which) -> realizarLlamada(contacto))
                .setNegativeButton("Eliminar", (dialog, which) -> eliminarContacto(contacto))
                .setNeutralButton("Cancelar", null)
                .show();
    }

    private void realizarLlamada(String contacto) {
        String telefono = contacto.split(" \\| ")[1]; // Extraer número de teléfono
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono));

        if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 1);
        }
    }

    private void eliminarContacto(String contacto) {
        String telefono = contacto.split(" \\| ")[1];
        dbHelper.getWritableDatabase().delete("contacts", "telefono=?", new String[]{telefono});
        Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
        cargarContactos(); // Recargar lista después de eliminar
    }

}