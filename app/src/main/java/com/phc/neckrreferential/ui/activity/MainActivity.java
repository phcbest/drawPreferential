package com.phc.neckrreferential.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.ui.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainPageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }

    private void initView() {
        //user transaction add fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_page_container,new HomeFragment());
        ft.commit();

        mainPageContainer = (FrameLayout) findViewById(R.id.main_page_container);
    }
}