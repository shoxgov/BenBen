package com.benben.bb.bean;

public class SettingItem {
    private int iconResId;
    private String title;
    private String content;

    public SettingItem(int iconResId, String title, String content) {
        this.iconResId = iconResId;
        this.title = title;
        this.content = content;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
