/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.joshsCode;

//import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.SimpleRobot;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
//import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.camera.AxisCameraException;
//import edu.wpi.first.wpilibj.image.BinaryImage;
//import edu.wpi.first.wpilibj.image.ColorImage;
//import edu.wpi.first.wpilibj.image.CriteriaCollection;
//import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
//import edu.wpi.first.wpilibj.image.NIVisionException;
//import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author Warbots
 */
public class Arm {

    Joystick armControl;
    int centerValue = 0;
    //This int defines which direction the arm needs to move to center itself.
    //-1 means its to the left of center and needs to go right
    // 0 means its at center and doesn't need to move
    // 1 means its to the right of center and needs to go left.
    int centerButton;
    int leftButton;
    int rightButton;
    int reverseButton;
    double xValue;
    double yValue;
    //double centerXValue;
    //double centerYValue;
    Jaguar tiltMotor;
    Jaguar strafeMotor;
    Victor firingMotor;
    //AnalogChannel stringSensor;
    //AnalogChannel gyroscope;
    DigitalInput centerMarker;
    Gyro gyro;
    int angleArmButton;
    double angle;
    double margin;
    double factor;
    boolean running;

    public Arm(Joystick controlStick,
            int tiltMotorChannel,
            int strafeMotorChannel,
            int centerIn,
            int firingMotorChannel,
            //int gyroIn,
            int argCenterButton,
            int argReverseButton,
            int gyroChannel,
            int angleButton) {
        //super();

        armControl = controlStick;
        tiltMotor = new Jaguar(tiltMotorChannel);
        strafeMotor = new Jaguar(strafeMotorChannel);
        xValue = 0.0;
        yValue = 0.0;
        centerMarker = new DigitalInput(centerIn);
        firingMotor = new Victor(firingMotorChannel);
        //gyroscope = new AnalogChannel(gyroIn);
        centerButton = argCenterButton;
        reverseButton = argReverseButton;
        //leftButton = argLeftButton;
        //rightButton = argRightButton;
        gyro = new Gyro(gyroChannel);
        angleArmButton = angleButton;
        angle = 49.6; //desired angle.
        margin = 5; //margin of the angle
        gyro.reset();
    }

    public void doStuff() {
       //TODO: set the entrapment stars to two different directions
        xValue = armControl.getX();
        yValue = armControl.getY();
        if (centerValue == 0) {
            if (xValue < 0) {
                centerValue = -1;
            }
            if (xValue > 0) {
                centerValue = 1;
            }
        }
        if (Math.abs(xValue) > .2) {
            strafeMotor.set(-1 * xValue);
        }
        else {
            strafeMotor.set(0);
        }
        //if ((centerValue == 0 && armControl.getY() < 0) || (armControl.getY() > 0)) {
            tiltMotor.set(yValue);
        //}
        if (armControl.getTrigger()) {
            System.out.println("Firing...");
            firingMotor.set(1 - ((armControl.getRawAxis(3) + 1) / 2));
        } else if (armControl.getRawButton(reverseButton)) {
            firingMotor.set(-(1 - ((armControl.getRawAxis(3) + 1) / 2)));
        } else {
            firingMotor.set(0);
        }
        if (armControl.getRawButton(centerButton)) {
            System.out.println("Centering...");
            centerArm();
        }
        if (centerMarker.get()) {
            //System.out.println("Centered...");
            centerValue = 0;
        }
        if (armControl.getRawButton(angleArmButton)) {
            double a = gyro.getAngle();
            System.out.println("Gyro Angle: " + a);
            factor = 0;
            if (a == angle || (angle - margin < a && a < angle + margin)) {
                //do nothing
                tiltMotor.set(0);
            } else if (a < angle) {
                //move up
                factor = Math.abs(1 - (a / angle));
                tiltMotor.set(-.5 * factor);
            } else if (a > angle) {
                //move down
                factor = Math.abs(1 - (angle / a));
                tiltMotor.set(.5 * factor);
            }
        }
        if (armControl.getRawButton(9)) {
            gyro.reset();
        }
        System.out.println("Gyro Angle: " + gyro.getAngle());
    }

    private void centerArm() {
        if (centerValue == -1) {
            while (!centerMarker.get()) {
                strafeMotor.set(.25);
            }
        }

        if (centerValue == 1) {
            while (!centerMarker.get()) {
                strafeMotor.set(-.25);
            }
        }
        strafeMotor.set(0.0);
        centerValue = 0;
    }

    private void fire() {
        firingMotor.set(armControl.getThrottle());
        //try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "10ft2.jpg"
             * 
             */
//            
//            AxisCamera camera = AxisCamera.getInstance();  // get an instance ofthe camera
//            CriteriaCollection cc = new CriteriaCollection();      // create the criteria for the particle filter
//            cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
//            cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
//            ColorImage image = camera.getImage();     // comment if using stored images
//            //ColorImage image;                           // next 2 lines read image from flash on cRIO
//            //image =  new RGBImage("/10ft2.jpg");
//            BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects
//            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
//            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
//            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles
//
//            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
//            for (int i = 0; i < reports.length; i++) {                                // print results
//                ParticleAnalysisReport r = reports[i];
//                System.out.println("Particle: " + i + ":  Center of mass x normalized: " + r.center_mass_x_normalized);
//            }
//            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());
//
//            /**
//             * all images in Java must be freed after they are used since they are allocated out
//             * of C data structures. Not calling free() will cause the memory to accumulate over
//             * each pass of this loop.
//             */
//            filteredImage.free();
//            convexHullImage.free();
//            bigObjectsImage.free();
//            thresholdImage.free();
//            image.free();
//
//        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
//            ex.printStackTrace();
//        } catch (NIVisionException ex) {
//            ex.printStackTrace();
//        }
//    }
//    public boolean isRunning() {
//        return running;
//    }
//    public void setRunning(boolean run) {
//        running = run;
//    }
    }
}
