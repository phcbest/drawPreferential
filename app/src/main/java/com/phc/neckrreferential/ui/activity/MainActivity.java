package com.phc.neckrreferential.ui.activity;

import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseActivity;
import com.phc.neckrreferential.base.BaseFragment;
import com.phc.neckrreferential.ui.fragment.HomeFragment;
import com.phc.neckrreferential.ui.fragment.OnSellFragment;
import com.phc.neckrreferential.ui.fragment.SearchFragment;
import com.phc.neckrreferential.ui.fragment.SelectedFragment;
import com.phc.neckrreferential.utils.ToastUtils;
import com.phc.neckrreferential.utils.logUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity {


    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;

    private OnSellFragment mOnSellFragment;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mfm;


    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected void initPresenter() {

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
        mOnSellFragment = new OnSellFragment();
        mSelectedFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mfm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }


    private long exitTime = 0;

    /**
     * TODO 需要做出返回键的逻辑，点两下返回键才可以退出
     * 这里的返回是是否放行该事件，如果放行返回true，不然返回false
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit() {
        //这里面拿到第二下点击后再修改isExit
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        }else {
            finish();
        }
    }

    private void initListener() {
        //导航栏的点击事件
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    switchFragment(mOnSellFragment);
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
        //判断是否有最后一个fragment 如果最后一个点击和新点击的是一样的直接退出判断
        if (lastOneFragment != null && lastOneFragment != targetFragment) {
            //有就隐藏
            fragmentTransaction.hide(lastOneFragment);
        }
        //将目标fragment保存为最后一个fragment
        lastOneFragment = targetFragment;
        //直接使用更换的方法会导致重新加载，所以使用添加和隐藏
//        fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void switch2Search() {
        //切换导航栏中的选中,于点击同效果
        mNavigationView.setSelectedItemId(R.id.search);
    }
}