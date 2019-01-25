package com.mml.drc.Model;

import org.litepal.crud.LitePalSupport;

/**
 * Created by 11324 on 2019/1/25
 */
@Deprecated
public class ReportItem extends LitePalSupport {
    private Report report;

    private Double measurementValue = null;//测量值
    private String imagePath = null;//测量图路径

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Double measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
