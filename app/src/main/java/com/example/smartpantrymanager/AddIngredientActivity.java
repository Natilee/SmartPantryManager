package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;

    private Spinner spinnerUnit;

    private Button btnSaveIngredient;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    // Used when editing an existing ingredient
    private int editIngredientId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_ingredient);


        // FIND VIEWS

        etIngredientName =
                findViewById(R.id.etIngredientName);

        etQuantity =
                findViewById(R.id.etQuantity);

        etExpiryDate =
                findViewById(R.id.etExpiryDate);

        spinnerUnit =
                findViewById(R.id.spinnerUnit);

        btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        btnCancel =
                findViewById(R.id.btnCancel);


        // DATABASE

        databaseHelper =
                new DatabaseHelper(this);


        // UNIT OPTIONS

        String[] units = {
                "pieces",
                "kg",
                "g",
                "litres",
                "ml",
                "packets",
                "cans",
                "cups",
                "items"
        };


        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );


        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerUnit.setAdapter(unitAdapter);


        // CHECK IF WE ARE EDITING

        if (getIntent().hasExtra("ingredient_id")) {

            editIngredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String name =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double quantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            1
                    );

            String unit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String expiryDate =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );


            // PUT EXISTING DATA INTO FORM

            etIngredientName.setText(name);

            etQuantity.setText(
                    String.valueOf(quantity)
            );

            etExpiryDate.setText(
                    expiryDate
            );


            // SELECT EXISTING UNIT

            if (unit != null) {

                for (int i = 0; i < units.length; i++) {

                    if (units[i].equals(unit)) {

                        spinnerUnit.setSelection(i);

                        break;
                    }
                }
            }


            // CHANGE BUTTON TEXT

            btnSaveIngredient.setText(
                    "UPDATE INGREDIENT"
            );
        }


        // SAVE / UPDATE BUTTON

        btnSaveIngredient.setOnClickListener(v ->
                saveIngredient()
        );


        // CANCEL BUTTON

        btnCancel.setOnClickListener(v ->
                finish()
        );
    }


    private void saveIngredient() {

        String name =
                etIngredientName
                        .getText()
                        .toString()
                        .trim();


        String quantityText =
                etQuantity
                        .getText()
                        .toString()
                        .trim();


        String unit =
                spinnerUnit
                        .getSelectedItem()
                        .toString();


        String expiryDate =
                etExpiryDate
                        .getText()
                        .toString()
                        .trim();


        // VALIDATE NAME

        if (name.isEmpty()) {

            etIngredientName.setError(
                    "Please enter an ingredient"
            );

            etIngredientName.requestFocus();

            return;
        }


        // VALIDATE QUANTITY

        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Please enter a quantity"
            );

            etQuantity.requestFocus();

            return;
        }


        double quantity;

        try {

            quantity =
                    Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Please enter a valid number"
            );

            etQuantity.requestFocus();

            return;
        }


        // QUANTITY MUST BE GREATER THAN ZERO

        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than 0"
            );

            etQuantity.requestFocus();

            return;
        }


        // UPDATE EXISTING INGREDIENT

        if (editIngredientId != -1) {

            boolean result =
                    databaseHelper.updateIngredient(
                            editIngredientId,
                            name,
                            quantity,
                            unit,
                            expiryDate
                    );


            if (result) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully!",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Could not update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }


            return;
        }


        // ADD NEW INGREDIENT

        long result =
                databaseHelper.addIngredient(
                        name,
                        quantity,
                        unit,
                        expiryDate
                );


        if (result != -1) {

            Toast.makeText(
                    this,
                    "Ingredient added successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not add ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}