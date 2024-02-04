package com.rlabdevs.unifymobile.models.service;

import androidx.annotation.NonNull;

public class NewServiceClassModel {
    private Integer ServiceClassId;
    private Integer ServiceId;
    private Integer Class;

    public NewServiceClassModel() {
    }

    public NewServiceClassModel(Integer serviceClassId, Integer serviceId, Integer aClass) {
        ServiceClassId = serviceClassId;
        ServiceId = serviceId;
        Class = aClass;
    }

    public Integer getServiceClassId() {
        return ServiceClassId;
    }

    public void setServiceClassId(Integer serviceClassId) {
        ServiceClassId = serviceClassId;
    }

    public Integer getServiceId() {
        return ServiceId;
    }

    public void setServiceId(Integer serviceId) {
        ServiceId = serviceId;
    }

    public Integer getServiceClass() {
        return Class;
    }

    public void setServiceClass(Integer aClass) {
        Class = aClass;
    }
}
