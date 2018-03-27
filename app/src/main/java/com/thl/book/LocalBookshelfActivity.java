package com.thl.book;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiajunhui.xapp.medialoader.MediaLoader;
import com.jiajunhui.xapp.medialoader.bean.FileItem;
import com.jiajunhui.xapp.medialoader.bean.FileResult;
import com.jiajunhui.xapp.medialoader.bean.FileType;
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.thl.book.base.BaseActivity;
import com.thl.book.base.SingleAdapter;
import com.thl.book.base.SuperViewHolder;
import com.thl.reader.ReadActivity;
import com.thl.reader.db.BookList;
import com.thl.reader.filechooser.FileChooserActivity;
import com.thl.reader.util.FileUtils;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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


        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ProgressLayout headerView = new ProgressLayout(this);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);
        refreshLayout.setOverScrollRefreshShow(true);
        ib_more = findViewById(R.id.ib_more);
        ib_more.setVisibility(View.VISIBLE);
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
//                            isDel = !isDel;
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
        initFristData();

    }

    private void initFristData() {
        boolean initFristData = SharedPreferencesUtils.getBoolean(this, "initFristData", true);
        if (true) {
            MediaLoader.getLoader().loadFiles(LocalBookshelfActivity.this, new OnFileLoaderCallBack(FileType.DOC) {
                @Override
                public void onResult(FileResult result) {
                    SharedPreferencesUtils.saveBoolean(LocalBookshelfActivity.this,"initFristData",false);
                    if (result!=null&&result.getItems()!=null&&result.getItems().size()>0){
                        List<BookList> bookLists = new ArrayList<BookList>();
                        for (FileItem item : result.getItems()) {

                            if (item.getMime().equals("text/plain")) {
                                Log.e("taohiali",item.getMime());
                                Log.e("taohiali",item.getDisplayName());
                                Log.e("taohiali",item.getPath());
                                BookList bookList = new BookList();
//                                String bookName = FileUtils.getFileName(item.getPath());
                                bookList.setBookname(item.getDisplayName());
                                bookList.setBookpath(item.getPath());
                                bookLists.add(bookList);
                            }
                        }
                        SaveBookToSqlLiteTask mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
                        mSaveBookToSqlLiteTask.execute(bookLists);
                    }
                }
            });
        }else{
            bookLists.clear();
            bookLists.addAll(getBooks());
            adapter.notifyDataSetChanged();
        }
    }

    private List<BookList> getBooks() {
        List<BookList> all = DataSupport.findAll(BookList.class);
        return all;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_book:

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Intent intent = new Intent(LocalBookshelfActivity.this, FileChooserActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissins(new PermissionUtils.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent(LocalBookshelfActivity.this, FileChooserActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onPermissionDenied(String[] deniedPermissions) {

                        }
                    });
                }
                popWindow.dissmiss();
                break;

            case R.id.ib_more:
                View view = LayoutInflater.from(this).inflate(R.layout.view_popu2, null);
                view.findViewById(R.id.tv_edit).setOnClickListener(this);
                view.findViewById(R.id.tv_about).setOnClickListener(this);
                view.findViewById(R.id.tv_share).setOnClickListener(this);
                view.findViewById(R.id.add_book).setOnClickListener(this);
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
                Intent intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
                popWindow.dissmiss();
                break;
            case R.id.tv_share:
                Intent shareTextIntent = new Intent(Intent.ACTION_SEND);
                shareTextIntent.setType("text/plain");
                shareTextIntent.putExtra(Intent.EXTRA_TEXT, "\"https://www.coolapk.com/apk/com.thl.book\"");
                shareTextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(shareTextIntent);
                popWindow.dissmiss();
                break;
        }
    }


    private void requestPermissins(PermissionUtils.OnPermissionListener listener) {
        String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        PermissionUtils.requestPermissions(this, 0
                , permissions, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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


    private class SaveBookToSqlLiteTask extends AsyncTask<List<BookList>, Void, Integer> {
        private static final int FAIL = 0;
        private static final int SUCCESS = 1;
        private static final int REPEAT = 2;
        private BookList repeatBookList;

        @Override
        protected Integer doInBackground(List<BookList>... params) {
            List<BookList> bookLists = params[0];
            for (BookList bookList : bookLists) {
                List<BookList> books = DataSupport.where("bookpath = ?", bookList.getBookpath()).find(BookList.class);
                String bookpath = bookList.getBookpath();
                InputStreamReader reader = null; // 建立一个输入流对象reader
                try {
                    File filename = new File(bookpath); // 要读取以上路径的input。txt文件
                    String charset = FileUtils.getCharset(bookpath);
                    reader = new InputStreamReader(new FileInputStream(filename),charset);
                    BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
                    String line = "";
                    StringBuffer buf = new StringBuffer();
                    line = br.readLine();
                    buf.append(line);
                    for (int i = 0; i < 6; i++) {
                        if (line != null) {
                            line = br.readLine(); // 一次读入一行数据
                            buf.append(line);
                        }
                    }

                    String msg = buf.toString();
                    Log.e("taohaili",msg);
                    bookList.setMsg(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (books.size() > 0) {
                    repeatBookList = bookList;
                    return REPEAT;
                }
            }
            try {
                DataSupport.saveAll(bookLists);
            } catch (Exception e) {
                e.printStackTrace();
                return FAIL;
            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            bookLists.clear();
            bookLists.addAll(getBooks());
            adapter.notifyDataSetChanged();
        }
    }

}


