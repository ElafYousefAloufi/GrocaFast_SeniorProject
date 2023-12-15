package com.grocafast.grocafast;

public class Items {
    String itemName, category, aisleNumber;
    int x=0,y=0;

    //constructor
    public Items() {
        this.itemName = "";
        x=0;
        y=0;
    }

    public Items(String itemName){
        this.itemName = itemName;
        x=0;
        y=0;
    }

    public Items(String itemName, String category, String aisleNumber){
        this.itemName = itemName;
        this.category = category;
        this.aisleNumber = aisleNumber;
    }

    //constructor
    public Items(String itemName, String category, String aisleNumber,int x,int y) {
        this.itemName = itemName;
        this.category = category;
        this.aisleNumber = aisleNumber;
        this.x = x;
        this.y = y;
    }

    //setters and getters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAisleNumber() {
        return aisleNumber;
    }

    public void setAisleNumber(String aisleNumber) {
        this.aisleNumber = aisleNumber;
    }

    public int getx() {
        return x;
    }

    public void setx(int x) {
        this.x = x;
    }

    public int gety() {
        return y;
    }

    public void sety(int y) { this.y = y;  }

}
