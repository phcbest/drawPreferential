package com.phc.neckrreferential.ui.activity;

import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseActivity;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.ui.fragment.HomeFragment;
import com.phc.neckrreferential.ui.fragment.RedPacketFragment;
import com.phc.neckrreferential.ui.fragment.SearchFragment;
import com.phc.neckrreferential.ui.fragment.SelectedFragment;
import com.phc.neckrreferential.utils.logUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    public FrameLayout mainPageContainer;

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mainNavigationBar;
    private RedPacketFragment mRedPacketFragment;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mfm;

    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initFragment();
    }



    /**
     * 初始化fragment，将fragment全添加进去
     */
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
                if (item.getItemId() == R.id.home) {
                    logUtils.d(this, "切换到了home");
                    switchFragment(mHomeFragment);
                } else if (item.getItemId() == R.id.selected) {
                    logUtils.e(this, "切换到了selected");
                    switchFragment(mSelectedFragment);
                } else if (item.getItemId() == R.id.red_packet) {
                    logUtils.i(this, "切换到了red_packet");
                    switchFragment(mRedPacketFragment);
                } else if (item.getItemId() == R.id.search) {
                    logUtils.w(this, "切换到了search");
                    switchFragment(mSearchFragment);
                }
                return true;
            }


        });

    }

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //修改成add和hide的方式来控制fragment的切换
        FragmentTransaction fragmentTransaction = mfm.beginTransaction();
        /**
         * Return true if the fragment is currently added to its activity.
         */
        if (!targetFragment.isAdded()) {
            //没有添加进来，使用事务添加fragment，第一个是坑，第二个是填坑的
            fragmentTransaction.add(R.id.main_page_container, targetFragment);
        } else {
            //添加进来了，使用show让他出现
            fragmentTransaction.show(targetFragment);
        }
        //判断是否有最后一个fragment
        if (lastOneFragment != null) {
            //有就隐藏追
            fragmentTransaction.hide(lastOneFragment);
        }
        //将目标fragment保存为最后一个fragment
        lastOneFragment = targetFragment ;
        //直接使用更换的方法会导致重新加载，所以使用添加和隐藏
//        fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }
}