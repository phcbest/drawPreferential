package com.phc.neckrreferential.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.modle.domain.IBaseInfo;
import com.phc.neckrreferential.modle.domain.ILinearItemInfo;
import com.phc.neckrreferential.utils.ToastUtils;
import com.phc.neckrreferential.utils.UrlUtils;
import com.phc.neckrreferential.utils.logUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/27 09
 * 描述：
 */
public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {


    List<ILinearItemInfo> mData = new ArrayList<>();

    private OnListItemClickListener mItemClickListener = null ;


    /**
     * NonNull参数告诉编译器参数非空
     * @param parent
     * @param viewType
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linear_goods_content,parent,false);
        logUtils.d(this,"onCreateViewHolder");
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ILinearItemInfo dataBean = mData.get(position);
        //设置数据
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        int oldSize = contents.size();
        mData.addAll(contents);
        //通知适配器更新ui，范围更改
        notifyItemRangeChanged(oldSize,contents.size());
        ToastUtils.showToast("加载了"+contents.size()+"条");
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;
        @BindView(R.id.goods_title)
        public TextView title;
        @BindView(R.id.goods_off_prise)
        public TextView offPriseTv;
        @BindView(R.id.goods_after_off_prise)
        public TextView finalPriseTv;
        @BindView(R.id.goods_original_prise)
        public TextView originalPriseTv;
        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(ILinearItemInfo dataBean) {
//            拿到位置的size
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
//            三元运算符算出最长的边
//            int coverSize = Math.max(layoutParams.width, layoutParams.height) /2;
//          拿出必要参数
            String finalPrice = dataBean.getFinalPrise();
            long couponAmount = dataBean.getCouponAmount();
            float resultPrise = Float.parseFloat(finalPrice )- couponAmount;
            Context context = itemView.getContext();
//            logUtils.d(this,"Url______________"+dataBean.getPict_url());
            //设置数据
            title.setText(dataBean.getTitle());
            logUtils.d(this,"图片路径+++++"+UrlUtils.getCoverPath(dataBean.getCover()));
            Glide.with(context).load(UrlUtils.getCoverPath(dataBean.getCover()))
                    .into(cover);
            offPriseTv.setText(String.format
                    (context.getString(R.string.text_goods_off_prise),couponAmount));
            finalPriseTv.setText(String.format
                    (Locale.CHINA,"%.2f",resultPrise));
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriseTv.setText(String.format
                    (context.getString(R.string.text_goods_original_prise),finalPrice));
            sellCountTv.setText(String.format
                    (context.getString(R.string.text_goods_sell_count),dataBean.getVolume()));
        }
    }

    /**
     * 该方法将接口暴露出来允许外部给接口逻辑
     * @param listener
     */
    public void setOnListItemClickListener(OnListItemClickListener listener){
        this.mItemClickListener = listener;
    }


    public interface OnListItemClickListener{
        void onItemClick(IBaseInfo item);
    }
}
