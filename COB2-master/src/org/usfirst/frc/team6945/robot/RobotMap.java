/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  //DRIVETRAIN
  public static CANSparkMax frontL = new CANSparkMax(2, MotorType.kBrushless);
  public static CANSparkMax frontR = new CANSparkMax(5, MotorType.kBrushless);
  public static CANSparkMax backL = new CANSparkMax(4, MotorType.kBrushless);
  public static CANSparkMax backR = new CANSparkMax(1, MotorType.kBrushless);

  public static SpeedControllerGroup leftSide = new SpeedControllerGroup(frontL, backL);
  public static SpeedControllerGroup rightSide = new SpeedControllerGroup(frontR, backR);

  public static DifferentialDrive drivetrain = new DifferentialDrive(leftSide, rightSide);

  //HLG
  public static WPI_TalonSRX hlgDrive = new WPI_TalonSRX(0);
  public static WPI_TalonSRX hlgLift = new WPI_TalonSRX(2);
  public static SensorCollection hlgSensor = hlgLift.getSensorCollection();

  //succy succy
  public static CANSparkMax succy = new CANSparkMax(8, MotorType.kBrushless);
  public static CANEncoder succyEncoder = new CANEncoder(succy);


  //Intake
  public static WPI_TalonSRX wrist = new WPI_TalonSRX(3);
  public static WPI_TalonSRX intake = new WPI_TalonSRX(5);

  //Lift
  public static WPI_TalonSRX lift = new WPI_TalonSRX(4);
  public static SensorCollection liftSensor = lift.getSensorCollection();

  //Servos
  public static Servo rightServo = new Servo(0);
  public static Servo leftServo = new Servo(1);



}
