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

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Calculator20180403
 * Created by Long on 2018/4/3.
 * 修改时间：2018/4/3 19:01
 */
public class ReportListAdapter extends  RecyclerView.Adapter<ReportListAdapter.ReportListViewHolder>   {
    private Context mContext;
    private List<Report> list=new ArrayList<>();
    private OnItemClickListener itemClickListener;
    @NonNull
    @Override
    public ReportListViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity_main_fragment, parent, false);
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
                    itemClickListener.onItemClick(v,list.get(position), position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemClickListener!=null) {
                    itemClickListener.onLongItemClick(v,list.get(position), position);
                }
                return true;
            }
        });
        // todo pk
        holder.report_pk.setText(String.valueOf(report.getPKId()));
        holder.report_no.setText("项目"+(position+1));
        holder.report_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(report.getDate()));
        if (report.getSubmit()) {
            holder.report_status.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.report_status.setText("已提交");
        }else{
            holder.report_status.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.report_status.setText("未提交");
        }
        holder.report_opName.setText(report.getOpName()==null?"操作员":report.getOpName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ReportListAdapter(List<Report> list) {
        this.list = list;
    }

    //点击事件接口

    public interface OnItemClickListener{
        /**
         * @param view
         * @param report
         * @param position
         * 实现点击
         */
        void onItemClick(View view,Report report, int position);

        /**
         * @param view
         * @param report
         * @param position
         * 实现长点击
         */
        void onLongItemClick(View view,Report report, int position);
    }

    //设置点击事件的方法

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    class ReportListViewHolder extends RecyclerView.ViewHolder  {
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
