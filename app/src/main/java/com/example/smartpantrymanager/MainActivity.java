package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private Button btnPantryTitle;

    private RecyclerView recyclerPantry;

    private TextView tvEmptyPantry;

    private LinearLayout pantryContent;

    private DatabaseHelper databaseHelper;

    private ArrayList<PantryItem> pantryItems =
            new ArrayList<>();

    private PantryAdapter pantryAdapter;

    private boolean pantryExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_main
        );


        // =====================================================
        // FIND VIEWS
        // =====================================================

        btnPantryTitle =
                findViewById(
                        R.id.btnPantryTitle
                );

        pantryContent =
                findViewById(
                        R.id.pantryContent
                );

        recyclerPantry =
                findViewById(
                        R.id.recyclerPantry
                );

        tvEmptyPantry =
                findViewById(
                        R.id.tvEmptyPantry
                );

        btnAddIngredient =
                findViewById(
                        R.id.btnAddIngredient
                );

        btnSuggestedRecipes =
                findViewById(
                        R.id.btnSuggestedRecipes
                );

        btnSettings =
                findViewById(
                        R.id.btnSettings
                );


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // RECYCLER VIEW
        // =====================================================

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // =====================================================
        // LOAD PANTRY
        // =====================================================

        loadIngredients();


        // =====================================================
        // PANTRY ADAPTER
        // =====================================================

        pantryAdapter =
                new PantryAdapter(
                        pantryItems,

                        new PantryAdapter.OnPantryItemClickListener() {

                            @Override
                            public void onEdit(
                                    PantryItem item) {

                                openEditIngredient(item);
                            }


                            @Override
                            public void onDelete(
                                    PantryItem item) {

                                showDeleteConfirmation(item);
                            }
                        }
                );


        recyclerPantry.setAdapter(
                pantryAdapter
        );


        // =====================================================
        // START WITH PANTRY CLOSED
        // =====================================================

        pantryExpanded = false;

        pantryContent.setVisibility(
                View.GONE
        );

        btnPantryTitle.setText(
                "MY PANTRY  ▼"
        );


        // =====================================================
        // PANTRY DROPDOWN
        // =====================================================

        btnPantryTitle.setOnClickListener(v -> {

            pantryExpanded =
                    !pantryExpanded;


            if (pantryExpanded) {

                pantryContent.setVisibility(
                        View.VISIBLE
                );

                btnPantryTitle.setText(
                        "MY PANTRY  ▲"
                );

                updatePantryDisplay();

            } else {

                pantryContent.setVisibility(
                        View.GONE
                );

                btnPantryTitle.setText(
                        "MY PANTRY  ▼"
                );
            }
        });


        // =====================================================
        // ADD INGREDIENT
        // =====================================================

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddIngredientActivity.class
                    );

            startActivity(intent);
        });


        // =====================================================
        // SUGGESTED RECIPES
        // =====================================================

        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });


        // =====================================================
        // SETTINGS
        // =====================================================

        btnSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });
    }


    // =========================================================
    // REFRESH WHEN RETURNING TO HOME
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();


        if (databaseHelper != null) {

            loadIngredients();


            if (pantryAdapter != null) {

                pantryAdapter.notifyDataSetChanged();
            }


            if (pantryExpanded) {

                updatePantryDisplay();
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
    // UPDATE PANTRY DISPLAY
    // =========================================================

    private void updatePantryDisplay() {

        if (pantryItems.isEmpty()) {

            recyclerPantry.setVisibility(
                    View.GONE
            );

            tvEmptyPantry.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerPantry.setVisibility(
                    View.VISIBLE
            );

            tvEmptyPantry.setVisibility(
                    View.GONE
            );
        }


        if (pantryAdapter != null) {

            pantryAdapter.notifyDataSetChanged();
        }
    }


    // =========================================================
    // OPEN EDIT INGREDIENT
    // =========================================================

    private void openEditIngredient(
            PantryItem item) {


        Intent intent =
                new Intent(
                        MainActivity.this,
                        AddIngredientActivity.class
                );


        // INGREDIENT ID

        intent.putExtra(
                "ingredient_id",
                item.getId()
        );


        // INGREDIENT NAME

        intent.putExtra(
                "ingredient_name",
                item.getName()
        );


        // QUANTITY

        intent.putExtra(
                "ingredient_quantity",
                item.getQuantity()
        );


        // UNIT

        intent.putExtra(
                "ingredient_unit",
                item.getUnit()
        );


        // EXPIRY DATE

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


                                updatePantryDisplay();


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
}