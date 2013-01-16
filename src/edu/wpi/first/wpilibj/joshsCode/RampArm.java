/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.joshsCode;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Warbots
 */
public class RampArm {
    Servo latch;
    Servo hump;
    boolean pos; //true is up, false is down
    public RampArm(int latchChan, int humpChan) {
        latch = new Servo(latchChan);
        hump = new Servo(humpChan);
        pos = true;
    }
    public Servo getHump() {
        return hump;
    }
    public Servo getLatch() {
        return latch;
    }
    public boolean getPos() {
        return pos;
    }
    public void goDown() {
        if (!pos) {
            return;
        }
        //Hump goes down, then latch
        hump.set(1-.15);
        Timer.delay(1);
        latch.set(.3);
        pos = false;
    }
    public void goUp() {
        if (pos) {
            return;
        }
        //Latch goes up, then hump
        latch.set(.80);
        Timer.delay(1);
        hump.set(1-.78);
        pos = true;
    }
}
