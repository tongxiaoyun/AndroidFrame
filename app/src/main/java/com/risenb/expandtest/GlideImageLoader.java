package com.risenb.expandtest;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.risenb.expand.imagepick.PhotoPickerImageLoader;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/5/9
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GlideImageLoader extends PhotoPickerImageLoader {
    @Override
    public ImageView onCreateGridItemView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    @Override
    public void loadGridItemView(ImageView view, String imagePath, final int tag, int width, int height) {

        Glide.with(view.getContext())
                .load("file://" + imagePath)
                .placeholder(getDefaultPlaceHolder())
                .error(getDefaultPlaceHolder())
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(false)
                .centerCrop()
                .crossFade()
                .into(new ImageViewTarget<GlideDrawable>(view) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        view.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        view.setTag(tag, request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) view.getTag(tag);
                    }
                });
    }

    @Override
    public void resumeRequests(Context mCxt, int tag) {
        Glide.with(mCxt).resumeRequests();
    }

    @Override
    public void pauseRequests(Context mCxt, int tag) {
        Glide.with(mCxt).pauseRequests();
    }

    @Override
    public PhotoView onCreatePreviewItemView(Context context) {
        PhotoView photoView = new PhotoView(context);
        return photoView;
    }

    @Override
    public void loadPreviewItemView(ImageView view, String imagePath, int width, int height) {
        if (!imagePath.startsWith("http")) {
            Glide.with(view.getContext())
                    .load(Uri.fromFile(new File(imagePath)))
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                    .skipMemoryCache(true)
                    .crossFade()
                    .into(view);

        } else {
            Glide.with(view.getContext())
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                    .skipMemoryCache(true)
                    .crossFade()
                    .into(view);
        }

    }

}
