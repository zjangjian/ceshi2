package com.example.jianzhang.myapplication33;

/**
 * Author: AlexSai
 * Time: 2017/11/3 14:49
 * Introduction:
 */

public class CeshiItem {

    private int gtId;

    private String gtName;
    private String gtAddressText;
    private float gtAddressJd;

    @Override
    public String toString() {
        return "CeshiItem{" +
                "gtId=" + gtId +
                ", gtName='" + gtName + '\'' +
                ", gtAddressText='" + gtAddressText + '\'' +
                ", gtAddressJd=" + gtAddressJd +
                '}';
    }

    public int getGtId() {
        return gtId;
    }

    public void setGtId(int gtId) {
        this.gtId = gtId;
    }

    public String getGtName() {
        return gtName;
    }

    public void setGtName(String gtName) {
        this.gtName = gtName;
    }

    public String getGtAddressText() {
        return gtAddressText;
    }

    public void setGtAddressText(String gtAddressText) {
        this.gtAddressText = gtAddressText;
    }

    public float getGtAddressJd() {
        return gtAddressJd;
    }

    public void setGtAddressJd(float gtAddressJd) {
        this.gtAddressJd = gtAddressJd;
    }
}
