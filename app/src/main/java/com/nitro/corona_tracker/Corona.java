package com.nitro.corona_tracker;

public class Corona {

    private String active;
    private String positive;
    private String cured;
    private String deceased;
    private String state;
    private String new_cured;
    private String new_death;
    private String new_active;

    public Corona(String district,String positive, String active, String cured, String deceased) {
        this.state=district;
        this.active = active;
        this.positive = positive;
        this.cured = cured;
        this.deceased = deceased;
        this.new_active="0";
        this.new_cured="0";
        this.new_death="0";
    }

    public Corona(String state,String positive, String active, String cured, String deceased,String new_active,String new_cured,String new_death) {
        this.state=state;
        this.active = active;
        this.positive = positive;
        this.cured = cured;
        this.deceased = deceased;
        this.new_active=new_active;
        this.new_cured=new_cured;
        this.new_death=new_death;
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

    public String getNew_cured() {
        return new_cured;
    }

    public void setNew_cured(String new_cured) {
        this.new_cured = new_cured;
    }

    public String getNew_death() {
        return new_death;
    }

    public void setNew_death(String new_death) {
        this.new_death = new_death;
    }

    public String getNew_active() {
        return new_active;
    }

    public void setNew_active(String new_active) {
        this.new_active = new_active;
    }
}
