package com.gibatekpro.imeigenerator;

/**
 * Created by Anthony Gibah on 6/11/2017.
 */
public class analyst extends com.gibatekpro.imeigenerator.Digseperator {
    String num14;
    int count = 0;

    public analyst(String dnum14) {
        super(dnum14);
        // TODO Auto-generated constructor stub
        num14 = dnum14;
    }

    // Type Allocation code
    public String tac() {
        char a1, a2, a3, a4, a5, a6;
        String str;

        a1 = num14.charAt(0);
        a2 = num14.charAt(1);
        a3 = num14.charAt(2);
        a4 = num14.charAt(3);
        a5 = num14.charAt(4);
        a6 = num14.charAt(5);

        str = new StringBuilder().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).toString();
        //System.out.println(str);
        return str;
    }

    // Reporting Body Identifier
    public String rbi() {
        char a1, a2;
        String str;

        a1 = num14.charAt(0);
        a2 = num14.charAt(1);
        str = new StringBuilder().append(a1).append(a2).toString();
        //System.out.println(str);
        return str;
    }

    // Mobile Equipment Type
    public String meb() {
        char a3, a4, a5, a6;
        String str;

        a3 = num14.charAt(2);
        a4 = num14.charAt(3);
        a5 = num14.charAt(4);
        a6 = num14.charAt(5);

        str = new StringBuilder().append(a3).append(a4).append(a5).append(a6).toString();
        //System.out.println(str);
        return str;
    }

    // Final Assembly Code
    public String fac() {
        char a7, a8;
        String str;

        a7 = num14.charAt(6);
        a8 = num14.charAt(7);
        str = new StringBuilder().append(a7).append(a8).toString();
        //System.out.println(str);
        return str;
    }

    // Serial Number
    public String sn() {
        char a9, a0, b1, b2, b3, b4;
        String str;

        a9 = num14.charAt(8);
        a0 = num14.charAt(9);
        b1 = num14.charAt(10);
        b2 = num14.charAt(11);
        b3 = num14.charAt(12);
        b4 = num14.charAt(13);
        str = new StringBuilder().append(a9).append(a0).append(b1).append(b2).append(b3).append(b4).toString();
        //System.out.println(str);
        return str;
    }

    // Check Digit
    public String cd() {
        int b5 = Sepfifdig();
        String str = Integer.toString(b5);
        //System.out.println(str);
        return str;
    }

    // Luhn Digit
    public String ld() {
        int b5 = Sepfifdig();
        String str = Integer.toString(b5);
        //System.out.println(str);
        return str;
    }

}

