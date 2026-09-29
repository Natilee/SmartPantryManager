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

        setContentView(R.layout.activity_recipe_detail);

        tvDetailRecipeName =
                findViewById(R.id.tvDetailRecipeName);

        tvDetailRecipeIngredients =
                findViewById(R.id.tvDetailRecipeIngredients);

        tvDetailRecipeMethod =
                findViewById(R.id.tvDetailRecipeMethod);

        btnBackToRecipes =
                findViewById(R.id.btnBackToRecipes);

        String recipeName =
                getIntent().getStringExtra("recipe_name");

        String recipeIngredients =
                getIntent().getStringExtra("recipe_ingredients");

        String recipeMethod =
                getIntent().getStringExtra("recipe_method");

        if (recipeName == null || recipeName.trim().isEmpty()) {
            recipeName = "Recipe";
        }

        if (recipeIngredients == null ||
                recipeIngredients.trim().isEmpty()) {

            recipeIngredients =
                    "No ingredients information available.";
        }

        if (recipeMethod == null ||
                recipeMethod.trim().isEmpty()) {

            recipeMethod =
                    "No preparation method available.";
        }

        tvDetailRecipeName.setText(recipeName);

        tvDetailRecipeIngredients.setText(
                formatIngredients(recipeIngredients)
        );

        tvDetailRecipeMethod.setText(recipeMethod);

        btnBackToRecipes.setOnClickListener(v -> finish());
    }

    private String formatIngredients(String ingredients) {

        StringBuilder formatted =
                new StringBuilder();

        String[] ingredientList =
                ingredients.split(";");

        for (String ingredient : ingredientList) {

            String[] parts =
                    ingredient.split(":");

            if (parts.length == 2) {

                String name =
                        parts[0].trim();

                String quantity =
                        parts[1].trim();

                formatted.append("• ")
                        .append(name)
                        .append("  × ")
                        .append(quantity)
                        .append("\n");

            } else {

                formatted.append("• ")
                        .append(ingredient.trim())
                        .append("\n");
            }
        }

        return formatted.toString().trim();
    }
}