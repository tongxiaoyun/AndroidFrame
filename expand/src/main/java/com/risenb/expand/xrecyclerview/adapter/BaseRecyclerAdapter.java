package com.risenb.expand.xrecyclerview.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.risenb.expand.xrecyclerview.bean.BaseFootBean;
import com.risenb.expand.xrecyclerview.bean.BaseHeadBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/16
 * 描    述：RecyclerView的适配器
 * 修订历史：2017/7/14 添加头部和底部视图 目前只支持一个的添加
 * ================================================
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> list;
    private FragmentActivity activity;
    protected ViewGroup parent;

    private BaseHeadBean baseHeadBean;

    private BaseFootBean baseFootBean;

    public BaseFootBean getBaseFootBean() {
        return baseFootBean;
    }

    public void setBaseFootBean(BaseFootBean baseFootBean) {
        this.baseFootBean = baseFootBean;
    }

    public BaseHeadBean getBaseHeadBean() {
        return baseHeadBean;
    }

    public void setBaseHeadBean(BaseHeadBean baseHeadBean) {
        this.baseHeadBean = baseHeadBean;
    }

    /**
     * item类型
     */
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    /**
     * 头部View个数
     */
    private int mHeaderCount = 0;
    /**
     * 底部View个数
     */
    private int mBottomCount = 0;

    private View headView;


    private View bottomView;

    public int getmBottomCount() {
        return mBottomCount;
    }

    public void setmBottomCount(int mBottomCount) {
        this.mBottomCount = mBottomCount;
    }

    public int getmHeaderCount() {
        return mHeaderCount;
    }

    public void setmHeaderCount(int mHeaderCount) {
        this.mHeaderCount = mHeaderCount;
    }

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

    /**
     * 判断当前item类型
     */
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
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


        if (getItemViewType(position) == ITEM_TYPE_CONTENT) {
            ((BaseViewHolder) holder).prepareData(list == null ? null : list.get(position - mHeaderCount), position - mHeaderCount);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView, position - mHeaderCount);
                    }
                });
            }
        } else if (getItemViewType(position) == ITEM_TYPE_HEADER) {
            ((BaseViewHolder) holder).initHead(baseHeadBean);
        } else if (getItemViewType(position) == ITEM_TYPE_BOTTOM) {
            ((BaseViewHolder) holder).initFoot(baseFootBean);
        }
    }


    @Override
    public int getItemCount() {
        return mHeaderCount + getCount() + mBottomCount;
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    protected abstract BaseViewHolder<T> loadView(Context context, int viewType);


    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.onItemClickListener = mOnItemClickListener;
    }
}
