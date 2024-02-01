// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  CANSparkMax leftArm = new CANSparkMax(0, MotorType.kBrushless);
  CANSparkMax rightArm = new CANSparkMax(1, MotorType.kBrushless);
  public ArmSubsystem() {}

  public void setSpeed(double speed){
    leftArm.set(speed);
    rightArm.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}