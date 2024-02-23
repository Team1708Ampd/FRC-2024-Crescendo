// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class SetArmToTop extends Command {
  /** Creates a new SetArmToBottom. */
  public SetArmToTop() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.armSub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(Robot.armSub.getPosition() < 201) {
      Robot.armSub.setSpeed(0.6);
    } else if(Robot.armSub.getPosition() > 199) {
      Robot.armSub.setSpeed(-0.6);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.armSub.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs(200 - Robot.armSub.getPosition()) <= 1) {
      return true;
    }
    return false;
  }
}
