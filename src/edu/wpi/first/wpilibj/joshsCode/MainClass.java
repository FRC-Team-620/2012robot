/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.joshsCode;

import edu.wpi.first.wpilibj.AnalogModule;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.camera.AxisCameraException;
//import edu.wpi.first.wpilibj.image.BinaryImage;
//import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
//import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
//import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MainClass extends SimpleRobot {

    RobotDrive drive;
    
    DriverStationLCD driverStation;
 
    Joystick  leftStick;
    Joystick rightStick;
    Joystick   armStick;
    
    Jaguar leftJag;
    Jaguar rightJag;
    
    Arm robotArm;
    
    RampArm bonus;
    
    OtherMotors motors;
    
    Encoder encode1;
    AxisCamera camera;
    CriteriaCollection cc;
    ParticleAnalysisReport[] targets;
    
    //MASSIVE NUMBER OF VARIABLES!
    int tiltMotorJaguarChannel;
    int strafeMotorJaguarChannel;
    int lowerConveyorMotorVictorChannel;
    int upperConveyorMotorVictorChannel;
    int entrapmentMotorVictorChannel;
    int firingMotorVictorChannel;
    int gyroSensorAnalogChannel;
    int centerButtonDigitalChannel;
    int leftJaguarChannel;
    int rightJaguarChannel;
    int latchServoChannel;
    int humpServoChannel;
    int conveyorOnRawButton;
    int conveyorOffRawButton;
    int conveyorRevRawButton;
    int entrapmentInRawButton;
    int entrapmentOutRawButton;
    int entrapmentOffRawButton;
    int centerRawButton;
    int firingMotorReverseButton;
    int useRampArmButton;
    int upperConveyorRevShortButton;
    int angleArmButton;
    int gyroChan;
    
    public MainClass() {
        
        //All control and channel definitions are in this method for convenience.
        defineControls();
        driverStation = DriverStationLCD.getInstance();
//        drive = new RobotDrive(leftJaguarChannel, rightJaguarChannel);
        leftStick  = new Joystick(1);
        rightStick = new Joystick(2);
        armStick   = new Joystick(3);
        
        //encode1    = new Encoder(1, 2); //were not using encoders at this time
        //encode1.setDistancePerPulse(0.075398224);
        
        //transverseDriveThread = new Transverse_drive();
        
        //These are thread arguments. All definitions should be in the
        //defineControls() method. All documentation is there as well.
        
        robotArm = new Arm(armStick                    ,
                           tiltMotorJaguarChannel      ,
                           strafeMotorJaguarChannel    ,
                           centerButtonDigitalChannel  ,
                           firingMotorVictorChannel    ,
                           centerRawButton             ,
                           firingMotorReverseButton    ,
                           gyroChan,
                           angleArmButton);
          
        motors = new OtherMotors(armStick                         ,
                                 leftStick                        ,
                                 rightStick                       ,
                                 entrapmentOffRawButton           ,
                                 conveyorOnRawButton              ,
                                 conveyorOffRawButton             ,
                                 conveyorRevRawButton             ,
                                 upperConveyorRevShortButton      ,
                                 lowerConveyorMotorVictorChannel  ,
                                 upperConveyorMotorVictorChannel  ,
                                 entrapmentMotorVictorChannel     );
        
        /*bonus = new RampArm (latchServoChannel,
                             humpServoChannel);*/
        
        System.out.println("Here I am!");
        
    }

    //This method puts all the control definitions in one place for easy access.
    private void defineControls() {
        //Because the electronics board isn't done yet, I've taken the liberty of putting all of the arguments
        //in this method here. All of these things need defining before the robot will work properly.
        //The comments next to each argument detail what each of these things do. Use an integer for each!
        //channels:
        leftJaguarChannel               = 2;  // The Jaguar channel for the motor that drives the left wheels
        rightJaguarChannel              = 1;  // The Jaguar channel for the motor that drives the right wheels
        tiltMotorJaguarChannel          = 3;  // The Jaguar channel for the motor that tilts the robot arm
        strafeMotorJaguarChannel        = 8;  // The Jaguar channel for the motor that slides the robot arm back and forth
        lowerConveyorMotorVictorChannel = 6;  // The Victor channel for the motor that controls the lower conveyor belt
        upperConveyorMotorVictorChannel = 7;  // The Victor channel for the motor that controls the upper conveyor belt
        entrapmentMotorVictorChannel    = 5;  // The Victor channel for the motor that drives the entrapment stars
        firingMotorVictorChannel        = 9;  // The Victor channel for the motor that FIRES ZE CANNON!!!
        centerButtonDigitalChannel      = 3;  // The digital channel for the limit switch that is depressed when the arm is at center
        latchServoChannel               = 10;  // DIO channel for the ramp adjusting thing's latch
        humpServoChannel                = 4; // DIO channel for the ramp adjusting thing's hump
        //buttons
        conveyorOnRawButton             = 5;  // The button on (what is currently) armStick that toggles the conveyor forward
        conveyorOffRawButton            = 2;  // The button on (what is currently) armStick that toggles the conveyor reverse
        conveyorRevRawButton            = 4;  // The button on (what is currently) armStick that turns off the conveyor
        upperConveyorRevShortButton     = 3;  // button on armStick that causes converyor to reverse for .2 secs
        entrapmentInRawButton           = 2;  // The button on (what is currently) armStick that toggles the entrapment stars inwards
        entrapmentOutRawButton          = 3;  // The button on left/rightsick that toggles the entrapment stars outwards
        entrapmentOffRawButton          = 2;  // The button on rightStick that turns off entrapment stars
        centerRawButton                 = 11; // The button on (what is currently) armStick that tells the arm to return to home position
        firingMotorReverseButton        = 10; //Button on armStick that tells firingmotor to reverse
        useRampArmButton                = 2; //Button on leftStick tht tells rampArm thing to move down
        angleArmButton                  = 8;
        gyroChan                        = 1; //Channel of the gyro
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        leftJag = new Jaguar(leftJaguarChannel);
        rightJag = new Jaguar(rightJaguarChannel);
        leftJag.set(-.445 * .5);
        rightJag.set(.575 * .5);
        Timer.delay(8.0);
        leftJag.set(0);
        rightJag.set(0);
        
        robotArm.tiltMotor.set(-.75);
        robotArm.firingMotor.set(.65 * .95);
        Timer.delay(1.5);
        robotArm.tiltMotor.set(0);
        
        motors.upperConveyorController.set(-1);
        
        Timer.delay(5);
        
        leftJag.free();
        rightJag.free();
    }
   
