// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  TalonFX leftArm = new TalonFX(8);
  TalonFX rightArm = new TalonFX(9);
  DutyCycleEncoder encoder = new DutyCycleEncoder(1);
  static DigitalInput topSwitch = new DigitalInput(3); 
  static DigitalInput bottomSwitch = new DigitalInput(4); 

  public ArmSubsystem() {
    leftArm.setNeutralMode(NeutralModeValue.Brake);
    rightArm.setNeutralMode(NeutralModeValue.Brake);
  }

  public void setSpeed(double speed){
    leftArm.set(-speed);
    rightArm.set(speed);
  }

  public double getPosition() {
    return encoder.getAbsolutePosition() * 360;
  }

  public static boolean getTopSwitch(){
    return topSwitch.get();
  }

  public static boolean getBottomSwitch(){
    return bottomSwitch.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    System.out.println(getPosition() + " ARM");
  }
}
