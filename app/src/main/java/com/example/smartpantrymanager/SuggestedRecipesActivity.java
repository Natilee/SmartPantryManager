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
    private TextView tvRecipeCount;
    private Button btnBack;

    private DatabaseHelper databaseHelper;

    private ArrayList<Recipe> allRecipes = new ArrayList<>();
    private ArrayList<Recipe> matchingRecipes = new ArrayList<>();

    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        tvRecipeCount = findViewById(R.id.tvRecipeCount);
        btnBack = findViewById(R.id.btnBack);

        databaseHelper = new DatabaseHelper(this);

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

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

        recyclerRecipes.setAdapter(recipeAdapter);

        btnBack.setOnClickListener(v ->
                finish()
        );

        findMatchingRecipes();
    }

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

        allRecipes =
                databaseHelper.getAllRecipes();

        ArrayList<PantryItem> pantryItems =
                databaseHelper.getAllIngredients();

        matchingRecipes.clear();

        for (Recipe recipe : allRecipes) {

            if (recipeCanBeMade(
                    recipe,
                    pantryItems)) {

                matchingRecipes.add(recipe);
            }
        }

        recipeAdapter.notifyDataSetChanged();

        // =====================================================
        // NO MATCHING RECIPES
        // =====================================================

        if (matchingRecipes.isEmpty()) {

            recyclerRecipes.setVisibility(
                    View.GONE
            );

            tvRecipeCount.setVisibility(
                    View.GONE
            );

            tvNoRecipes.setVisibility(
                    View.VISIBLE
            );

            tvNoRecipes.setText(
                    "No recipes can be made with your current pantry.\n\n" +
                            "Add the required ingredients and quantities " +
                            "to see recipe suggestions."
            );

        } else {

            recyclerRecipes.setVisibility(
                    View.VISIBLE
            );

            tvRecipeCount.setVisibility(
                    View.VISIBLE
            );

            tvNoRecipes.setVisibility(
                    View.GONE
            );

            tvRecipeCount.setText(
                    matchingRecipes.size() +
                            " recipe" +
                            (matchingRecipes.size() == 1 ? "" : "s") +
                            " available with your pantry"
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

        if (ingredients == null ||
                ingredients.trim().isEmpty()) {

            return false;
        }

        String[] requiredIngredients =
                ingredients.split(";");

        // Every required ingredient must be available.

        for (String required :
                requiredIngredients) {

            String[] parts =
                    required.split(":");

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

            if (requiredQuantity <= 0) {

                return false;
            }

            double pantryQuantity = 0;

            // Add together all pantry entries
            // matching this ingredient.

            for (PantryItem pantryItem :
                    pantryItems) {

                if (ingredientsMatch(
                        pantryItem.getName(),
                        requiredName)) {

                    pantryQuantity +=
                            pantryItem.getQuantity();
                }
            }

            // The recipe is rejected if there is
            // not enough of this ingredient.

            if (pantryQuantity <
                    requiredQuantity) {

                return false;
            }
        }

        // Every ingredient and quantity is available.

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

        if (ingredient == null) {

            return "";
        }

        String value =
                ingredient
                        .trim()
                        .toLowerCase()
                        .replaceAll("\\s+", " ");

        // Handle common spelling variation.

        if (value.equals("patatoes")) {

            value = "potatoes";
        }

        // Handle simple plural forms.

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