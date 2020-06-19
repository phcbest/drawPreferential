package com.phc.neckrreferential.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.ui.fragment.HomePagerFragment;
import com.phc.neckrreferential.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/14 10
 * 描述：
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    /**
     * 该集合用于保存分目类类别的数据
     */
    private List<Categories.DataBean> categoryList = new ArrayList<>();

    /***
     *    构造方法调用该方法需要给一个fragmentManager
     */
    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    /**
     * fragmentPagerAdapter的方法，同于返回当前页面的fragmentTitle
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    /**
     * 返回当前页面
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        //实例化一个新的fragment返回
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        //返回有几个页面
        return categoryList.size();
    }

    /**
     * 该方法
     * @param categories
     */
    public void setCategoryList(Categories categories) {
        //给Categories对象填充数据，
        //清空对象
        this.categoryList.clear();
        //将拿到的数据新建Categories对象
        List<Categories.DataBean> data = categories.getData();
        //将新建的对象添加到老对象中去
        this.categoryList.addAll(data);
        LogUtils.d(this,"size"+categoryList.size());
        //告诉观察者数据更新
        notifyDataSetChanged();
    }
}
