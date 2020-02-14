/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;


public class Robot extends TimedRobot {

  //initialize variables
  private static double speed;
  private static double twist;
  private static double throttle;
  private static double crab;
  double hlgDriveVal;
  boolean hlgInit;
  private static double minSucc;
  private static double maxSucc;

  private static double setServo = 90;

  private static Timer timer = new Timer();

  private static int time;
  private static int maxTime;

  private static String period = "";

  private static String servoPos = "";

  private static double lift;

  private static double twistMulti = 1;

  private static boolean somethingWrong = false;
  private static boolean beginPark = false;

  private static int liftHeight;
  private static int HLGHeight;

  private static boolean flash = false;

  private OI OI = new OI();

  @Override
  public void robotInit() {
    OI.fixJoy();
    RobotMap.hlgLift.enableCurrentLimit(true);

    RobotMap.drivetrain.setRightSideInverted(false);

    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);

    minSucc = RobotMap.succyEncoder.getPosition();
    maxSucc = minSucc + 100;

    setServo = 90;
    RobotMap.leftServo.setAngle(setServo);
    RobotMap.rightServo.setAngle(setServo);


    RobotMap.drivetrain.setSafetyEnabled(false);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Succy Position", RobotMap.succyEncoder.getPosition());
    SmartDashboard.putNumber("Succy Manual", RobotMap.succyEncoder.getPosition());

    RobotMap.leftServo.setAngle(setServo);
    RobotMap.rightServo.setAngle(setServo);

      //get values from extreme joystick (santiago)
      speed = -OI.extremeJoystick.getRawAxis(1);
      twist = OI.extremeJoystick.getRawAxis(2) * twistMulti;
      throttle = (-OI.extremeJoystick.getRawAxis(3)+3)/4;
      crab = OI.extremeJoystick.getRawAxis(0);
      lift = -OI.gamepad.getRawAxis(5);

    //change twist multiplier
    if (OI.extremeJoystick.getPOV() != -1 || OI.extremeJoystick.getRawButton(3) || OI.extremeJoystick.getRawButton(5) || OI.extremeJoystick.getRawButton(4) || OI.extremeJoystick.getRawButton(6)) 
      twistMulti = 0.85;
    else
      twistMulti = 0.6;

      //add deadzone
      if ((speed < 0.1 && speed > 0) || (speed > 0.1 && speed < 0))
        speed = 0;
      if ((twist < 0.3 && twist > 0) || (twist > 0.3 && twist < 0))
        twist = 0;
      if ((crab < 0.1 && crab > 0) || (crab > 0.1 && crab < 0))
        crab = 0;
      //if ((lift < 0.1 && lift > 0) || (lift > 0.1 && lift < 0))
        //lift = 0;
      


    //Dashboard stuff
    time = maxTime - (int)timer.get();

    SmartDashboard.putString("Period", period);
    SmartDashboard.putString("PeriodBL", period);
    SmartDashboard.putString("PeriodBR", period);
    SmartDashboard.putNumber("Time", time);

    liftHeight = RobotMap.liftSensor.getPulseWidthPosition() / -1000;
    SmartDashboard.putNumber("Lift", liftHeight);
    HLGHeight = RobotMap.hlgSensor.getPulseWidthPosition() / -1000;
    SmartDashboard.putNumber("HLG", HLGHeight);


    if (RobotMap.leftServo.getAngle() > 45)
      servoPos = "UP";
    else
      servoPos = "DOWN";

