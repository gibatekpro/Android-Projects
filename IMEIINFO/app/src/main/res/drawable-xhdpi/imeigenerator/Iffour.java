package com.gibatekpro.imeigenerator;

/**
 * Created by Anthony Gibah on 6/11/2017.
 */
public class Iffour extends analyst {
    String num14;

    public Iffour(String dnum14) {
        super(dnum14);
        // TODO Auto-generated constructor stub
        num14 = dnum14;
    }

    public int Sepfourdig() {
        char a1, a2, a3, a4, a5, a6, a7, a8, a9, a0, b1, b2, b3, b4;

        int dig1, dig2, dig3, dig4, dig5, dig6, dig7, dig8, dig9, dig10, dig11, dig12, dig13, dig14;

        int luhn = 0;

        int digtot;

        a1 = num14.charAt(0);
        a2 = num14.charAt(1);
        a3 = num14.charAt(2);
        a4 = num14.charAt(3);
        a5 = num14.charAt(4);
        a6 = num14.charAt(5);
        a7 = num14.charAt(6);
        a8 = num14.charAt(7);
        a9 = num14.charAt(8);
        a0 = num14.charAt(9);
        b1 = num14.charAt(10);
        b2 = num14.charAt(11);
        b3 = num14.charAt(12);
        b4 = num14.charAt(13);

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
        return luhn;
        // System.out.println(digtot);
        // System.out.println(luhn);

    }

    public long iffournew() {
        long cast = Long.parseLong(num14);
        char lastdig;
        long corrluhn;
        int newluhn = Sepfourdig();

        corrluhn = ((cast * 10) + newluhn);

        return corrluhn;
    }

    public String cd2() {
        int b5 = Sepfourdig();
        String str = Integer.toString(b5);
        //System.out.println(str);
        return str;
    }

    public String ld2() {
        int b5 = Sepfourdig();
        String str = Integer.toString(b5);
        //System.out.println(str);
        return str;
    }
}

