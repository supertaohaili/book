package com.thl.book;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.thl.book.base.BaseActivity;
import com.thl.book.base.SingleAdapter;
import com.thl.book.base.SuperViewHolder;
import com.thl.reader.ReadActivity;
import com.thl.reader.db.BookList;
import com.thl.reader.filechooser.FileChooserActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class LocalBookshelfActivity extends BaseActivity implements View.OnClickListener {

    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;

    private SingleAdapter<BookList> adapter;
    private List<BookList> bookLists;
    private View ib_more;

    private CustomPopWindow popWindow;
    private boolean isDel = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_book;
    }


    @Override
    protected void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("本地书架");
        View fad = findViewById(R.id.fab);
        fad.setOnClickListener(this);

        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ProgressLayout headerView = new ProgressLayout(this);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);
        refreshLayout.setOverScrollRefreshShow(true);
        ib_more = findViewById(R.id.ib_more);
        ib_more.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                bookLists.clear();
                bookLists.addAll(getBooks());
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SingleAdapter<BookList>(LocalBookshelfActivity.this, R.layout.item_book) {
            @Override
            protected void bindData(SuperViewHolder holder, BookList book) {
                LinearLayout lltDel = holder.getView(R.id.llt_del);
                if (isDel) {
                    lltDel.setVisibility(View.VISIBLE);
                    lltDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataSupport.delete(BookList.class, book.getId());
                            bookLists.clear();
                            bookLists.addAll(getBooks());
                            isDel = !isDel;
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    lltDel.setVisibility(View.GONE);
                }
                TextView tvName = holder.getView(R.id.tv_name);
                TextView tvMsg = holder.getView(R.id.tv_msg);
                tvName.setText(book.getBookname());
                tvMsg.setText(book.getMsg());
                holder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReadActivity.openBook(book, LocalBookshelfActivity.this);
                    }
                });
            }
        };

        bookLists = new ArrayList<>();
        adapter.setData(bookLists);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookLists.clear();
        bookLists.addAll(getBooks());
        adapter.notifyDataSetChanged();
    }

    private List<BookList> getBooks() {
        List<BookList> all = DataSupport.findAll(BookList.class);
        return all;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fab:
                intent = new Intent(this, FileChooserActivity.class);
                this.startActivity(intent);
                break;

            case R.id.ib_more:
                View view = LayoutInflater.from(this).inflate(R.layout.view_popu, null);
                view.findViewById(R.id.tv_edit).setOnClickListener(this);
                view.findViewById(R.id.tv_about).setOnClickListener(this);
                if (popWindow == null) {
                    popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                            .setView(view)
                            .enableBackgroundDark(false)
                            .setFocusable(true)
                            .setOutsideTouchable(true)
                            .create();
                }
                popWindow.showAsDropDown(ib_more, 0, 0);
                break;

            case R.id.tv_edit:
                isDel = !isDel;
                adapter.notifyDataSetChanged();
                popWindow.dissmiss();
                break;

            case R.id.tv_about:
                intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
                popWindow.dissmiss();
                break;
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isDel) {
            isDel = !isDel;
            adapter.notifyDataSetChanged();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(this, "再次点击返回确认退出", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}


