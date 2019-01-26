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
    public static final int MAX_=0;

    private Date date;//日期

    private String opName;//操作员
    //private List<ReportItem> reportItems = new ArrayList<>(); //测量值 图片路径
    private Boolean isSubmit = false; //是否提交至云端

    private List<String> photoPaths = new ArrayList<>();
    private List<String> measurementValue = new ArrayList<>();

    public Report() {
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public List<String> getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(List<String> measurementValue) {
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

    @Override
    public boolean save() {
        trimData();
        return super.save();
    }

    public void fill() {
        for (List<String> l : new List[]{photoPaths, measurementValue}) {
            int j = 3 - l.size();
            for (int a = 0; a < j; a++) l.add(null);
        }
    }

    /**
     * 剪裁数据
     */
    public void trimData() {
        for (List<String> l : new List[]{photoPaths, measurementValue}) {
            List<Integer> removes = new ArrayList<>();
            int i = 0;
            for (String s : l) {
                if (s == null || s.length() == 0) {
                    removes.add(i);
                }
                i++;
            }
            i = 0;
            for (int p : removes) {
                l.remove(p - i);
                i++;
            }
        }
    }

    /**
     * 更新数据库
     *
     * @return
     */
    public int update() {
        trimData();
        return super.update(getBaseObjId());
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }

    public long getPKId() {
        return getBaseObjId();
    }
}
