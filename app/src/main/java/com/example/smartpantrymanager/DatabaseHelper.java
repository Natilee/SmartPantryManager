package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 4;

    // =========================================================
    // INGREDIENTS TABLE
    // =========================================================

    private static final String TABLE_INGREDIENTS = "ingredients";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY = "expiryDate";

    // =========================================================
    // RECIPES TABLE
    // =========================================================

    private static final String TABLE_RECIPES = "recipes";

    private static final String RECIPE_ID = "recipe_id";
    private static final String RECIPE_NAME = "recipe_name";
    private static final String RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String RECIPE_METHOD = "recipe_method";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =========================================================
    // CREATE DATABASE
    // =========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // -----------------------------------------------------
        // INGREDIENT TABLE
        // -----------------------------------------------------

        String createIngredientTable =
                "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY + " TEXT)";

        db.execSQL(createIngredientTable);

        // -----------------------------------------------------
        // RECIPE TABLE
        // -----------------------------------------------------

        String createRecipeTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                        RECIPE_METHOD + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipeTable);

        // -----------------------------------------------------
        // ADD RECIPES
        // -----------------------------------------------------

        seedRecipes(db);
    }

    // =========================================================
    // DATABASE UPGRADE
    // =========================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        // -----------------------------------------------------
        // VERSION 1 → VERSION 2
        // -----------------------------------------------------

        if (oldVersion < 2) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_INGREDIENTS +
                            " ADD COLUMN " +
                            COLUMN_QUANTITY +
                            " REAL NOT NULL DEFAULT 1"
            );

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_INGREDIENTS +
                            " ADD COLUMN " +
                            COLUMN_UNIT +
                            " TEXT NOT NULL DEFAULT 'item'"
            );

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_INGREDIENTS +
                            " ADD COLUMN " +
                            COLUMN_EXPIRY +
                            " TEXT"
            );
        }

        // -----------------------------------------------------
        // VERSION 2 → VERSION 3
        // -----------------------------------------------------

        if (oldVersion < 3) {

            String createRecipeTable =
                    "CREATE TABLE " +
                            TABLE_RECIPES +
                            " (" +
                            RECIPE_ID +
                            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            RECIPE_NAME +
                            " TEXT NOT NULL, " +
                            RECIPE_INGREDIENTS +
                            " TEXT NOT NULL, " +
                            RECIPE_METHOD +
                            " TEXT NOT NULL" +
                            ")";

            db.execSQL(createRecipeTable);

            seedRecipes(db);
        }

        // -----------------------------------------------------
        // VERSION 3 → VERSION 4
        // -----------------------------------------------------

        if (oldVersion < 4) {

            // Only recipes are recreated.
            // Pantry ingredients are preserved.

            db.delete(
                    TABLE_RECIPES,
                    null,
                    null
            );

            seedRecipes(db);
        }
    }

    // =========================================================
    // ADD INGREDIENT
    // =========================================================

    public long addIngredient(
            String ingredient,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                ingredient
        );

        values.put(
                COLUMN_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_UNIT,
                unit
        );

        values.put(
                COLUMN_EXPIRY,
                expiryDate
        );

        long result =
                db.insert(
                        TABLE_INGREDIENTS,
                        null,
                        values
                );

        db.close();

        return result;
    }

    // =========================================================
    // GET ALL INGREDIENTS
    // =========================================================

    public ArrayList<PantryItem> getAllIngredients() {

        ArrayList<PantryItem> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                COLUMN_ID + ", " +
                                COLUMN_NAME + ", " +
                                COLUMN_QUANTITY + ", " +
                                COLUMN_UNIT + ", " +
                                COLUMN_EXPIRY +

                                " FROM " +
                                TABLE_INGREDIENTS +

                                " ORDER BY " +
                                COLUMN_ID +
                                " ASC",

                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            double quantity =
                    cursor.getDouble(2);

            String unit =
                    cursor.getString(3);

            String expiryDate =
                    cursor.getString(4);

            PantryItem item =
                    new PantryItem(
                            id,
                            name,
                            quantity,
                            unit,
                            expiryDate
                    );

            ingredients.add(item);
        }

        cursor.close();

        db.close();

        return ingredients;
    }

    // =========================================================
    // UPDATE INGREDIENT
    // =========================================================

    public boolean updateIngredient(
            int id,
            String ingredient,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                ingredient
        );

        values.put(
                COLUMN_QUANTITY,
                quantity
        );

        values.put(
                COLUMN_UNIT,
                unit
        );

        values.put(
                COLUMN_EXPIRY,
                expiryDate
        );

        int result =
                db.update(
                        TABLE_INGREDIENTS,
                        values,
                        COLUMN_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result > 0;
    }

    // =========================================================
    // DELETE INGREDIENT
    // =========================================================

    public boolean deleteIngredient(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_INGREDIENTS,
                        COLUMN_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result > 0;
    }

    // =========================================================
    // CLEAR ALL INGREDIENTS
    // =========================================================

    public void clearAllIngredients() {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                TABLE_INGREDIENTS,
                null,
                null
        );

        db.close();
    }

    // =========================================================
    // GET ALL RECIPES
    // =========================================================

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT " +
                                RECIPE_ID + ", " +
                                RECIPE_NAME + ", " +
                                RECIPE_INGREDIENTS + ", " +
                                RECIPE_METHOD +

                                " FROM " +
                                TABLE_RECIPES +

                                " ORDER BY " +
                                RECIPE_ID +
                                " ASC",

                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String ingredients =
                    cursor.getString(2);

            String method =
                    cursor.getString(3);

            Recipe recipe =
                    new Recipe(
                            id,
                            name,
                            ingredients,
                            method
                    );

            recipes.add(recipe);
        }

        cursor.close();

        db.close();

        return recipes;
    }

    // =========================================================
    // COUNT RECIPES
    // =========================================================

    public int getRecipeCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_RECIPES,
                        null
                );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        db.close();

        return count;
    }

    // =========================================================
    // ADD RECIPE
    // =========================================================

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String ingredients,
            String method) {

        ContentValues values =
                new ContentValues();

        values.put(
                RECIPE_NAME,
                name
        );

        values.put(
                RECIPE_INGREDIENTS,
                ingredients
        );

        values.put(
                RECIPE_METHOD,
                method
        );

        db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    // =========================================================
    // SEED RECIPES
    // =========================================================

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Chicken Bowl",
                "Chicken:1;Rice:1;Avo:0.5;Feta Cheese:0.25",
                "Cook the chicken until fully cooked. " +
                        "Prepare the rice. " +
                        "Slice half an avocado. " +
                        "Combine the chicken and rice, " +
                        "then add the avocado and feta cheese."
        );

        addRecipe(
                db,
                "Steak & Corn",
                "Steak:1;Corn:1",
                "Cook the steak to your preferred level. " +
                        "Heat the corn and serve together."
        );

        addRecipe(
                db,
                "Beef Burger",
                "Beef patties:1;Cheese:1;Hamburger rolls:1;Onion:0.5",
                "Cook the beef patty thoroughly. " +
                        "Toast the hamburger roll. " +
                        "Add the beef patty, cheese and sliced onion."
        );

        addRecipe(
                db,
                "Cheesy Steak & Potatoes",
                "Steak:1;Potatoes:2;Cheese:1",
                "Cook the steak to your preferred level. " +
                        "Boil or roast the potatoes until tender. " +
                        "Add cheese and serve with the steak."
        );

        addRecipe(
                db,
                "Chicken & Feta Wrap",
                "Chicken:1;Wraps:1;Feta Cheese:0.25",
                "Cook the chicken and slice it. " +
                        "Place the chicken and feta cheese inside a wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Beef & Cheese Wrap",
                "Beef patties:1;Wraps:1;Cheese:1",
                "Cook the beef patty and slice it. " +
                        "Place the beef and cheese inside a wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Chicken Avo Wrap",
                "Chicken:1;Wraps:1;Avo:0.5",
                "Cook and slice the chicken. " +
                        "Add the chicken and avocado to a wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Chicken & Cheese Wrap",
                "Chicken:1;Wraps:1;Cheese:1",
                "Cook the chicken. " +
                        "Place the chicken and cheese inside a wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Steak & Cheese Wrap",
                "Steak:1;Wraps:1;Cheese:1",
                "Cook and slice the steak. " +
                        "Add the steak and cheese to a wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Beef & Onion Wrap",
                "Beef patties:1;Wraps:1;Onion:0.5",
                "Cook the beef patty and slice it. " +
                        "Add the beef and sliced onion to the wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Chicken Rice Bowl",
                "Chicken:1;Rice:1",
                "Cook the chicken thoroughly. " +
                        "Prepare the rice and serve the chicken over the rice."
        );

        addRecipe(
                db,
                "Cheesy Chicken Rice",
                "Chicken:1;Rice:1;Cheese:1",
                "Cook the chicken and prepare the rice. " +
                        "Combine them and add cheese on top."
        );

        addRecipe(
                db,
                "Steak & Potatoes",
                "Steak:1;Potatoes:2",
                "Cook the steak. " +
                        "Boil or roast the potatoes until tender. " +
                        "Serve together."
        );

        addRecipe(
                db,
                "Beef & Cheese",
                "Beef patties:1;Cheese:1",
                "Cook the beef patty thoroughly. " +
                        "Add cheese while the patty is hot and serve."
        );

        addRecipe(
                db,
                "Chicken & Avocado",
                "Chicken:1;Avo:0.5",
                "Cook and slice the chicken. " +
                        "Slice half an avocado and serve together."
        );

        addRecipe(
                db,
                "Cheesy Steak",
                "Steak:1;Cheese:1",
                "Cook the steak to your preferred level. " +
                        "Add cheese on top and allow it to melt."
        );

        addRecipe(
                db,
                "Beef & Avocado Wrap",
                "Beef patties:1;Wraps:1;Avo:0.5",
                "Cook the beef patty and slice it. " +
                        "Add the beef and avocado to a wrap. " +
                        "Fold and serve."
        );

        addRecipe(
                db,
                "Chicken & Onion Wrap",
                "Chicken:1;Wraps:1;Onion:0.5",
                "Cook the chicken and slice it. " +
                        "Add the chicken and sliced onion to the wrap. " +
                        "Fold and serve."
        );
    }
}