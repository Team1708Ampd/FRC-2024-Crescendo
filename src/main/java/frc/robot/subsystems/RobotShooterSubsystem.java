// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotShooterSubsystem extends SubsystemBase {
  /** Creates a new RobotShooter. */
 TalonFX ShooterMotor = new TalonFX(3); 
  public RobotShooterSubsystem() {}
public void setPower(double power){
  ShooterMotor.set(power);
}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
