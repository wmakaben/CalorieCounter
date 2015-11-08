package com.example.wmakaben.caloriecounter;

/**
 * Created by wmakaben on 11/8/15.
 */
public class Ingredient {
    private String name;
    private int calories;
    private boolean selected;

    public Ingredient(String name, int calories, boolean selected){
        this.name = name;
        this.calories = calories;
        this.selected = selected;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}
