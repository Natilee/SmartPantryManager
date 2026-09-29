package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipes;
    private TextView tvNoRecipes;
    private Button btnBack;

    private DatabaseHelper databaseHelper;

    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private ArrayList<Recipe> matchingRecipes = new ArrayList<>();

    private RecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);


        // =====================================================
        // FIND VIEWS
        // =====================================================

        recyclerRecipes =
                findViewById(R.id.recyclerRecipes);

        tvNoRecipes =
                findViewById(R.id.tvNoRecipes);

        btnBack =
                findViewById(R.id.btnBack);


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // RECYCLER VIEW
        // =====================================================

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // =====================================================
        // ADAPTER
        // =====================================================

        recipeAdapter =
                new RecipeAdapter(
                        matchingRecipes,
                        recipe -> {

                            Intent intent =
                                    new Intent(
                                            SuggestedRecipesActivity.this,
                                            RecipeDetailActivity.class
                                    );

                            intent.putExtra(
                                    "recipe_id",
                                    recipe.getId()
                            );

                            intent.putExtra(
                                    "recipe_name",
                                    recipe.getName()
                            );

                            intent.putExtra(
                                    "recipe_ingredients",
                                    recipe.getIngredients()
                            );

                            intent.putExtra(
                                    "recipe_method",
                                    recipe.getMethod()
                            );

                            startActivity(intent);
                        }
                );


        recyclerRecipes.setAdapter(
                recipeAdapter
        );


        // =====================================================
        // BACK BUTTON
        // =====================================================

        btnBack.setOnClickListener(v ->
                finish()
        );


        // =====================================================
        // LOAD MATCHING RECIPES
        // =====================================================

        findMatchingRecipes();
    }


    // =========================================================
    // REFRESH WHEN RETURNING TO THIS SCREEN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                recipeAdapter != null) {

            findMatchingRecipes();
        }
    }


    // =========================================================
    // FIND MATCHING RECIPES
    // =========================================================

    private void findMatchingRecipes() {

        // Get every recipe from the database

        allRecipes =
                databaseHelper.getAllRecipes();


        // Get everything currently in the pantry

        ArrayList<PantryItem> pantryItems =
                databaseHelper.getAllIngredients();


        // Remove old results

        matchingRecipes.clear();


        // Check every recipe

        for (Recipe recipe : allRecipes) {

            if (recipeCanBeMade(
                    recipe,
                    pantryItems)) {

                matchingRecipes.add(recipe);
            }
        }


        // Update RecyclerView

        recipeAdapter.notifyDataSetChanged();


        // =====================================================
        // SHOW / HIDE NO RECIPES MESSAGE
        // =====================================================

        if (matchingRecipes.isEmpty()) {

            recyclerRecipes.setVisibility(
                    View.GONE
            );

            tvNoRecipes.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerRecipes.setVisibility(
                    View.VISIBLE
            );

            tvNoRecipes.setVisibility(
                    View.GONE
            );
        }
    }


    // =========================================================
    // STRICT RECIPE MATCHING
    // =========================================================

    private boolean recipeCanBeMade(
            Recipe recipe,
            ArrayList<PantryItem> pantryItems) {


        String ingredients =
                recipe.getIngredients();


        // Recipe must have ingredients

        if (ingredients == null ||
                ingredients.trim().isEmpty()) {

            return false;
        }


        // Example:
        // Chicken:1;Rice:1;Avo:1

        String[] requiredIngredients =
                ingredients.split(";");


        // =====================================================
        // EVERY INGREDIENT MUST BE AVAILABLE
        // =====================================================

        for (String required :
                requiredIngredients) {


            String[] parts =
                    required.split(":");


            // Invalid recipe ingredient format

            if (parts.length != 2) {

                return false;
            }


            String requiredName =
                    parts[0].trim();


            double requiredQuantity;


            try {

                requiredQuantity =
                        Double.parseDouble(
                                parts[1].trim()
                        );

            } catch (NumberFormatException e) {

                return false;
            }


            // =================================================
            // ADD UP ALL MATCHING PANTRY QUANTITIES
            // =================================================

            double pantryQuantity = 0;


            for (PantryItem pantryItem :
                    pantryItems) {


                String pantryName =
                        pantryItem.getName();


                if (ingredientsMatch(
                        pantryName,
                        requiredName)) {


                    pantryQuantity +=
                            pantryItem.getQuantity();
                }
            }


            // =================================================
            // REQUIRED QUANTITY MUST BE AVAILABLE
            // =================================================

            if (pantryQuantity <
                    requiredQuantity) {

                // Even ONE missing ingredient
                // means the recipe cannot be made.

                return false;
            }
        }


        // =====================================================
        // ALL INGREDIENTS AND QUANTITIES ARE AVAILABLE
        // =====================================================

        return true;
    }


    // =========================================================
    // INGREDIENT NAME MATCHING
    // =========================================================

    private boolean ingredientsMatch(
            String pantryName,
            String requiredName) {


        if (pantryName == null ||
                requiredName == null) {

            return false;
        }


        String pantry =
                normalizeIngredient(
                        pantryName
                );


        String required =
                normalizeIngredient(
                        requiredName
                );


        return pantry.equals(required);
    }


    // =========================================================
    // NORMALIZE INGREDIENT NAMES
    // =========================================================

    private String normalizeIngredient(
            String ingredient) {


        String value =
                ingredient
                        .trim()
                        .toLowerCase();


        // =====================================================
        // HANDLE COMMON SPELLING VARIATION
        // =====================================================

        if (value.equals("patatoes")) {

            value = "potatoes";
        }


        // =====================================================
        // HANDLE PLURAL FORM
        // =====================================================

        if (value.endsWith("s") &&
                value.length() > 3) {

            value =
                    value.substring(
                            0,
                            value.length() - 1
                    );
        }


        return value;
    }
}