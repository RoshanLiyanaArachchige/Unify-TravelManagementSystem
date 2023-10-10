package com.rlabdevs.unifymobile.common.enums;

public enum CurrencyCode {

    USD, EURO, LKR;

    public String getCurrencyCode()
    {
        switch (this)
        {
            case USD:
                return "CUR1";
            case EURO:
                return "CUR2";
            case LKR:
                return "CUR3";
        }
        return "";
    }
}
