package com.example.wmakaben.caloriecounter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wmakaben on 11/8/15.
 */
public class HistoryListRow {
    public static final int LAYOUT = R.layout.history_list_item;

    private final Context context;
    private final TextView textView;
    private final ImageView imageView;

    private HistoryListRow(Context context, View convertView){
        this.context = context;
        this.imageView = (ImageView)convertView.findViewById(R.id.history_list_icon);
        this.textView = (TextView)convertView.findViewById(R.id.history_list_text);
    }

    public void bind(HistoryListItem historyListItem){
        this.textView.setText(historyListItem.getLabel());
        //Picasso.with(this.context).load(historyListItem.getImageUrl()).into(this.imageView);
    }

}
