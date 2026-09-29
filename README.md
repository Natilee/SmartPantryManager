# Smart Pantry Manager

## Project Overview

Smart Pantry Manager is a Java Android application designed to help users manage their pantry ingredients and reduce food waste.

The application allows users to record ingredients that they currently have available, including quantities, units and optional expiry dates. The application then compares the available pantry ingredients with stored recipes and displays recipes that can be prepared using the available ingredients.

## Project Purpose

The purpose of Smart Pantry Manager is to help users make better use of leftover ingredients instead of allowing unused food to go to waste.

The application focuses on strict recipe matching. A recipe is only suggested when all of its required ingredients and required quantities are available in the user's pantry.

## Main Features

### Pantry Management

Users can:

- Add pantry ingredients
- Edit existing ingredients
- Delete ingredients
- View pantry ingredients
- Enter ingredient quantities
- Select measurement units
- Add optional expiry dates
- Clear all pantry ingredients

### Recipe Suggestions

The application contains a collection of stored recipes.

The recipe suggestion system checks:

- Ingredient names
- Required quantities
- Available pantry quantities

A recipe is only displayed when all required ingredients and quantities are available.

Partial matches are not displayed.

### Recipe Details

Users can select a suggested recipe to view:

- Recipe name
- Required ingredients
- Ingredient quantities
- Preparation method

### Settings

The Settings screen allows users to:

- View application information
- Clear all pantry ingredients
- Return to the pantry

## Technologies Used

- Java
- Android Studio
- Android SDK
- SQLite
- RecyclerView
- Android Activities
- XML layouts
- Git
- GitHub

## Database

The application uses SQLite to store pantry ingredients and recipes.

The pantry database stores information such as:

- Ingredient ID
- Ingredient name
- Quantity
- Unit
- Expiry date

The recipe database stores:

- Recipe ID
- Recipe name
- Required ingredients
- Preparation method

The database allows information to remain available when the application is closed and reopened.

## Application Screens

The application contains the following main screens:

1. Pantry / Home Screen
2. Add Ingredient Screen
3. Suggested Recipes Screen
4. Recipe Detail Screen
5. Settings Screen

## Recipe Matching

The application uses strict recipe matching.

For each recipe, the application checks whether every required ingredient exists in the pantry with enough quantity.

For example:

If a recipe requires:

- Chicken × 1
- Rice × 1

and the pantry contains:

- Chicken × 1
- Rice × 1

the recipe can be suggested.

However, if the pantry only contains:

- Chicken × 1

the recipe will not be suggested because the required rice is missing.

The same principle applies when the pantry contains an insufficient quantity of an ingredient.

## Input Validation

The application validates user input when adding or editing ingredients.

Validation includes:

- Ingredient name cannot be empty
- Ingredient name has a maximum length
- Quantity cannot be empty
- Quantity must be a valid number
- Quantity must be greater than zero
- Quantity must remain within the allowed range
- A measurement unit must be selected

## Project Structure

The project is organised into Android activities, database classes, model classes and adapters.

Important components include:

- `MainActivity`
- `AddIngredientActivity`
- `SuggestedRecipesActivity`
- `RecipeDetailActivity`
- `SettingsActivity`
- `DatabaseHelper`
- `PantryItem`
- `Recipe`
- `PantryAdapter`
- `RecipeAdapter`

## How to Run the Application

1. Install Android Studio.
2. Open the Smart Pantry Manager project.
3. Allow Gradle to synchronise.
4. Create or select an Android Virtual Device.
5. Run the application using the Android Studio Run button.
6. Add ingredients to the pantry.
7. Open Suggested Recipes to view recipes that can currently be prepared.

## GitHub Version Control

Git and GitHub are used to track the development of the application.

The project contains multiple meaningful commits documenting the development of:

- Pantry functionality
- Input validation
- CRUD functionality
- SQLite recipe storage
- Recipe matching
- Recipe details
- Settings and navigation
- User interface improvements
- Project documentation

## Author

Natilee Le Roux

## Project

Smart Pantry Manager

Java Android Application