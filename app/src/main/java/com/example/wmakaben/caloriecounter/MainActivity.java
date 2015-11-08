package com.example.wmakaben.caloriecounter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener{
    public static final String IMAGEURL = "http://153.104.42.132:8080/api";
    //public static final String IMAGEURL = "http://153.104.123.159:8080/api";
    public static final String BOUNDARY = "*****";
    public static final String CRLF = "\r\n";
    public static final String TWOHYPHENS = "--";
    public static final String ATTACHMENTNAME = "data";
    public static final String ATTACHMENTFILENAME = "image.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomPagerAdapter mPagerAdapter;
        mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(mPagerAdapter);
    }

    //public void OnCaptureButtonSelected(Camera camera, PictureCallback picture){
    public void OnCaptureButtonSelected(byte[] data){
        ImageUploadTask task = new ImageUploadTask();
        task.execute(data);
    }


    public void onRecordSelectedInteraction(String string) {
        // Do different stuff
        Log.d("yo",string);
    }

    private class ImageUploadTask extends AsyncTask<byte[], Void, String>{

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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
            Log.d("RESULT", result);
            try{
                JSONObject jObject = new JSONObject(result);
                if (jObject.getJSONArray("data").length() == 0){
                    // Start intent for search box.
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);

                }
                Log.d("RESULT", jObject.getJSONArray("data").toString());




            }catch(JSONException e){
                Log.d("onPostExecute", e.getMessage());
            }
        }
    }
    
}
