/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.joshsCode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author Warbots
 */
public class CameraCode {
    private double mutiplier = 1.0;
    public static ParticleAnalysisReport getTarget() {
        ParticleAnalysisReport[] reports = new ParticleAnalysisReport[0];
        ParticleAnalysisReport topBasket = null;
        AxisCamera camera = AxisCamera.getInstance();
        if (camera == null) {
            return null;
        } else {
            try {
                //drive.tankDrive(0,0);
                boolean itWorked;
                ColorImage image = null;
                
                    itWorked = false;
                    while (!itWorked) {
                        try {    
                            itWorked = true;
                            image = camera.getImage();     // comment if using stored images
                        }
                        catch (AxisCameraException e) {
                            itWorked = false;
                            System.out.println("Nope!");
                        }
                    }
                
                
                //ColorImage image;                           // next 2 lines read image from flash on cRIO
                //image =  new RGBImage("/10ft2.jpg");
                BinaryImage thresholdImage = image.thresholdRGB(0, 45, 0, 45, 15, 255);   // keep only blue objects
                BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
                BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
                CriteriaCollection cc = new CriteriaCollection();      // create the criteria for the particle filter
                cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
                cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
                BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles
                reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
                System.out.println(reports.length);
                topBasket = reports[0];
                for (int i = 0; i < reports.length; i++) { 
                    //System.out.println(reports[i]); // print results
                    if (reports[i].center_mass_x < topBasket.center_mass_x) {
                        topBasket = reports[i];
                    }
                }
                System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());

                /**
                 * all images in Java must be freed after they are used since they are allocated out
                 * of C data structures. Not calling free() will cause the memory to accumulate over
                 * each pass of this loop.
                 */
                filteredImage.free();
                convexHullImage.free();
                bigObjectsImage.free();
                thresholdImage.free();
                image.free();
            } catch (NIVisionException e) {
                e.printStackTrace();
            }
            return topBasket;
        }
    }
    
//   public double pidGet() {
//      return mutiplier * this.getTarget().center_mass_x_normalized;
//   }
//   
//   public void setLR(boolean seter) { //set 0 for left and 1 for right
//       if(seter) {
//          mutiplier = -1.0;
//       }
//       else {
//           mutiplier = 1.0;
//       }
//   }
    
}