    SmartDashboard.putString("Servos", servoPos);
    SmartDashboard.putBoolean("Flash", flash);
  }

  @Override
  public void disabledInit() {
    period = "DISABLED";
    timer.stop();
    hlgInit = false;
    OI.fixJoy();
    somethingWrong = false;
    beginPark = false;
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();

  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
    period = "SANDSTORM";
    maxTime = 15;
    OI.fixJoy();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    timer.start();
    maxTime = 135;
    time = 135;
    period = "TELEOP";
    OI.fixJoy();
  }


  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    //--DRIVETRAIN--

    //limit values by throttle amount
    speed *= throttle;
    twist *= throttle; //extra reduction given -- not final
    crab *= throttle * 0.7; //extra reduction given -- not final

    //activate normal driving or crab driving
    if (!OI.extremeJoystick.getRawButton(2)) //when side button not pressed
      if (OI.extremeJoystick.getRawButton(1)) //when trigger button pressed
        Drivetrain.crab(crab);
      else
        Drivetrain.set(speed, twist);
    else
      Drivetrain.set(0, 0);


    //--HLG--
    //lift
   if(OI.gamepad.getRawButton(9)) //left joystick click
      hlgInit = true;

    if (OI.gamepad.getPOV() == -1){
     if(hlgInit){
      RobotMap.hlgLift.set(OI.gamepad.getRawAxis(0)*.5); //left joystick left(up)/right
     }else{//extremeJStick lift control:
     if (OI.extremeJoystick.getRawButton(11))
       RobotMap.hlgLift.set(1);
     else if (OI.extremeJoystick.getRawButton(12))
       RobotMap.hlgLift.set(-1); 
     else
      RobotMap.hlgLift.set(0);
    }
  }


    //drive
    if (OI.extremeJoystick.getRawButton(2)) //when side button pressed
      RobotMap.hlgDrive.set(OI.extremeJoystick.getRawAxis(1) * 0.5); //main forward/back
    else
      RobotMap.hlgDrive.set(0);


    //--LIFT--
    //right joystick
    if (OI.gamepad.getPOV() == -1){ //only runs lift normally if dpad isnt pressed
      double liftSpeed = -lift; //right joystick up/down
      double liftLimit = 0.85;
    
      RobotMap.lift.set(liftSpeed * liftLimit);

  }


    //Servos

    if (OI.extremeJoystick.getRawButton(7)) //button 7 on exJoy drops skis
      setServo = 0;
    if (OI.extremeJoystick.getRawButton(8)) //button 8 holds skis
      setServo = 90;

    //--INTAKE--
    if(OI.gamepad.getRawButton(5)==true)     //Left shoulder -- intake in
      RobotMap.intake.set(-1);
    else if (OI.gamepad.getRawButton(6))  //Right shoulder -- intake out
      RobotMap.intake.set(1);
    else if (OI.gamepad.getRawAxis(3)!=0)
      RobotMap.intake.set(OI.gamepad.getRawAxis(3)*.5);//soft out
       else
        RobotMap.intake.set(0);


    //--SUCCY--


    if (OI.gamepad.getRawButton(4)) { //Y button - unsucc
      if (RobotMap.succyEncoder.getPosition() > minSucc || OI.gamepad.getRawButton(8)) //start button -- manual override
        RobotMap.succy.set(-1);
      else
        RobotMap.succy.set(0);
    } else {
      if (OI.gamepad.getRawButton(1)) { //A button - succ
        if (RobotMap.succyEncoder.getPosition() < maxSucc || OI.gamepad.getRawButton(8)) //start button -- manual override)
          RobotMap.succy.set(1);
        else
          RobotMap.succy.set(0);
      }else
        RobotMap.succy.set(0);
    }

    if (OI.gamepad.getRawButton(7)){ //recalibrate succy min/max -- back button
      minSucc = RobotMap.succyEncoder.getPosition();
      maxSucc = minSucc +  100;
    }


      //--WRIST--
      

      RobotMap.wrist.set(OI.gamepad.getRawAxis(1)); //left joystick up/down
    
    //--Cool Park-- "The Button"
    //parks robot with one joystick

    if (OI.gamepad.getPOV() != -1) { //only runs if dpad is pressed
      double parkSpeed = lift; //right joystick up/dowm
      double mainMulti = 0.6; //main lift speed
      double hlgMulti = 0.84; //hlg lift speed

      RobotMap.lift.set(-parkSpeed * mainMulti);
      RobotMap.hlgLift.set(-parkSpeed * hlgMulti);
    }

    //--Dashboard Timer--
    if (time < 25 && period != "SANDSTORM"){
      if (time % 2 == 0){
        period = "PARK";
        flash = false;
      }else{
        period = "";
        flash = true;
      }
    }
  }
}
