/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.joshsCode;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Warbots
 */
public class DriveMethods {
 
    public static void quadraticTankDrive(RobotDrive drive, double argDeadzone, double argLeftY, double argRightY) {
        //Deadzone is essentially a gate. Any input less than that will simply not be reported
        //Sensitivity coefficient makes Joystick.getY() equal to Joystick.getY() * (sensitiveCoefficients)
        double deadzone = argDeadzone;
        double leftY  =  argLeftY;
        double rightY = argRightY;
        //Lefty and righty! Ha! I'm so witty... (Not really)
        //Anyway, these are simply the y axis values from each stick
        if (Math.abs(leftY) < deadzone) {
            leftY = 0.0;
        } else {
            leftY *= Math.abs(leftY);
        }
        if (Math.abs(rightY) < deadzone) {
            rightY = 0.0;
        } else {
            rightY *= Math.abs(rightY);
        }
        drive.tankDrive(leftY, rightY);
    }

    public static void linearTankDrive(RobotDrive drive, double argDeadzone, double argSensitiveCoefficient, double argLeftY, double argRightY) {
        //Deadzone is essentially a gate. Any input less than that will simply not be reported
        //Sensitivity coefficient makes Joystick.getY() equal to Joystick.getY() * (sensitiveCoefficients)
        double deadzone = argDeadzone;
        double sensitiveCoefficient = argSensitiveCoefficient;
        double leftY  = argLeftY;
        double rightY = argRightY;
        //Lefty and righty! Ha! I'm so witty... (Not really)
        //Anyway, these are simply the y axis values from each stick
        if (Math.abs(leftY) < deadzone) {
            leftY = 0.0;
        } else {
            leftY *= sensitiveCoefficient;
        }
        if (Math.abs(rightY) < deadzone) {
            rightY = 0.0;
        } else {
            rightY *= sensitiveCoefficient;
        }
        drive.tankDrive(leftY, rightY);
    }
    
    public static void exponentialTankDrive(RobotDrive drive, double argDeadzone, double argLeftY, double argRightY) {
        double deadzone = argDeadzone;
        double leftY = argLeftY;
        double rightY = argRightY;
        if (Math.abs(leftY) < deadzone) {
            leftY = 0.0;
        } else {
            leftY = MathUtils.pow(2, Math.abs(leftY)) - 1;
            if (argLeftY < 0) {
                leftY *= -1;
            }
        }
        if (Math.abs(rightY) < deadzone) {
            rightY = 0.0;
        } else {
            rightY = MathUtils.pow(2, Math.abs(rightY)) - 1;
            if (argRightY < 0) {
                rightY *= -1;
            }
        }
        //System.out.println(leftY + " " + rightY);
    drive.tankDrive(leftY,rightY);
    }
}
