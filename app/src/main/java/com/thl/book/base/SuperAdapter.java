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
 * 多布局的Adapter
 * ---------------
 * 复杂数据类型，使用LayoutWrapper包装
 */
public class SuperAdapter extends RecyclerView.Adapter<SuperViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<LayoutWrapper> items = new ArrayList<>();
    private Map<Integer, Integer> layoutMap = new HashMap<>();

    public SuperAdapter(Context context, int[] layoutIds) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        for (int i = 0; i < layoutIds.length; i++) {
            layoutMap.put(i, layoutIds[i]);
        }
    }

    public Context getContext() {
        return context;
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuperViewHolder(inflater.inflate(getLayoutId(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        LayoutWrapper wrapper = items.get(position);
        wrapper.getHolder().bind(context, holder, wrapper.getData(), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //LayoutId转ViewType
    public int getViewType(int position) {
        LayoutWrapper wrapper = items.get(position);
        Iterator iter = layoutMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            Integer val = (Integer) entry.getValue();
            if (val == wrapper.getLayoutId()) {
                return key;
            }
        }
        return 0;
    }

    //ViewType转LayoutId
    public int getLayoutId(int viewType) {
        return layoutMap.get(viewType);
    }

    //设置数据源
    public void setData(List<LayoutWrapper> list) {
        this.items = list;
        notifyDataSetChanged();
    }
}
