package com.grocafast.grocafast;

public class DataList {

    String name;
    String Delete="Delete";
    public DataList(String itemName) {
        this.name = itemName;this.Delete="Delete";
    }

    public String getName() {
        return name;
    }
    public String getDelete() {
        return Delete;
    }
}