//    public void target() {
//        target = CameraCode.getTarget();
//            if (targets.length == 1) {
//                if (targets[0].center_mass_x_normalized < 0) {
//                    
//                } else if (targets[0].center_mass_x_normalized > 0) {
//                    //move thing right I think
//                } else {
//                    //keep it still
//                }
//            }
//    }
    /**
     * This function is called once each time the robot enters operator control.
     */
    
    /*public void disabled() {
//       System.out.println("Target locking...");
//        try {
//        CameraCode.getTarget();
//        }
//        catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Nothing!");
//        }
//        System.out.println("Target acquired");
        if (!isEnabled()) {
            robotArm.strafeMotor.set(0);
            robotArm.tiltMotor.set(0);
            robotArm.firingMotor.set(0);
            System.out.println("Arm motors offline");
        
            motors.lowerConveyorController.set(0);
            motors.upperConveyorController.set(0);
            motors.entrapmentController.set(0);
        
            System.out.println("All motors offline");
        }
        return;
    }*/
    
    public void operatorControl() {
        drive = new RobotDrive(leftJaguarChannel, rightJaguarChannel);
        motors.upperConveyorController.set(0);
        robotArm.firingMotor.set(0);
        System.out.println("Remote controls online");
        while (isOperatorControl() && isEnabled()) {
            DriveMethods.exponentialTankDrive(drive, .0, leftStick.getY(), rightStick.getY());
            updateDashboard();
            robotArm.doStuff();
            motors.doStuff();
            /*if (leftStick.getRawButton(useRampArmButton)) {
                if (bonus.getPos()) {
                    bonus.goDown();
                } else if (!bonus.getPos()) {
                    bonus.goUp();
                }
            }*/
//            targets = getTargets();
//            if (targets.length == 1) {
//                if (targets[0].center_mass_x_normalized < 0) {
//                    //move thing left i think
//                } else if (targets[0].center_mass_x_normalized > 0) {
//                    //move thing right I think
//                } else {
//                    //keep it still
//                }
//            }
            //Timer.delay(0.005); //Do we need this? Is the cRIO a real-time OS?
        }
        drive.free();
    }


    public static void updateDashboard() {
        Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
        lowDashData.addCluster();
        {
            lowDashData.addCluster();
            {     //analog modules
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(1).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(2).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
            }
            lowDashData.finalizeCluster();

            lowDashData.addCluster();
            { //digital modules
                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 1;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 2;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayReverse());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

            }
            lowDashData.finalizeCluster();

            lowDashData.addByte(Solenoid.getAllFromDefaultModule());
        }
        lowDashData.finalizeCluster();
        lowDashData.commit();
    }
    
}

