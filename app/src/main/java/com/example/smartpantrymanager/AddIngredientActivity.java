package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;

    private Spinner spinnerUnit;

    private Button btnSaveIngredient;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    private int editIngredientId = -1;

    private final String[] units = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        // --------------------------------------------------
        // CONNECT XML VIEWS
        // --------------------------------------------------

        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        spinnerUnit = findViewById(R.id.spinnerUnit);

        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);
        btnCancel = findViewById(R.id.btnCancel);

        databaseHelper = new DatabaseHelper(this);

        // --------------------------------------------------
        // UNIT SPINNER
        // --------------------------------------------------

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

        // --------------------------------------------------
        // EXPIRY DATE PICKER
        // --------------------------------------------------

        etExpiryDate.setOnClickListener(v -> showDatePicker());

        // --------------------------------------------------
        // CHECK IF EDITING AN EXISTING INGREDIENT
        // --------------------------------------------------

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
                            0
                    );

            String unit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String expiryDate =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );

            // Fill the form with existing values

            if (name != null) {
                etIngredientName.setText(name);
            }

            if (quantity > 0) {
                etQuantity.setText(
                        String.valueOf(quantity)
                );
            }

            if (expiryDate != null) {
                etExpiryDate.setText(expiryDate);
            }

            // Select existing unit

            if (unit != null) {

                for (int i = 0; i < units.length; i++) {

                    if (units[i].equalsIgnoreCase(unit)) {

                        spinnerUnit.setSelection(i);
                        break;
                    }
                }
            }

            btnSaveIngredient.setText(
                    "UPDATE INGREDIENT"
            );
        }

        // --------------------------------------------------
        // SAVE / UPDATE BUTTON
        // --------------------------------------------------

        btnSaveIngredient.setOnClickListener(v ->
                saveIngredient()
        );

        // --------------------------------------------------
        // CANCEL BUTTON
        // --------------------------------------------------

        btnCancel.setOnClickListener(v ->
                finish()
        );
    }

    // ======================================================
    // SAVE OR UPDATE INGREDIENT
    // ======================================================

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

        String expiryDate =
                etExpiryDate
                        .getText()
                        .toString()
                        .trim();

        String unit =
                spinnerUnit
                        .getSelectedItem()
                        .toString();

        // --------------------------------------------------
        // VALIDATE NAME
        // --------------------------------------------------

        if (TextUtils.isEmpty(name)) {

            etIngredientName.setError(
                    "Please enter an ingredient name"
            );

            etIngredientName.requestFocus();

            return;
        }

        if (name.length() > 50) {

            etIngredientName.setError(
                    "Ingredient name must be 50 characters or less"
            );

            etIngredientName.requestFocus();

            return;
        }

        // --------------------------------------------------
        // VALIDATE QUANTITY
        // --------------------------------------------------

        if (TextUtils.isEmpty(quantityText)) {

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

        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than 0"
            );

            etQuantity.requestFocus();

            return;
        }

        if (quantity > 100000) {

            etQuantity.setError(
                    "Quantity is too large"
            );

            etQuantity.requestFocus();

            return;
        }

        // --------------------------------------------------
        // VALIDATE UNIT
        // --------------------------------------------------

        if (TextUtils.isEmpty(unit)) {

            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // --------------------------------------------------
        // UPDATE EXISTING INGREDIENT
        // --------------------------------------------------

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
                        name + " updated successfully!",
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

        // --------------------------------------------------
        // ADD NEW INGREDIENT
        // --------------------------------------------------

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

    // ======================================================
    // DATE PICKER
    // ======================================================

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {

                            String formattedDate =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            selectedDay,
                                            selectedMonth + 1,
                                            selectedYear
                                    );

                            etExpiryDate.setText(
                                    formattedDate
                            );
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }
}