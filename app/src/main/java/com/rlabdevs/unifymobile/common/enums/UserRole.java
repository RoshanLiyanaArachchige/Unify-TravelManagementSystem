package com.rlabdevs.unifymobile.common.enums;

public enum UserRole {

    Admin, User;

    public String getUserRole()
    {
        switch (this)
        {
            case Admin:
                return "USRRL1";
            case User:
                return "USRRL2";
        }

        return "";
    }
}

