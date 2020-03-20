package com.frc.thawkscouting2020;

/**
 * Class that holds data for each cycle
 *
 * @author Aniketh Dandu - Team 1100
 */
final class Cycle {

    int innerHit;
    int outerHit;
    int bottomHit;
    int highMiss;
    int lowMiss;

    Cycle(int innerHit, int outerHit, int bottomHit, int highMiss, int lowMiss) {
        this.innerHit = innerHit;
        this.outerHit = outerHit;
        this.bottomHit = bottomHit;
        this.highMiss = highMiss;
        this.lowMiss = lowMiss;
    }
}
