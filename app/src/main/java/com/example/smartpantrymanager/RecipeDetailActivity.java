package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvDetailRecipeName;
    private TextView tvDetailRecipeIngredients;
    private TextView tvDetailRecipeMethod;

    private Button btnBackToRecipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_detail
        );


        // =====================================================
        // FIND VIEWS
        // =====================================================

        tvDetailRecipeName =
                findViewById(
                        R.id.tvDetailRecipeName
                );

        tvDetailRecipeIngredients =
                findViewById(
                        R.id.tvDetailRecipeIngredients
                );

        tvDetailRecipeMethod =
                findViewById(
                        R.id.tvDetailRecipeMethod
                );

        btnBackToRecipes =
                findViewById(
                        R.id.btnBackToRecipes
                );


        // =====================================================
        // GET RECIPE INFORMATION
        // =====================================================

        String recipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        String recipeIngredients =
                getIntent().getStringExtra(
                        "recipe_ingredients"
                );

        String recipeMethod =
                getIntent().getStringExtra(
                        "recipe_method"
                );


        // =====================================================
        // DISPLAY RECIPE NAME
        // =====================================================

        if (recipeName != null &&
                !recipeName.trim().isEmpty()) {

            tvDetailRecipeName.setText(
                    recipeName
            );
        }


        // =====================================================
        // DISPLAY INGREDIENTS
        // =====================================================

        if (recipeIngredients != null &&
                !recipeIngredients.trim().isEmpty()) {

            tvDetailRecipeIngredients.setText(
                    formatIngredients(
                            recipeIngredients
                    )
            );

        } else {

            tvDetailRecipeIngredients.setText(
                    "No ingredients available."
            );
        }


        // =====================================================
        // DISPLAY METHOD
        // =====================================================

        if (recipeMethod != null &&
                !recipeMethod.trim().isEmpty()) {

            tvDetailRecipeMethod.setText(
                    recipeMethod
            );

        } else {

            tvDetailRecipeMethod.setText(
                    "No preparation method available."
            );
        }


        // =====================================================
        // BACK BUTTON
        // =====================================================

        btnBackToRecipes.setOnClickListener(v ->
                finish()
        );
    }


    // =========================================================
    // FORMAT INGREDIENTS
    // =========================================================

    private String formatIngredients(
            String ingredients) {


        if (ingredients == null ||
                ingredients.trim().isEmpty()) {

            return "";
        }


        String[] parts =
                ingredients.split(";");


        StringBuilder result =
                new StringBuilder();


        for (String part : parts) {


            String[] ingredient =
                    part.split(":");


            if (ingredient.length >= 1) {


                if (result.length() > 0) {

                    result.append("\n");
                }


                // Ingredient name

                result.append("• ");

                result.append(
                        ingredient[0].trim()
                );


                // Required quantity

                if (ingredient.length >= 2) {

                    result.append(" × ");

                    result.append(
                            ingredient[1].trim()
                    );
                }
            }
        }


        return result.toString();
    }
}