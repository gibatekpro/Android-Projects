package com.gibatekpro.tipsnodds.ListItems;

/**
 * Created by Gibah on 12/13/2017.
 */

public class ListItem_played {

    private int check_icon;
    private int flag;
    private String id;
    private String country;
    private String date;
    private String home;
    private String vs;
    private String away;
    private String tip;
    private String time;
    private String odd;


    public ListItem_played(int check_icon, int flag, String id, String country, String date, String home, String vs, String away, String tip, String time, String odd) {
        this.check_icon = check_icon;
        this.flag = flag;
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

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        return date;
    }


    public String getHome() {
        return home;
    }

    public String getVs() {
        return vs;
    }

    public String getAway() {
        return away;
    }

    public String getTip() {
        return tip;
    }

    public String getTime() {
        return time;
    }

    public String getOdd() {
        return odd;
    }
}
