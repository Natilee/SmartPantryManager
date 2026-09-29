package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;

    private OnRecipeClickListener listener;


    // =========================================================
    // CLICK LISTENER
    // =========================================================

    public interface OnRecipeClickListener {

        void onRecipeClick(Recipe recipe);
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RecipeAdapter(
            ArrayList<Recipe> recipes,
            OnRecipeClickListener listener) {

        this.recipes = recipes;
        this.listener = listener;
    }


    // =========================================================
    // CREATE VIEW HOLDER
    // =========================================================

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_recipe,
                                parent,
                                false
                        );

        return new RecipeViewHolder(view);
    }


    // =========================================================
    // BIND DATA
    // =========================================================

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe =
                recipes.get(position);


        holder.tvRecipeName.setText(
                recipe.getName()
        );


        holder.tvRecipeIngredients.setText(
                formatIngredients(
                        recipe.getIngredients()
                )
        );


        holder.btnViewRecipe.setOnClickListener(v -> {

            if (listener != null) {

                listener.onRecipeClick(recipe);
            }
        });
    }


    // =========================================================
    // NUMBER OF RECIPES
    // =========================================================

    @Override
    public int getItemCount() {

        return recipes.size();
    }


    // =========================================================
    // FORMAT INGREDIENTS
    // =========================================================

    private String formatIngredients(
            String ingredients) {

        if (ingredients == null ||
                ingredients.isEmpty()) {

            return "";
        }


        String[] parts =
                ingredients.split(";");


        StringBuilder result =
                new StringBuilder();


        for (int i = 0;
             i < parts.length;
             i++) {

            String[] ingredient =
                    parts[i].split(":");


            if (ingredient.length > 0) {

                if (i > 0) {

                    result.append(" + ");
                }


                result.append(
                        ingredient[0]
                );
            }
        }


        return result.toString();
    }


    // =========================================================
    // VIEW HOLDER
    // =========================================================

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;

        TextView tvRecipeIngredients;

        Button btnViewRecipe;


        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);


            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );


            tvRecipeIngredients =
                    itemView.findViewById(
                            R.id.tvRecipeIngredients
                    );


            btnViewRecipe =
                    itemView.findViewById(
                            R.id.btnViewRecipe
                    );
        }
    }
}