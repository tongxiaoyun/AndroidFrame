package com.risenb.expand.imagepick.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.risenb.expand.R;
import com.risenb.expand.xrecyclerview.adapter.BaseRecyclerAdapter;
import com.risenb.expand.xrecyclerview.adapter.BaseViewHolder;
import com.risenb.expand.xrecyclerview.bean.BaseFootBean;
import com.risenb.expand.xrecyclerview.bean.BaseHeadBean;

import java.io.File;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/8/9
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImageAdapter<T extends String> extends BaseRecyclerAdapter {

    private ClickImageBack clickImageBack;

    public void setClickImageBack(ClickImageBack clickImageBack) {
        this.clickImageBack = clickImageBack;
    }

    @Override
    protected BaseViewHolder loadView(Context context, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
    }

    private class ViewHolder extends BaseViewHolder<T> {


        private ImageView iv_item_image_image;
        private ImageView iv_item_image_del;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void prepareData() {
            if (!bean.startsWith("http")) {
                Glide.with(getActivity())
                        .load(Uri.fromFile(new File(bean)))
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.default_error)
                        .placeholder(R.drawable.default_error)
                        .into(iv_item_image_image);

            } else {
                Glide.with(getActivity())
                        .load(iv_item_image_image)
                        .error(R.drawable.default_error)
                        .placeholder(R.drawable.default_error)
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                        .skipMemoryCache(true)
                        .crossFade()
                        .into(iv_item_image_image);
            }
            iv_item_image_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickImageBack.delete(position);
                }
            });

            iv_item_image_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickImageBack.review(position);
                }
            });
        }

        @Override
        protected void initHead(BaseHeadBean bean) {

        }

        @Override
        protected void initFoot(BaseFootBean bean) {

        }

        @Override
        protected void reflectionView(View v) {
            iv_item_image_image = (ImageView) v.findViewById(R.id.iv_item_image_image);
            iv_item_image_del = (ImageView) v.findViewById(R.id.iv_item_image_del);
        }
    }

    public interface ClickImageBack {
        void delete(int position);

        void review(int position);
    }
}
