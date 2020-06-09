package com.phc.neckrreferential.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public FrameLayout mainPageContainer;

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mainNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
        initListener();

    }

    private void initListener() {
        mainNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    Log.d(TAG, "切换到了home");
                }else if (item.getItemId() == R.id.selected){
                    Log.d(TAG, "切换到了selected");
                }else if (item.getItemId() == R.id.red_packet){
                    Log.d(TAG, "切换到了red_packet");
                }else if (item.getItemId() == R.id.search){
                    Log.d(TAG, "切换到了search");
                }
                return true;
            }
        });
    }

    private void initView() {
        //user transaction add fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_page_container, new HomeFragment());
        ft.commit();

        mainPageContainer = (FrameLayout) findViewById(R.id.main_page_container);

    }
}