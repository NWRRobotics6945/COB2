/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public Joystick extremeJoystick;
  public Joystick gamepad;
  public Joystick arduinoLeonardo;
  
  public OI() {
  
  //Controllers
  extremeJoystick = new Joystick(0); //santiago

  gamepad = new Joystick(1); //not santiago

  //arduinoLeonardo = new Joystick(2); //also not santiago

  if (gamepad.getName().equals("Logitech Extreme 3D")) {
    int newjoy = gamepad.getPort();
    int newcon = extremeJoystick.getPort();
    gamepad = new Joystick(newcon);
    extremeJoystick = new Joystick(newjoy);

    }
/*
  if (gamepad.getName().equals("Arduino Leonardo")) {
    int newglove = gamepad.getPort();
    int newcon = arduinoLeonardo.getPort();
    gamepad = new Joystick(newcon);
    arduinoLeonardo = new Joystick(newglove);
    
  }

  if(extremeJoystick.getName().equals("Arduino Leonardo")){
    int newjoy = arduinoLeonardo.getPort();
    int newglove = extremeJoystick.getPort();
    extremeJoystick = new Joystick(newjoy);
    arduinoLeonardo = new Joystick(newglove);
  }

*/

 
}

public void fixJoy() {
  if (gamepad.getName().equals("Logitech Extreme 3D")) {
    int newjoy = gamepad.getPort();
    int newcon = extremeJoystick.getPort();
    gamepad = new Joystick(newcon);
    extremeJoystick = new Joystick(newjoy);
  }
}
}
