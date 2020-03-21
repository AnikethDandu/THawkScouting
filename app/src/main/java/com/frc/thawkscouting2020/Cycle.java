package com.frc.thawkscouting2020;

/**
 * Class that holds shots for each cycle
 *
 * @author Aniketh Dandu - FRC Team 1100
 */
final class Cycle {

    int InnerHit;
    int OuterHit;
    int BottomHit;
    int HighMiss;
    int LowMiss;

    Cycle(int innerHit, int outerHit, int bottomHit, int highMiss, int lowMiss) {
        this.InnerHit = innerHit;
        this.OuterHit = outerHit;
        this.BottomHit = bottomHit;
        this.HighMiss = highMiss;
        this.LowMiss = lowMiss;
    }
}
