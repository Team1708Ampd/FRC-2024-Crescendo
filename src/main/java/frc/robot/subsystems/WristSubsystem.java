// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WristSubsystem extends SubsystemBase {
  /** Creates a new WristSubsystem. */
  CANSparkMax Wrist = new CANSparkMax (10, MotorType.kBrushless); 
  DutyCycleEncoder encoder = new DutyCycleEncoder(0);

  public WristSubsystem() {
    Wrist.setIdleMode(IdleMode.kBrake);
  }

  public double getAngle() {
    return encoder.getAbsolutePosition();
  }
  
  public void setSpeed(double speed){
    Wrist.set(speed);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
