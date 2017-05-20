package me.tintoll.config;


public enum Section {

    HOME("home"),
    POST("post"),
    CATEGORY("category");

    private String value;

    Section(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
