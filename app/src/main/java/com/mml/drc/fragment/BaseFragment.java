package com.mml.drc.fragment;

/**
 * 项目名称：Base
 * Created by Long on 2018/11/15.
 * 修改时间：2018/11/15 14:31
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mml.drc.Model.Report;
import com.mml.drc.R;
import com.mml.drc.adapter.ReportListAdapter;
import com.mml.drc.application.MyApplication;
import com.mml.drc.utils.LogUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {

    public static BaseFragment newInstance(Boolean isSubmit) {
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();
        args.putBoolean("isSubmit", isSubmit);
        fragment.setArguments(args);

        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_fragment, null);
        Boolean isSubmit=getArguments().getBoolean("isSubmit");
        bindData(view,isSubmit);
        return view;

    }
    public void bindData(View view,Boolean isSubmit){
        List<Report> reportList=new ArrayList<>();
        //todo 区分状态
        if(isSubmit){
            //已提交
            reportList=LitePal.where("isSubmit=?","1").find(Report.class);
        }else {
            //未提交
            reportList=LitePal.where("isSubmit=?","0").find(Report.class);
        }
       // reportList=LitePal.findAll(Report.class);

        RecyclerView recyclerView=view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyApplication.getApplication().getMainActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator (new DefaultItemAnimator());
        ReportListAdapter adapter=new ReportListAdapter(reportList);
        LogUtils.d("提交状态："+isSubmit+":list数目:"+reportList.size());
        recyclerView.setAdapter(adapter);
    }

}
