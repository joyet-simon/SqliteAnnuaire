package com.example.neumi.sqliteannuaire;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity implements AlertDialog.OnClickListener {

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
        String clause = "id = " + etId.getText().toString();
        db.update("contacts", values, clause, null);
    }

    private void insert() {
        ContentValues values = new ContentValues();
        values.put("name", etName.getText().toString());
        values.put("tel", etTel.getText().toString());
        db.insert("contacts", "", values);
    }

    public void delete(View view) {
        createAndShowDialog();
    }

    public void clear(View view) {
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

    private void createAndShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suppression");
        builder.setMessage("Confirmez vous la suppression?");
        builder.setPositiveButton("Oui", this);
        builder.setNegativeButton("Non", this);
        builder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            case BUTTON_POSITIVE:
                String clause = "id = " + etId.getText().toString();
                db.delete("contacts", clause, null);
                clear(null);
                Toast.makeText(this, "suppression", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                break;
        }
    }
}
