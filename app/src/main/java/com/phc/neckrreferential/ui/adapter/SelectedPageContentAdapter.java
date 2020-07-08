package com.phc.neckrreferential.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.utils.Constants;
import com.phc.neckrreferential.utils.logUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/7 14
 * 描述：
 */
public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    public List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean>
            mData = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData = mData.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentItemClickListener.onContentItemClick(itemData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode() == Constants.SUCCESS_CODE) {
            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> uatm_tbk_item
                    = content.getData().getTbk_uatm_favorites_item_get_response()
                    .getResults().getUatm_tbk_item();
            this.mData.clear();
            this.mData.addAll(uatm_tbk_item);
            notifyDataSetChanged();
        }
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_cover)
        ImageView cover;
        @BindView(R.id.selected_off_prise)
        TextView offPriseTv;
        @BindView(R.id.selected_title)
        TextView title;
        @BindView(R.id.selected_buy_btn)
        TextView buyBtn;
        @BindView(R.id.selected_original_prise)
        TextView originalPriseTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData) {
            //imageUrl
            String pict_url = itemData.getPict_url();
//            logUtils.d(this,"精选界面图片url==="+pict_url);
            Glide.with(itemView.getContext()).load(pict_url).into(cover);
//            加载文字内容
            title.setText(itemData.getTitle());
            logUtils.d(this, "商品信息===" + itemData.getZk_final_price() + "\n" + itemData.getTitle() + "\n" + itemData.getCoupon_click_url());
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                buyBtn.setVisibility(View.GONE);
                originalPriseTv.setText(R.string.text_no_off);
            } else {
                buyBtn.setVisibility(View.VISIBLE);
                originalPriseTv.setText
                        (String.format(itemView.getContext()
                                        .getString(R.string.text_selected_right_final_price)
                                , itemData.getZk_final_price()));

            }

            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPriseTv.setVisibility(View.GONE);
            } else {
                offPriseTv.setVisibility(View.VISIBLE);
                offPriseTv.setText(itemData.getCoupon_info());
            }

        }
    }


    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener contentItemClickListener) {
        this.mContentItemClickListener = contentItemClickListener;
    }

    public interface OnSelectedPageContentItemClickListener {
        void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item);
    }

}
