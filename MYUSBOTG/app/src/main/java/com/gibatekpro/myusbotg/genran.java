package com.gibatekpro.myusbotg;

import java.util.Random;

/**
 * Created by Anthony Gibah on 6/11/2017.
 */
public class genran {


    String dimei;
    int count;
    Random rand = new Random();

    public genran(String inimei) {
        dimei = inimei;
    }

    public String if12() {
        // this generates the 14 digits of the imei for
        // user input of 12 digits.
        int value = rand.nextInt(90) + 10;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if11() {
        // this generates the 14 digits of the imei for
        // user input of 11 digits.
        int value = rand.nextInt(900) + 100;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if10() {
        // this generates the 14 digits of the imei for
        // user input of 10 digits.
        int value = rand.nextInt(9000) + 1000;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if9() {
        // this generates the 14 digits of the imei for
        // user input of 9 digits.
        int value = rand.nextInt(90000) + 10000;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if8() {
        // this generates the 14 digits of the imei for
        // user input of 8 digits.
        int value = rand.nextInt(900000) + 100000;
        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();


        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();
        System.out.println(fullimei);
        return fullimei;


    }

    public String if7() {
        // this generates the 14 digits of the imei for
        // user input of 7 digits.
        int value = rand.nextInt(9000000) + 1000000;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if6() {
        // this generates the 14 digits of the imei for
        // user input of 6 digits.
        int value = rand.nextInt(90000000) + 10000000;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if5() {
        // this generates the 14 digits of the imei for
        // user input of 5 digits.
        int value = rand.nextInt(900000000) + 100000000;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if4() {
        // this generates the 14 digits of the imei for
        // user input of 4 digits.
        int value = rand.nextInt((int) 9000000000L) + 1000000000;
        String first14 = "";

        String strvalue = Integer.toString(value);
        first14 = new StringBuilder().append(dimei).append(strvalue).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if3() {
        // this generates the 14 digits of the imei for
        // user input of 3 digits.

        String first14 = "";
        int value = rand.nextInt((int) 9000000000L) + 1000000000;
        int value2 = rand.nextInt(10);
        String strvalue = Integer.toString(value);
        String strvalue2 = Integer.toString(value2);
        first14 = new StringBuilder().append(dimei).append(strvalue).append(strvalue2).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();
        System.out.println(fullimei);
        return fullimei;

    }

    public String if2() {
        // this generates the 14 digits of the imei for
        // user input of 2 digits.

        String first14 = "";
        int value = rand.nextInt((int) 9000000000L) + 1000000000;
        int value2 = rand.nextInt(90) + 10;
        String strvalue = Integer.toString(value);
        String strvalue2 = Integer.toString(value2);
        first14 = new StringBuilder().append(dimei).append(strvalue).append(strvalue2).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if1() {
        // this generates the 14 digits of the imei for
        // user input of 1 digits.
        String first14 = "";
        int value = rand.nextInt((int) 9000000000L) + 1000000000;
        int value2 = rand.nextInt(900) + 100;
        String strvalue = Integer.toString(value);
        String strvalue2 = Integer.toString(value2);
        first14 = new StringBuilder().append(dimei).append(strvalue).append(strvalue2).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;

    }

    public String if0() {
        // this generates the 14 digits of the imei for
        // user input of 0 digits.
        String first14 = "";
        int value = rand.nextInt((int) 1000L) + 35000;
        int value2 = rand.nextInt(9000) + 1000;
        int value3 = rand.nextInt(90000) + 10000;
        String strvalue = Integer.toString(value);
        String strvalue2 = Integer.toString(value2);
        String strvalue3 = Integer.toString(value3);
        first14 = new StringBuilder().append(dimei).append(strvalue).append(strvalue2).append(strvalue3).toString();

        String fullimei = "";
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = first14.charAt(0);
        a2 = first14.charAt(1);
        a3 = first14.charAt(2);
        a4 = first14.charAt(3);
        a5 = first14.charAt(4);
        a6 = first14.charAt(5);
        a7 = first14.charAt(6);
        a8 = first14.charAt(7);
        a9 = first14.charAt(8);
        a0 = first14.charAt(9);
        b1 = first14.charAt(10);
        b2 = first14.charAt(11);
        b3 = first14.charAt(12);
        b4 = first14.charAt(13);

        dig1 = ((((Character.getNumericValue(a2)) * 2) % 100) / 10) + (((Character.getNumericValue(a2)) * 2) % 10);
        dig2 = ((((Character.getNumericValue(a4)) * 2) % 100) / 10) + (((Character.getNumericValue(a4)) * 2) % 10);
        dig3 = ((((Character.getNumericValue(a6)) * 2) % 100) / 10) + (((Character.getNumericValue(a6)) * 2) % 10);
        dig4 = ((((Character.getNumericValue(a8)) * 2) % 100) / 10) + (((Character.getNumericValue(a8)) * 2) % 10);
        dig5 = ((((Character.getNumericValue(a0)) * 2) % 100) / 10) + (((Character.getNumericValue(a0)) * 2) % 10);
        dig6 = ((((Character.getNumericValue(b2)) * 2) % 100) / 10) + (((Character.getNumericValue(b2)) * 2) % 10);
        dig7 = ((((Character.getNumericValue(b4)) * 2) % 100) / 10) + (((Character.getNumericValue(b4)) * 2) % 10);
        dig8 = (Character.getNumericValue(a1));
        dig9 = (Character.getNumericValue(a3));
        dig10 = (Character.getNumericValue(a5));
        dig11 = (Character.getNumericValue(a7));
        dig12 = (Character.getNumericValue(a9));
        dig13 = (Character.getNumericValue(b1));
        dig14 = (Character.getNumericValue(b3));

        digtot = dig1 + dig2 + dig3 + dig4 + dig5 + dig6 + dig7 + dig8 + dig9 + dig10 + dig11 + dig12 + dig13 + dig14;

        for (int i = digtot; i % 10 != 0; i++)
            luhn++;
        String strluhn = Integer.toString(luhn);
        fullimei = new StringBuilder().append(first14).append(strluhn).toString();

        return fullimei;
    }


}

