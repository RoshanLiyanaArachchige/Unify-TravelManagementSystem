package com.rlabdevs.unifymobile.models;

import javax.annotation.Nullable;

public class SelectorItemModel
{
    private String ItemCode;
    private String ItemName;

    public SelectorItemModel() {
    }

    public SelectorItemModel(String itemCode, String itemName) {
        ItemCode = itemCode;
        ItemName = itemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
