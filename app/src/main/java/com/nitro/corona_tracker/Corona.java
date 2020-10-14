package com.nitro.corona_tracker;

public class Corona {

    private String active;
    private String positive;
    private String cured;
    private String deceased;
    private String state;

    public Corona(String state,String positive, String active, String cured, String deceased) {
        this.state=state;
        this.active = active;
        this.positive = positive;
        this.cured = cured;
        this.deceased = deceased;
    }

    public String getCured() {
        return cured;
    }

    public void setCured(String cured) {
        this.cured = cured;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
