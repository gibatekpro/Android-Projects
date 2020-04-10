package com.gibatekpro.tipsnodds.Admin.Model_played;

public class ToDo_played {

    private int check_icon;
    private int flag;
    private String progress;
    private String id;
    private String country;
    private String date;
    private String home;
    private String vs;
    private String away;
    private String tip;
    private String time;
    private String odd;

    public ToDo_played() {

    }


    public ToDo_played(int check_icon, int flag, String progress, String id, String country, String date, String home, String vs, String away, String tip, String time, String odd) {
        this.check_icon = check_icon;
        this.flag = flag;
        this.progress = progress;
        this.id = id;
        this.country = country;
        this.date = date;
        this.home = home;
        this.vs = vs;
        this.away = away;
        this.tip = tip;
        this.time = time;
        this.odd = odd;
    }

    public int getCheck_icon() {
        return check_icon;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String country) {
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getVs() {
        return vs;
    }

    public void setVs(String vs) {
        this.vs = vs;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }
}
