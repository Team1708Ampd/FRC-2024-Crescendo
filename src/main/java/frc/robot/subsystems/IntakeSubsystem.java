// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */
  CANSparkMax intakemotor=new CANSparkMax(11, MotorType.kBrushless);
  public IntakeSubsystem() {}
  public void setpower(double power){
    intakemotor.set(power);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
