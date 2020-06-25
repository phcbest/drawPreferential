package com.phc.neckrreferential.ui.fragment;

import android.view.View;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.base.BaseFragment;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 10
 * 描述：
 */
public class SelectedFragment extends BaseFragment {



    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

}
