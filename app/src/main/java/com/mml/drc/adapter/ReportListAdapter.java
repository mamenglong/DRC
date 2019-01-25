package com.mml.drc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mml.drc.Model.Report;
import com.mml.drc.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 项目名称：Calculator20180403
 * Created by Long on 2018/4/3.
 * 修改时间：2018/4/3 19:01
 */
public class ReportListAdapter extends  RecyclerView.Adapter<ReportListAdapter.ReportListViewHolder>implements View.OnClickListener   {
    private Context mContext;
    private List<Report> list;
    private OnItemClickListener itemClickListener;
    @NonNull
    @Override
    public ReportListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity_main_fragment, parent, false);
        view.setOnClickListener(this);
        ReportListViewHolder holder = new ReportListViewHolder(view);
        return holder;
    }

    @Override
    public void onViewRecycled(@NonNull ReportListViewHolder holder) {
        //销毁之前销毁监听器
        super.onViewRecycled(holder);
    }


    @Override
    public void onBindViewHolder(final ReportListViewHolder holder, final int position) {
        Log.e("adapter", "onBindViewHolder: "+position);
        Report report=list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null) {
                    itemClickListener.onItemClick(list.get(position), position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemClickListener!=null) {
                    itemClickListener.onLongItemClick(list.get(position), position);
                }
                return true;
            }
        });
        // todo pk
        holder.report_pk.setText("a");
        holder.report_no.setText("项目"+(position+1));
        holder.report_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(report.getDate()));
        holder.report_status.setText(report.getSubmit().toString());
        holder.report_opName.setText(report.getOpName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
    }


    public ReportListAdapter(List<Report> list) {
        this.list = list;
    }

    //点击事件接口

    public interface OnItemClickListener{
        void onItemClick(Report report, int position);
        void onLongItemClick(Report report, int position);
    }
    //设置点击事件的方法

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public class ReportListViewHolder extends RecyclerView.ViewHolder  {
        private TextView report_pk,report_no,report_date,report_status,report_opName;
        private LinearLayout item_report;
        public ReportListViewHolder(View itemView) {
            super(itemView);
            report_pk=itemView.findViewById(R.id.report_pk);
            report_no=itemView.findViewById(R.id.report_no);
            report_date=itemView.findViewById(R.id.report_date);
            report_status=itemView.findViewById(R.id.report_status);
            report_opName=itemView.findViewById(R.id.report_opName);
        }
    }


}
