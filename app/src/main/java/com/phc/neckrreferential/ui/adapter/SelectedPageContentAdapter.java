package com.phc.neckrreferential.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/7 14
 * 描述：
 */
public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.innerHoledr> {

    public List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean>
            mData = new ArrayList<>();

    @NonNull
    @Override
    public innerHoledr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull innerHoledr holder, int position) {

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

    public class innerHoledr extends RecyclerView.ViewHolder {

        public innerHoledr(@NonNull View itemView) {
            super(itemView);
        }
    }
}
