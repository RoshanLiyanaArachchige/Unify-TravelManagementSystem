package com.rlabdevs.unifymobile.models.master;

public class NewCurrencyModel {
    private Integer CurrencyId;
    private String Code;
    private String Name;
    private String Symbol;
    private Integer StatusId;

    public NewCurrencyModel() {
    }

    public NewCurrencyModel(Integer currencyId, String code, String name, String symbol, Integer statusId) {
        CurrencyId = currencyId;
        Code = code;
        Name = name;
        Symbol = symbol;
        StatusId = statusId;
    }

    public Integer getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        CurrencyId = currencyId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public Integer getStatusId() {
        return StatusId;
    }

    public void setStatusId(Integer statusId) {
        StatusId = statusId;
    }
}
