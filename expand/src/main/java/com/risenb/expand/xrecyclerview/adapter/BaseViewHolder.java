package com.risenb.expand.xrecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.risenb.expand.xrecyclerview.bean.BaseFootBean;
import com.risenb.expand.xrecyclerview.bean.BaseHeadBean;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/16
 * 描    述： ViewHold
 * 修订历史：
 * ================================================
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    protected View convertView;
    protected T bean;
    protected int position;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.convertView = itemView;
        reflectionView(itemView);
    }

    protected abstract void prepareData();

    protected abstract void initHead(BaseHeadBean bean);

    protected abstract void initFoot(BaseFootBean bean);


    protected abstract void reflectionView(View v);

    protected View findViewById(int viewId) {
        return this.convertView.findViewById(viewId);
    }

    public final View getView() {
        return this.convertView;
    }


    public void prepareData(T bean, int position) {
        this.bean = bean;
        this.position = position;
        this.prepareData();
    }


}
