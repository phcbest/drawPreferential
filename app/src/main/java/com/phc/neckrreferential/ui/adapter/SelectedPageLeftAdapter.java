package com.phc.neckrreferential.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.phc.neckrreferential.R;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/7 06
 * 描述：
 */
public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelectedPageCategory.DataBean> mData = new ArrayList<>();
    private int mCurrentSelectedPosition = 0;
    private OnLeftItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedPageCategory.DataBean dataBean = mData.get(position);
        TextView itemTv = holder.itemTv;
        if (mCurrentSelectedPosition == position) {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorEFEEEE, null));
        } else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white, null));
        }
        itemTv.setText(dataBean.getFavorites_title());
        itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null && mCurrentSelectedPosition != position) {
                    mCurrentSelectedPosition = position;
                    mItemClickListener.onLeftItemClick(dataBean);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataBean> data = categories.getData();
        if (data != null) {
            this.mData.clear();
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            mItemClickListener.onLeftItemClick(mData.get(mCurrentSelectedPosition));
        }
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        TextView itemTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            itemTv = (TextView) itemView.findViewById(R.id.left_category_tv);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelectedPageCategory.DataBean item);
    }
}
