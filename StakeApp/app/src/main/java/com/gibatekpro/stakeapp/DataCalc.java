package com.gibatekpro.stakeapp;

public class DataCalc {

    private int round;
    private double previousStake, stake, odd, returns, profit;

    public DataCalc(int round, double previousStake, double odd, double profit) {
        this.round = round;
        this.previousStake = previousStake;
        this.odd = odd;
        this.profit = profit;
    }

    public int getRound() {
        return round;
    }

    public double getStake() {

        stake = (profit + previousStake)/(odd - 1);

        return stake;
    }

    public double getOdd() {
        return odd;
    }

    public double getReturns() {

        returns = stake * odd;

        return returns;
    }

    public double getProfit() {
        return profit;
    }

}
