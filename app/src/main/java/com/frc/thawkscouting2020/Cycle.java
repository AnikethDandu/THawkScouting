package com.frc.thawkscouting2020;

/**
 * Class that holds data for each cycle
 *
 * @author Aniketh Dandu - Team 1100
 */
class Cycle {

    /**
     * Integers for power cell hits and misses
     */
    int innerHit;
    int outerHit;
    int bottomHit;
    int highMiss;
    int lowMiss;

    // Constructor for class
    Cycle(int innerHit, int outerHit, int bottomHit, int highMiss, int lowMiss) {
        this.innerHit = innerHit;
        this.outerHit = outerHit;
        this.bottomHit = bottomHit;
        this.highMiss = highMiss;
        this.lowMiss = lowMiss;
    }
}
