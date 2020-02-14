/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Drivetrain extends Subsystem {

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  //Normal driving
  public static void set(double speed, double twist){
    RobotMap.drivetrain.arcadeDrive(twist, speed);
  }

  //Crab driving
  public static void crab(double side){
    //Go in same direction
    RobotMap.frontL.set(side);
    RobotMap.backR.set(-side);

    //Go in opposite direction as above
    RobotMap.frontR.set(side);
    RobotMap.backL.set(-side);
  }
}
