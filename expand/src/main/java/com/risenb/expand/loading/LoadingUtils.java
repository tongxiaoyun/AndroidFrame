package com.risenb.expand.loading;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.expand.R;
import com.risenb.expand.loading.listener.ReloadListener;
import com.risenb.expand.m;

/**
 * ================================================
 * 作    者：faf
 * 版    本：1.0
 * 创建日期：2017/3/6
 * 描    述：加载帮助类
 * 修订历史：
 * ================================================
 */
public class LoadingUtils implements View.OnClickListener {

    private boolean isShowHead = false;

    private boolean isShowFoot = false;

    private int customHeight = 0;

    private ViewGroup decorView;

    private ViewGroup rootView;

    private Context mContext;

    private boolean isShowing;

    private ReloadListener reloadListener;
    /**
     * 加载为空的布局
     */
    private RelativeLayout loading_empty;
    /**
     * 加载错误的布局
     */
    private RelativeLayout loading_error;
    /**
     * 加载中的你布局
     */
    private RelativeLayout loading_load;

    /**
     * 初始化界面
     */
    public void init(Context mContext) {
        this.mContext = mContext;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        decorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.loading_parent, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) rootView.getLayoutParams();
        int marginsTop = isShowHead ? (customHeight == 0 ? m.getInstance().getLoadingM().getMarginsTop() : customHeight) : 0;
        int marginsBottom = isShowFoot ? m.getInstance().getLoadingM().getMarginsBottom() : 0;
        p.setMargins(0, marginsTop, 0, marginsBottom);
        loading_empty = (RelativeLayout) layoutInflater.inflate(R.layout.loading_empty, rootView, false);
        loading_error = (RelativeLayout) layoutInflater.inflate(R.layout.loading_error, rootView, false);
        loading_load = (RelativeLayout) layoutInflater.inflate(R.layout.loading_load, rootView, false);
        rootView.addView(loading_empty);
        rootView.addView(loading_error);
        rootView.addView(loading_load);
    }

    /**
     * fragment初始化界面
     */
    public void init(Context mContext, ViewGroup decorView) {
        this.mContext = mContext;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        this.decorView = decorView;
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.loading_parent, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) rootView.getLayoutParams();
        int marginsTop = isShowHead ? (customHeight == 0 ? m.getInstance().getLoadingM().getMarginsTop() : customHeight) : 0;
        int marginsBottom = isShowFoot ? m.getInstance().getLoadingM().getMarginsBottom() : 0;
        p.setMargins(0, marginsTop, 0, marginsBottom);
        loading_empty = (RelativeLayout) layoutInflater.inflate(R.layout.loading_empty, rootView, false);
        loading_error = (RelativeLayout) layoutInflater.inflate(R.layout.loading_error, rootView, false);
        loading_load = (RelativeLayout) layoutInflater.inflate(R.layout.loading_load, rootView, false);
        TextView tv_loading_error_reload = (TextView) loading_error.findViewById(R.id.tv_loading_error_reload);
        tv_loading_error_reload.setOnClickListener(this);
        rootView.addView(loading_empty);
        rootView.addView(loading_error);
        rootView.addView(loading_load);
    }

    /**
     * 必须在init之前
     */
    public void setShowHead() {
        isShowHead = true;
    }


    public void setShowFoot() {
        isShowFoot = true;
    }

    public void setReloadListener(ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    /**
     * 显示加载
     */
    public void show(LoadResult loadResult) {

        if (loadResult == LoadResult.ERROR) {
            //错误的情况
            loading_empty.setVisibility(View.GONE);
            loading_error.setVisibility(View.VISIBLE);
            loading_load.setVisibility(View.GONE);
        } else if (loadResult == LoadResult.EMPTY) {
            //列表为空的情况
            loading_empty.setVisibility(View.VISIBLE);
            loading_error.setVisibility(View.GONE);
            loading_load.setVisibility(View.GONE);
        } else if (loadResult == LoadResult.LOADING) {
            //加载中的情况
            loading_empty.setVisibility(View.GONE);
            loading_error.setVisibility(View.GONE);
            loading_load.setVisibility(View.VISIBLE);
        } else if (loadResult == LoadResult.SUCCESS) {
            //加载完成
            decorView.removeView(rootView);
            isShowing = false;
            return;
        }
        if (rootView.getParent() == null && !isShowing)
            decorView.addView(rootView);
        isShowing = true;


    }

    @Override
    public void onClick(View v) {
        if (reloadListener != null) {
            show(LoadResult.LOADING);
            reloadListener.reload();
        }
    }

    public void setCustomHeight(int customHeight) {
        this.customHeight = customHeight;
    }

    /**
     * 加载状态
     */
    public enum LoadResult {
        ERROR, EMPTY, SUCCESS, LOADING;

    }
}
