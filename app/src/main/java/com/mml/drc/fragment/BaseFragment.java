package com.mml.drc.fragment;

/**
 * 项目名称：Base
 * Created by Long on 2018/11/15.
 * 修改时间：2018/11/15 14:31
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mml.drc.Model.Report;
import com.mml.drc.R;
import com.mml.drc.activities.NewReportActivity;
import com.mml.drc.adapter.ReportListAdapter;
import com.mml.drc.application.MyApplication;
import com.mml.drc.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Long
 */
public class BaseFragment extends Fragment {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    ReportListAdapter adapter;
    List<Report> reportList = new ArrayList<>();
    Boolean isSubmit;
    SwipeRefreshLayout.OnRefreshListener refreshListener= new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            bindData();
            refresh_layout.setRefreshing(false);
        }
    };
    public static BaseFragment newInstance(Boolean isSubmit) {
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();
        args.putBoolean("isSubmit", isSubmit);
        fragment.setArguments(args);

        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_fragment, container,false);
        assert getArguments() != null;
        isSubmit = getArguments().getBoolean("isSubmit");
        ButterKnife.bind(this, view);
        initView();
        bindData();
        LogUtils.d(" onCreateView");
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(" super.onResume();");
        refreshListener.onRefresh();
    }

    public void bindData() {
        //todo 区分状态，litepal中对booolean用0或1
        reportList.clear();
        if (isSubmit) {
            //已提交
            reportList.addAll(LitePal.where("isSubmit=?", "1").find(Report.class));
        } else {
            //未提交
            reportList.addAll(LitePal.where("isSubmit=?", "0").find(Report.class));
        }
        adapter.notifyDataSetChanged();
    }

    public void initView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyApplication.getApplication().getMainActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReportListAdapter(reportList);
        //TODO 点击事件监听
        adapter.setItemClickListener(new ReportListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Report report, int position) {
                LogUtils.d("item点击第" + position + 1 + "个，report id：" + report.getPKId());
                Intent intent = new Intent(getActivity(), NewReportActivity.class);
                intent.putExtra("id", report.getPKId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, final Report report, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("项目提交")
                        .setMessage("是否提交项目" + (position + 1) + "?")
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (report.getSubmit()) {
                                    Toast.makeText(getActivity(), "已经提交，无需重复操作！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "提交完成！", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                LogUtils.d("item长按第" + (position + 1) + "个，report id：" + report.getPKId());
            }
        });
        LogUtils.d("提交状态：" + isSubmit + ":list数目:" + reportList.size());
        recyclerView.setAdapter(adapter);
        refresh_layout.setOnRefreshListener(refreshListener);
    }
}
