package com.example.wsr_pool;

public class Dishes {
    private int id;
    private String name;
    private int price;
    private String icon;
    private int category;
    private int version;

    public Dishes(int id, String name, int category, String icon, int price, int version) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.icon = icon;
        this.price = price;
        this.version = version;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(int  _price) {
        this.price = _price;
    }

    public int  getPrice() {
        return this.price;
    }

    public void setIcon(String _icon) {
        this.icon = _icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setVersion(int  _version) {
        this.version = _version;
    }

    public int  getVersion() {
        return this.version;
    }
}
