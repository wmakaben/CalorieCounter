package com.example.wmakaben.caloriecounter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity {
    public static final String IMAGEURL = "http://153.104.42.132:8080/api/answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupActionBar();

        Button button = (Button)findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = (EditText)SearchActivity.this.findViewById(R.id.search_keyword);
                String text = e.getText().toString();
                if(!text.trim().equals("")){
                    FoodSearchTask task = new FoodSearchTask();
                    task.execute(text);
                }
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FoodSearchTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute(){
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... food){
            String key = food[0];
            String response = "";
            try{
                URL url = new URL(IMAGEURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("food", key);
                connection.setRequestMethod("GET");
                connection.connect();

                int status = connection.getResponseCode();
                switch(status){
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        response =  sb.toString();
                }

                connection.disconnect();

            } catch (MalformedURLException e){
                Log.d("MainActivity", "MalformedURLException: " + e.getMessage());
            } catch (IOException e){
                Log.d("MainActivity", "IOException: " + e.getMessage());
            }

            return response;
        }

        protected void onPostExecute(String result){
            Log.d("TESTING", result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try{
                JSONObject jObject = new JSONObject(result);
                if (jObject.length() == 0){
                    DialogHelper.showDialog( "Error!","Unable to retrieve information at the moment",SearchActivity.this);
                }
                else{
                    Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                    intent.putExtra("json", result);
                    startActivity(intent);
                }
                //Log.d("RESULT", jObject.getJSONArray("data").toString());

            }catch(JSONException e){
                Log.d("onPostExecute", e.getMessage());
            }
        }
    }

}
