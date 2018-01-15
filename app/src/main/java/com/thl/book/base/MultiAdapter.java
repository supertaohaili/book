package com.thl.book.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 多布局Adapter
 * ---------------
 * 单一数据类型
 */
public abstract class MultiAdapter<T> extends RecyclerView.Adapter<SuperViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<T> items = new ArrayList<>();
    private Map<Integer, Integer> layoutMap = new HashMap<>();

    public MultiAdapter(Context context, int[] layoutIds) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        for (int i = 0; i < layoutIds.length; i++) {
            layoutMap.put(i, layoutIds[i]);
        }
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuperViewHolder(inflater.inflate(getLayoutId(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        bindData(context, holder, items.get(position), bindLayout(items.get(position), position), position);
    }

    @Override
    public int getItemViewType(int position) {
        int layoutId = bindLayout(items.get(position), position);
        return getViewType(layoutId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //根据ViewType获取LayoutId
    public int getLayoutId(int viewType) {
        return layoutMap.get(viewType);
    }

    //根据LayoutId获取ViewType
    public int getViewType(int layoutId) {
        Iterator iter = layoutMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            Integer val = (Integer) entry.getValue();
            if (val == layoutId) {
                return key;
            }
        }
        return 0;
    }

    public Context getContext() {
        return context;
    }

    /*设置数据源*/
    public void setData(List<T> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    /*获取每个Item的ViewType*/
    public abstract int bindLayout(T item, int position);

    /*绑定数据源*/
    public abstract void bindData(Context context, SuperViewHolder holder, T item, int layoutId, int position);

}
