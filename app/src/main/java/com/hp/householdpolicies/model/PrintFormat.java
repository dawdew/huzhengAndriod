package com.hp.householdpolicies.model;

import java.util.List;

public class PrintFormat {

    public static int NUMBER = 0;
    public static int PEOPLECOUNT = 2;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * iNums : 1
         * alignmen : 1
         * changerow : 0
         * text : 【XXXXX 支行】
         * feedline : 3
         * bold : 1
         * sizetext : 1
         * cutpaper : 0
         */

        private String iNums;
        private String alignmen;
        private String changerow;
        private String text;
        private String feedline;
        private String bold;
        private String sizetext;
        private String cutpaper;
        private String qr;

        public String getiNums() {
            return iNums;
        }

        public String getQr() {
            return qr;
        }

        public DataBean setQr(String qr) {
            this.qr = qr;
            return this;
        }

        public void setiNums(String iNums) {
            this.iNums = iNums;
        }

        public String getINums() {
            return iNums;
        }

        public DataBean setINums(String iNums) {
            this.iNums = iNums;
            return this;
        }

        public String getAlignmen() {
            return alignmen;
        }

        public DataBean setAlignmen(String alignmen) {
            this.alignmen = alignmen;
            return this;
        }

        public String getChangerow() {
            return changerow;
        }

        public DataBean setChangerow(String changerow) {
            this.changerow = changerow;
            return this;
        }

        public String getText() {
            return text;
        }

        public DataBean setText(String text) {
            this.text = text;
            return this;
        }

        public String getFeedline() {
            return feedline;
        }

        public DataBean setFeedline(String feedline) {
            this.feedline = feedline;
            return this;
        }

        public String getBold() {
            return bold;
        }

        public DataBean setBold(String bold) {
            this.bold = bold;
            return this;
        }

        public String getSizetext() {
            return sizetext;
        }

        public DataBean setSizetext(String sizetext) {
            this.sizetext = sizetext;
            return this;
        }

        public String getCutpaper() {
            return cutpaper;
        }

        public DataBean setCutpaper(String cutpaper) {
            this.cutpaper = cutpaper;
            return this;
        }
    }
}
