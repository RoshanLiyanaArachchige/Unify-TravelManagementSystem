package com.rlabdevs.unifymobile.models;

public class CurrencyModel {

    private String CurrencyCode;
    private String CurrencyName;
    private String Symbol;
    private String StatusCode;

    public CurrencyModel() {
    }

    public CurrencyModel(String currencyCode, String currencyName, String symbol, String statusCode) {
        CurrencyCode = currencyCode;
        CurrencyName = currencyName;
        Symbol = symbol;
        StatusCode = statusCode;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public void setCurrencyName(String currencyName) {
        CurrencyName = currencyName;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
