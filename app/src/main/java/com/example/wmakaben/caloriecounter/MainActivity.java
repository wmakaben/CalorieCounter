package com.example.wmakaben.caloriecounter;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomPagerAdapter mPagerAdapter;
        mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(mPagerAdapter);
    }

    public void OnCaptureButtonSelected(Camera camera, PictureCallback picture){
        camera.takePicture(null,null,picture);
    }


    public void onRecordSelectedInteraction(String string) {
        // Do different stuff
        Log.d("yo",string);
    }
}
