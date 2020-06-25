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
 * 继承了抽象类，重写了其中的抽象方法，该方法可以传递layout id过去
 */
public class RedPacketFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}
