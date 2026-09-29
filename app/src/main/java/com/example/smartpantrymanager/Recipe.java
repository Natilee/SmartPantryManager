package com.example.smartpantrymanager;

public class Recipe {

    private int id;
    private String name;
    private String ingredients;
    private String method;

    public Recipe(
            int id,
            String name,
            String ingredients,
            String method) {

        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getIngredients() {
        return ingredients;
    }


    public String getMethod() {
        return method;
    }
}