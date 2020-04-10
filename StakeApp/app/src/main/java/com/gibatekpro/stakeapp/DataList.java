package com.gibatekpro.stakeapp;

public class DataList {
    int dRound;
    double dStake, dOdd, dReturns, dProfit;

    public DataList(int dRound, Double dStake, Double dOdd, Double dReturns, Double dProfit) {
        this.dRound = dRound;
        this.dStake = dStake;
        this.dOdd = dOdd;
        this.dReturns = dReturns;
        this.dProfit = dProfit;
    }

    public int getdRound() {
        return dRound;
    }

    public Double getdStake() {
        return dStake;
    }

    public Double getdOdd() {
        return dOdd;
    }

    public Double getdReturns() {
        return dReturns;
    }

    public Double getdProfit() {
        return dProfit;
    }

}
