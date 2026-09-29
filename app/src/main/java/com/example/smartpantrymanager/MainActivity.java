package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnAddIngredient;
    private Button btnSuggestedRecipes;
    private Button btnSettings;

    private RecyclerView recyclerPantry;

    private DatabaseHelper databaseHelper;

    private ArrayList<PantryItem> pantryItems = new ArrayList<>();

    private PantryAdapter pantryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);


        // FIND VIEWS

        recyclerPantry =
                findViewById(R.id.recyclerPantry);

        btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnSuggestedRecipes =
                findViewById(R.id.btnSuggestedRecipes);

        btnSettings =
                findViewById(R.id.btnSettings);


        // DATABASE

        databaseHelper =
                new DatabaseHelper(this);


        // RECYCLER VIEW

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // LOAD PANTRY

        loadIngredients();


        // ADAPTER

        pantryAdapter =
                new PantryAdapter(
                        pantryItems,
                        new PantryAdapter.OnPantryItemClickListener() {

                            @Override
                            public void onEdit(PantryItem item) {

                                openEditIngredient(item);
                            }


                            @Override
                            public void onDelete(PantryItem item) {

                                showDeleteConfirmation(item);
                            }
                        }
                );


        recyclerPantry.setAdapter(
                pantryAdapter
        );


        // ADD INGREDIENT

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddIngredientActivity.class
                    );

            startActivity(intent);
        });


        // SUGGESTED RECIPES

        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });


        // SETTINGS

        btnSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            loadIngredients();

            if (pantryAdapter != null) {

                pantryAdapter.notifyDataSetChanged();
            }
        }
    }


    // =========================================================
    // LOAD INGREDIENTS
    // =========================================================

    private void loadIngredients() {

        pantryItems.clear();

        pantryItems.addAll(
                databaseHelper.getAllIngredients()
        );
    }


    // =========================================================
    // OPEN EDIT INGREDIENT SCREEN
    // =========================================================

    private void openEditIngredient(PantryItem item) {

        Intent intent =
                new Intent(
                        MainActivity.this,
                        AddIngredientActivity.class
                );


        // SEND INGREDIENT ID

        intent.putExtra(
                "ingredient_id",
                item.getId()
        );


        // SEND INGREDIENT NAME

        intent.putExtra(
                "ingredient_name",
                item.getName()
        );


        // SEND QUANTITY

        intent.putExtra(
                "ingredient_quantity",
                item.getQuantity()
        );


        // SEND UNIT

        intent.putExtra(
                "ingredient_unit",
                item.getUnit()
        );


        // SEND EXPIRY DATE

        intent.putExtra(
                "ingredient_expiry",
                item.getExpiryDate()
        );


        startActivity(intent);
    }


    // =========================================================
    // DELETE CONFIRMATION
    // =========================================================

    private void showDeleteConfirmation(
            PantryItem item) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete Ingredient"
                )

                .setMessage(
                        "Are you sure you want to delete " +
                                item.getName() +
                                "?"
                )

                .setNegativeButton(
                        "CANCEL",
                        null
                )

                .setPositiveButton(
                        "DELETE",
                        (dialog, which) -> {

                            boolean deleted =
                                    databaseHelper.deleteIngredient(
                                            item.getId()
                                    );


                            if (deleted) {

                                loadIngredients();

                                pantryAdapter.notifyDataSetChanged();


                                Toast.makeText(
                                        this,
                                        item.getName() +
                                                " deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Could not delete ingredient",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )

                .show();
    }


    // =========================================================
    // SUGGESTED RECIPES
    // =========================================================

    private void showSuggestedRecipes() {

        ArrayList<String> ingredients =
                new ArrayList<>();


        // GET ALL PANTRY INGREDIENTS

        for (PantryItem item : pantryItems) {

            ingredients.add(
                    item.getName()
            );
        }


        StringBuilder recipes =
                new StringBuilder();


        // =====================================================
        // CHICKEN BOWL
        // =====================================================

        if (ingredients.contains("Chicken") &&
                ingredients.contains("Rice") &&
                ingredients.contains("Avo") &&
                ingredients.contains("Feta Cheese")) {

            recipes.append(
                    "🍗 CHICKEN BOWL\n"
            );

            recipes.append(
                    "Chicken + Rice + Avo + Feta Cheese\n\n"
            );
        }


        // =====================================================
        // STEAK & CORN
        // =====================================================

        if (ingredients.contains("Steak") &&
                ingredients.contains("Corn")) {

            recipes.append(
                    "🥩 STEAK & CORN\n"
            );

            recipes.append(
                    "Steak + Corn\n\n"
            );
        }


        // =====================================================
        // BEEF BURGER
        // =====================================================

        if (ingredients.contains("Beef patties") &&
                ingredients.contains("Cheese") &&
                ingredients.contains("Hamburger rolls") &&
                ingredients.contains("Onion")) {

            recipes.append(
                    "🍔 BEEF BURGER\n"
            );

            recipes.append(
                    "Beef Patty + Cheese + Hamburger rolls + Onion\n\n"
            );
        }


        // =====================================================
        // CHEESY STEAK & PATATOES
        // =====================================================

        if (ingredients.contains("Steak") &&
                ingredients.contains("Patatoes") &&
                ingredients.contains("Cheese")) {

            recipes.append(
                    "🥔 CHEESY STEAK & PATATOES\n"
            );

            recipes.append(
                    "Steak + Patatoes + Cheese\n\n"
            );
        }


        // =====================================================
        // CHICKEN & FETA WRAP
        // =====================================================

        if (ingredients.contains("Chicken") &&
                ingredients.contains("Wraps") &&
                ingredients.contains("Feta Cheese")) {

            recipes.append(
                    "🌯 CHICKEN & FETA WRAP\n"
            );

            recipes.append(
                    "Chicken + Wraps + Feta Cheese\n\n"
            );
        }


        // =====================================================
        // BEEF & CHEESE WRAP
        // =====================================================

        if (ingredients.contains("Beef patties") &&
                ingredients.contains("Wraps") &&
                ingredients.contains("Cheese")) {

            recipes.append(
                    "🌯 BEEF & CHEESE WRAP\n"
            );

            recipes.append(
                    "Beef Patty + Wraps + Cheese\n\n"
            );
        }


        // =====================================================
        // CHICKEN AVO WRAP
        // =====================================================

        if (ingredients.contains("Chicken") &&
                ingredients.contains("Wraps") &&
                ingredients.contains("Avo")) {

            recipes.append(
                    "🥑 CHICKEN AVO WRAP\n"
            );

            recipes.append(
                    "Chicken + Wraps + Avo\n\n"
            );
        }


        // =====================================================
        // WRAPS
        // =====================================================

        if (ingredients.contains("Wraps")) {

            recipes.append(
                    "🌯 WRAPS\n"
            );

            recipes.append(
                    "Use your wraps with the available " +
                            "chicken, steak, beef, cheese, " +
                            "onion or avocado.\n\n"
            );
        }


        // =====================================================
        // NO RECIPES
        // =====================================================

        if (recipes.length() == 0) {

            recipes.append(
                    "No recipes available yet.\n\n"
            );

            recipes.append(
                    "Add more ingredients to your pantry!"
            );
        }


        // =====================================================
        // DISPLAY RECIPES
        // =====================================================

        new AlertDialog.Builder(this)

                .setTitle(
                        "Suggested Recipes"
                )

                .setMessage(
                        recipes.toString()
                )

                .setPositiveButton(
                        "OK",
                        null
                )

                .show();
    }
}