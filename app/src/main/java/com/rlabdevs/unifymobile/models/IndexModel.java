package com.rlabdevs.unifymobile.models;

import com.google.firebase.firestore.Exclude;

public class IndexModel {

    @Exclude
    private String ID;
    private String IndexCode;
    private String IndexName;
    private int CurrentCount;
    private String Prefix;

    public IndexModel() {
    }

    public IndexModel(String indexCode, String indexName, int currentCount, String prefix) {
        IndexCode = indexCode;
        IndexName = indexName;
        CurrentCount = currentCount;
        Prefix = prefix;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIndexCode() {
        return IndexCode;
    }

    public void setIndexCode(String indexCode) {
        IndexCode = indexCode;
    }

    public String getIndexName() {
        return IndexName;
    }

    public void setIndexName(String indexName) {
        IndexName = indexName;
    }

    public int getCurrentCount() {
        return CurrentCount;
    }

    public void setCurrentCount(int currentCount) {
        CurrentCount = currentCount;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }
}
