package com.mml.drc.Model;

import android.util.Log;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报告
 * Created by 11324 on 2019/1/25
 */
public class Report extends LitePalSupport {
    private Date date;//日期

    private String opName;//操作员
    private List<ReportItem> reportItems = new ArrayList<>(); //测量值 图片路径
    private Boolean isSubmit = false; //是否提交至云端

    public Report() {
        addItem();
    }

    /**
     * 检查图片和值
     *
     * @return 报告是否完整
     */
    public boolean check() {
        for (ReportItem item : reportItems) {
            if (item.getImagePath() == null || item.getMeasurementValue() == null)
                return false;
        }
        return true;
    }

    public void addItem() {
        reportItems.add(new ReportItem());
        Log.d("11324 :", "addItem 添加 ----> " + reportItems.size());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public List<ReportItem> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }
}