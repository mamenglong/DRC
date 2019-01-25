package com.mml.drc.Model;

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
    //private List<ReportItem> reportItems = new ArrayList<>(); //测量值 图片路径
    private Boolean isSubmit = false; //是否提交至云端

    private List<String> photoPaths = new ArrayList<>();
    private List<Double> measurementValue = new ArrayList<>();

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public List<Double> getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(List<Double> measurementValue) {
        this.measurementValue = measurementValue;
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


    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }
}