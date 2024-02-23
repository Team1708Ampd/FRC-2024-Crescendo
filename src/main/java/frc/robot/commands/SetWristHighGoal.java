// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class SetWristHighGoal extends Command {
  /** Creates a new SetWristHighGoal. */
  public SetWristHighGoal() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.wristSub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(Robot.wristSub.getAngle() > 71) {
      Robot.wristSub.setSpeed(0.4);
    } else if(Robot.wristSub.getAngle() < 69) {
      Robot.wristSub.setSpeed(-0.4);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.wristSub.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs(Robot.wristSub.getAngle() - 70) <= 1) {
      return true;
    }
    return false;
  }
}
