package com.example.neumi.sqliteannuaire;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    EditText etTel;
    EditText etId;
    EditText etName;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbInit dbInit = new DbInit(this);
        db = dbInit.getWritableDatabase();
        etSearch = findViewById(R.id.etSearch);
        etTel = findViewById(R.id.etTel);
        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);

    }

    public void save(View view) {
        if (etId.getText().toString().isEmpty()) {
            insert();
        } else {
            update();
        }

    }

    private void update() {
        ContentValues values = new ContentValues();
        values.put("name", etName.getText().toString());
        values.put("tel", etTel.getText().toString());
        int id = Integer.parseInt(etId.getText().toString());
        String clause = "id = '" + id + "'";
        db.update("contacts", values, clause, null);
    }

    private void insert() {
        ContentValues values = new ContentValues();
        values.put("name", etName.getText().toString());
        values.put("tel", etTel.getText().toString());
        db.insert("contacts", "", values);
    }

    public void delete(View view) {
        int id = Integer.parseInt(etId.getText().toString());
        String clause = "id = '" + id + "'";
        db.delete("contacts", clause, null);
        effacer();
    }

    public void clear(View view) {
        effacer();
    }

    private void effacer() {
        etSearch.getText().clear();
        etId.getText().clear();
        etName.getText().clear();
        etTel.getText().clear();
    }

    public void search(View view) {
        String[] colonnes = {"id", "name", "tel"};
        String nom = etSearch.getText().toString().toUpperCase();
        nom = nom.replace("'", "''");
        String critere = "UPPER(name) = '" + nom + "'";
        Cursor cursor = db.query("contacts", colonnes, critere, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            etId.setText(cursor.getString(0));
            etName.setText(cursor.getString(1));
            etTel.setText(cursor.getString(2));
        } else {
            Toast.makeText(this, "Nom introuvable", Toast.LENGTH_LONG).show();
        }

    }
}
