package com.punchy.punchclock.vo;

public class EmailBody {

    private String locale;

    private String destinyEmailAddress;

    public String getDestinyEmailAddress() {
        return destinyEmailAddress;
    }

    public void setDestinyEmailAddress(String destinyEmailAddress) {
        this.destinyEmailAddress = destinyEmailAddress;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
