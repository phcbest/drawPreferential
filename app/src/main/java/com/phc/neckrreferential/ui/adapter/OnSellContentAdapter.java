package com.phc.neckrreferential.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phc.neckrreferential.R;
import com.phc.neckrreferential.modle.domain.IBaseInfo;
import com.phc.neckrreferential.modle.domain.OnSellContent;
import com.phc.neckrreferential.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/7/9 14
 * 描述：
 */
public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHolder> {


    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();

    public OnSellPageItemClickListener listener = null;



    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mDataBean = mData.get(position);
        holder.setData(mDataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里需要调用接口的逻辑
                listener.onSellItemClick(mDataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        this.mData.clear();
        this.mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    /**
     * 加载更多的内容
     * @param moreResult
     */
    public void onMoreLoaded(OnSellContent moreResult) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData
                = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        //拿到添加之前的数据长度
        int oldPosition = this.mData.size() - 1;
        this.mData.addAll(moreData);
        notifyItemRangeChanged(oldPosition,moreData.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.on_sell_cover)
        ImageView cover;
        @BindView(R.id.on_sell_content_title_tv)
        TextView titleTv;
        @BindView(R.id.on_sell_origin_prise_tv)
        TextView originalPriseTv;
        @BindView(R.id.on_sell_off_prise_tv)
        TextView offPriseTv;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data) {
            String original = data.getZk_final_price();

            titleTv.setText(data.getTitle());
//            logUtils.d(this,"特惠界面图片链接"+data.getPict_url());
            Glide.with(cover.getContext()).load(UrlUtils.getCoverPath(data.getPict_url())).into(cover);
            originalPriseTv.setText(String.format(originalPriseTv.getContext()
                    .getString(R.string.text_goods_original_prise_on_sell),original));
            //给原价添加中划线
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            float originalFloat = Float.parseFloat(original);
            float finalFloat = originalFloat - data.getCoupon_amount();
            offPriseTv.setText(String.format(offPriseTv.getContext()
                    .getString(R.string.text_on_sell_off_end),finalFloat));
        }
    }


    public void setOnSellPageItemClickListener(OnSellPageItemClickListener listener) {
        this.listener = listener;
    }

    public interface  OnSellPageItemClickListener{
        void onSellItemClick(IBaseInfo data);
    }
}
