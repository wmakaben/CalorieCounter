package com.example.wmakaben.caloriecounter;

/**
 * Created by wmakaben on 11/8/15.
 */
public class HistoryListItem {
    private String label;
    private String imageUrl;

    private HistoryListItem(String label, String imageUrl){
        this.label = label;
        this.imageUrl = imageUrl;
    }

    public String getLabel(){ return label; }
    public String getImageUrl() {return imageUrl; }
    public void setLabel(String label){ this.label = label; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
