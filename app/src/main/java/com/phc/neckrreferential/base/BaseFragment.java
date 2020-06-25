package com.phc.neckrreferential.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/6 14
 * 描述：
 */
public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    /**
     * 定义页面状态
     *
     */
    public enum State{
        /**
         *
         */
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    @OnClick(R.id.network_error_tips)
    public void retry(){
        //点了重新加载
        LogUtils.d(this,"on reTry...........");
        onRetryClick();
    }

    /**
     * 如果子fragment需要知道网络错误以后的点击，覆盖该方法
     */
    protected void onRetryClick() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //因为该方法可以重写，，所以每次的rootView都不一样
        View rootView = loadRootView(inflater,container);
        //将baseFragmentLayout中的FrameLayout base_container绑定对象，这个坑为rootView中留下来的base_container组件
        mBaseContainer = (FrameLayout) rootView.findViewById(R.id.base_container);
        //加载状态视图
        loadStatesView(inflater,container);
        //视图绑定的目标类，给黄油刀环境，因为是在非activity中使用，需要给this和view，子类继承也不需要给环境了
        mBind = ButterKnife.bind(this, rootView);
        //这里有两段逻辑，主要的对于页面进行初始化
        initView(rootView);
        initPresenter();
        loadData();
        return rootView;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载所有状态的view
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        //加载成功的
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);
        //加载loading的
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);
        //加载错误页面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);
        //内容为empty的页面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        //为了防止出现所有的页面同时出现，需要给一个什么都没有的页面
        setUpState(State.NONE);
    }



    /**
     *将该方法暴露出来用于切换状态
     * 子类通过这个方法来切换状态
     */
    public void setUpState(State state){
        this.currentState = state;
        //控制成功页面的显示与隐藏
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        //控制loading页面的显示与隐藏
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        //控制错误页面的显示与隐藏
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        //控制空页面的显示与隐藏
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);


    }


    /**
     * 加载错误页面
     * @param inflater
     * @param container
     * @return
     */
    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error,container,false);
    }
    /**
     * 加载空页面
     * @param inflater
     * @param container
     * @return
     */
    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }

    /**
     * 加载加载页面
     * @param inflater
     * @param container
     * @return
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading,container,false);
    }

    /**
     * 加载成功页面
     * @param inflater
     * @param container
     * @return 加载成功页面
     */
    protected  View loadSuccessView(LayoutInflater inflater, ViewGroup container){
        int resId = getRootViewResId();
        return inflater.inflate(resId,container,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }

        release();
    }

    protected void initView(View rootView) {

    }

    protected void release() {
        //释放资源
    }

    protected void initPresenter(){
        //创建presenter
    }

    protected void loadData() {
        //加载数据

    }

    protected abstract int getRootViewResId();

}
