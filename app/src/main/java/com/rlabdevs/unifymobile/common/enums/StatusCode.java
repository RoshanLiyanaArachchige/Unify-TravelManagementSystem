package com.rlabdevs.unifymobile.common.enums;

public enum StatusCode {

    Active, Deactive, Deleted, Pending, Finished, Canceled, Approved, Disapproved;

    public String getStatusCode()
    {
        switch (this)
        {
            case Active:
                return "STS1";
            case Deactive:
                return "STS2";
            case Deleted:
                return "STS3";
            case Pending:
                return "STS4";
            case Finished:
                return "STS5";
            case Canceled:
                return "STS6";
            case Approved:
                return "STS7";
            case Disapproved:
                return "STS8";
        }

        return "";
    }
}
