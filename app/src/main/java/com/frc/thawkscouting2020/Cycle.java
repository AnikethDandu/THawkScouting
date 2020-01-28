package com.frc.thawkscouting2020;

public class Cycle {

    int innerHit;
    int outerHit;
    int bottomHit;
    int highMiss;
    int lowMiss;

    public Cycle(int innerHit, int outerHit, int bottomHit, int highMiss, int lowMiss) {
        this.innerHit = innerHit;
        this.outerHit = outerHit;
        this.bottomHit = bottomHit;
        this.highMiss = highMiss;
        this.lowMiss = lowMiss;
    }
}
