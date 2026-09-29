package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btnClearPantry;
    private Button btnBackToPantry;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        databaseHelper = new DatabaseHelper(this);

        btnClearPantry = findViewById(R.id.btnClearPantry);
        btnBackToPantry = findViewById(R.id.btnBackToPantry);

        btnClearPantry.setOnClickListener(v ->
                showClearPantryConfirmation()
        );

        btnBackToPantry.setOnClickListener(v ->
                finish()
        );
    }

    private void showClearPantryConfirmation() {

        new AlertDialog.Builder(this)
                .setTitle("Clear Pantry?")
                .setMessage(
                        "Are you sure you want to delete all ingredients from your pantry?"
                )
                .setNegativeButton(
                        "CANCEL",
                        null
                )
                .setPositiveButton(
                        "CLEAR",
                        (dialog, which) -> clearPantry()
                )
                .show();
    }

    private void clearPantry() {

        databaseHelper.clearAllIngredients();

        Toast.makeText(
                this,
                "Pantry cleared successfully!",
                Toast.LENGTH_SHORT
        ).show();
    }
}