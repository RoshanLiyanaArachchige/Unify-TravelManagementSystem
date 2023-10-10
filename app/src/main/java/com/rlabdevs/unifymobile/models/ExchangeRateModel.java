package com.rlabdevs.unifymobile.models;

public class ExchangeRateModel {

    private String ExchangeRateCode;
    private String CurrencyCodeFrom;
    private String CurrencyCodeTo;
    private double ExchangeRate;
    private String StatusCode;

    public ExchangeRateModel() {
    }

    public ExchangeRateModel(String exchangeRateCode, String currencyCodeFrom, String currencyCodeTo, double exchangeRate, String statusCode) {
        ExchangeRateCode = exchangeRateCode;
        CurrencyCodeFrom = currencyCodeFrom;
        CurrencyCodeTo = currencyCodeTo;
        ExchangeRate = exchangeRate;
        StatusCode = statusCode;
    }

    public String getExchangeRateCode() {
        return ExchangeRateCode;
    }

    public void setExchangeRateCode(String exchangeRateCode) {
        ExchangeRateCode = exchangeRateCode;
    }

    public String getCurrencyCodeFrom() {
        return CurrencyCodeFrom;
    }

    public void setCurrencyCodeFrom(String currencyCodeFrom) {
        CurrencyCodeFrom = currencyCodeFrom;
    }

    public String getCurrencyCodeTo() {
        return CurrencyCodeTo;
    }

    public void setCurrencyCodeTo(String currencyCodeTo) {
        CurrencyCodeTo = currencyCodeTo;
    }

    public double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }
}
