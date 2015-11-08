package com.example.wmakaben.caloriecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class ResultActivity extends AppCompatActivity {
    String results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        results = intent.getExtras().getString("json");

        TextView text = (TextView)findViewById(R.id.item_info);

        try{
            JSONObject json = new JSONObject(results);
            JSONArray array = json.getJSONObject("foods").getJSONArray("results");
            //text.setText(array.getJSONObject(0).toString());
            JSONObject a = array.getJSONObject(0);
            String lists = "";

            Iterator<String> iter = a.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = a.get(key);
                    lists += a.get(key).toString() + "\n";
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

            text.setText(lists);

        } catch(JSONException e){
            // DO SOMETHING
            text.setText("Unable to retrieve information");
        }

    }


}
