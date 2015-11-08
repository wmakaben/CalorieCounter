package com.example.wmakaben.caloriecounter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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
        protected String doInBackground(byte[]... img){
            byte[] bm = img[0];
            String response = "";
            try{
                URL url = new URL(IMAGEURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

                DataOutputStream request = new DataOutputStream(connection.getOutputStream());
                request.writeBytes(TWOHYPHENS + BOUNDARY + CRLF);
                request.writeBytes("Content-Disposition: form-data; name=\"" + ATTACHMENTNAME + "\";filename=\"" + ATTACHMENTFILENAME + "\"" + CRLF);
                request.writeBytes(CRLF);
                request.write(bm);
                request.writeBytes(CRLF);
                request.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + CRLF);
                request.flush();
                request.close();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());

                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                response = stringBuilder.toString();
                responseStream.close();
                connection.disconnect();

            } catch (MalformedURLException e){
                Log.d("MainActivity", "MalformedURLException: " + e.getMessage());
            } catch (IOException e){
                Log.d("MainActivity", "IOException: " + e.getMessage());
            }

            return response;
        }

        protected void onPostExecute(String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //Log.d("RESULT", result);
            try{
                JSONObject jObject = new JSONObject(result);
                if (jObject.getJSONArray("data").length() == 0){
                    // Start intent for search box.
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);

                }
                //Log.d("RESULT", jObject.getJSONArray("data").toString());




            }catch(JSONException e){
                Log.d("onPostExecute", e.getMessage());
            }
        }
    }
}
