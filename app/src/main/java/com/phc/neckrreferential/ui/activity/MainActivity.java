package com.phc.neckrreferential.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.ui.fragment.HomeFragment;
import com.phc.neckrreferential.ui.fragment.RedPacketFragment;
import com.phc.neckrreferential.ui.fragment.SearchFragment;
import com.phc.neckrreferential.ui.fragment.SelectedFragment;
import com.phc.neckrreferential.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {


    public FrameLayout mainPageContainer;

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mainNavigationBar;
    private RedPacketFragment mRedPacketFragment;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mfm;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBind = ButterKnife.bind(this);


        initFragment();
        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSelectedFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mfm = getSupportFragmentManager();
        switchFragment(mHomeFragment);


    }

    private void initListener() {
        mainNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home){
                    LogUtils.d(this,"切换到了home");
                    switchFragment(mHomeFragment);
                }else if (item.getItemId() == R.id.selected){
                    LogUtils.e(this,"切换到了selected");
                    switchFragment(mSelectedFragment);
                }else if (item.getItemId() == R.id.red_packet){
                    LogUtils.i(this,"切换到了red_packet");
                    switchFragment(mRedPacketFragment);
                }else if (item.getItemId() == R.id.search){
                    LogUtils.w(this,"切换到了search");
                    switchFragment(mSearchFragment);
                }
                return true;
            }


        });

    }

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction fragmentTransaction = mfm.beginTransaction();
        fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();
    }

}