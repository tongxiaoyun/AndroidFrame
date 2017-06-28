package com.risenb.expand.xrecyclerview.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/16
 * 描    述：RecyclerView的适配器
 * 修订历史：
 * ================================================
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> list;
    private FragmentActivity activity;
    protected  ViewGroup parent;

    public void setList(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addBean(T bean) {
        if (this.list == null) {
            this.list = new ArrayList();
        }

        this.list.add(bean);
        this.notifyDataSetChanged();
    }

    public ArrayList<T> getList() {
        return (ArrayList) this.list;
    }


    public FragmentActivity getActivity() {
        return this.activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        return loadView(parent.getContext(), viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((BaseViewHolder) holder).prepareData(list == null ? null : list.get(position), position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    protected abstract BaseViewHolder<T> loadView(Context context, int viewType);

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.onItemClickListener = mOnItemClickListener;
    }
}
