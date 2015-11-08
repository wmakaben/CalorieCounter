package com.example.wmakaben.caloriecounter;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

        try{
            URL url = new URL(IMAGEURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY + CRLF);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());
            request.writeBytes(TWOHYPHENS + BOUNDARY + CRLF);
            request.writeBytes("Content-Disposition: form-data; name=\"" + ATTACHMENTNAME + "\";filename=\"" + ATTACHMENTFILENAME + "\"" + CRLF);
            request.writeBytes(CRLF);
            request.write(data);
            request.writeBytes(CRLF);
            request.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + CRLF);
            request.flush();
            request.close();
            Log.d("WORK GOD DAMMIT", "ITS WORKING");
            InputStream responseStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            responseStream.close();
            connection.disconnect();

            Log.d("RESPONSE", response);

        } catch (MalformedURLException e){
            Log.d("MainActivity", "MalformedURLException: " + e.getMessage());
        } catch (IOException e){
            Log.d("MainActivity", "IOException: " + e.getMessage());
        }
    }


    public void onRecordSelectedInteraction(String string) {
        // Do different stuff
        Log.d("yo",string);
    }

    private class ImageUploadTask extends AsyncTask<byte[], Void, String>{

        @Override
        protected void onPreExecute(){

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
            Log.d("RESULT", result);
        }
    }
    
}


 /*
    private class ImageUploadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls){
            String output = "";
            for(String url: urls){
                output = getOutputFromURL(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoOutput(true);
                httpConnection.connect();
                //post

                OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream());
                String urlParameters = "name="+name;
                writer.write(urlParameters);
                writer.flush();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            msg.setText(output);
        }
    }

    */