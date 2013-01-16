/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.joshsCode;

//import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Warbots
 */
public class OtherMotors {
    
    Joystick controlStick;
    Joystick entrapmentIn;
    Joystick entrapmentOut;
    Victor lowerConveyorController;
    Victor upperConveyorController;
    Victor entrapmentController;
    int conveyorOnButton;
    int conveyorOffButton;
    int conveyorRevButton;
    int entrapmentInButton;
    int entrapmentOutButton;
    int entrapmentOffButton;
    int upperConveyorRevShortButton;
    boolean running;
    double entStars = 0.0;
    
    public OtherMotors(Joystick argControlStick    ,
                       Joystick argEntrapmentIn    ,
                       Joystick argEntrapmentOut   ,
                       int argEntrapmentOffButton  ,
                       int argConveyorOnButton     ,
                       int argConveyorOffButton    ,
                       int argConveyorRevButton    ,
                       int argShortRevButton      ,
                       int lowerConveyorChannel    ,
                       int upperConveyorChannel    ,
                       int entrapmentChannel)      {
  //      super();
        
        controlStick        = argControlStick;
        entrapmentIn        = argEntrapmentIn;
        entrapmentOut       = argEntrapmentOut;
        entrapmentOffButton = argEntrapmentOffButton;
        
        conveyorOnButton    = argConveyorOnButton;
        conveyorOffButton   = argConveyorOffButton;
        conveyorRevButton   = argConveyorRevButton;
        upperConveyorRevShortButton = argShortRevButton;
        lowerConveyorController = new Victor(lowerConveyorChannel);
        upperConveyorController = new Victor(upperConveyorChannel);
        entrapmentController    = new Victor(entrapmentChannel);
        
    }
    
    public void doStuff() {

        if (controlStick.getRawButton(conveyorRevButton)) {
            System.out.println("Conveyor Button reverse...");
            lowerConveyorController.set(1);
            upperConveyorController.set(1);
            }
        else if (controlStick.getRawButton(conveyorOnButton)) {
            System.out.println("Conveyor Button forward...");
            lowerConveyorController.set(-1);
            upperConveyorController.set(-1);
        }
        else if (controlStick.getRawButton(conveyorOffButton)) {
            System.out.println("Conveyor Button off...");
            lowerConveyorController.set(0);
            upperConveyorController.set(0);
        } else if (controlStick.getRawButton(upperConveyorRevShortButton)) {//despite its name, it goes forward
            System.out.println("Making the upper conveyor go up for short time...");
            upperConveyorController.set(-1);
            Timer.delay(.6);
            upperConveyorController.set(0);
        }
        if (entrapmentIn.getTrigger()) {
          entStars = -0.4;
        }
        else if(entrapmentOut.getTrigger()) {
            entStars = 0.4;
        }
        else if (entrapmentOut.getRawButton(entrapmentOffButton)) {
            entStars = 0;
        }
         entrapmentController.set(entStars);
        //Timer.delay(0.005);
    }
}
    
