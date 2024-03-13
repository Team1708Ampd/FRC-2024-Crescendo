// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AmpPreset;
import frc.robot.commands.ArmDown;
import frc.robot.commands.ArmUp;
import frc.robot.commands.AutoIntakeCommand;
import frc.robot.commands.AutoShooter;
import frc.robot.commands.ClimbersDown;
import frc.robot.commands.ClimbersUp;
import frc.robot.commands.Feed;
import frc.robot.commands.FullPowerShot;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.IntakePreset;
import frc.robot.commands.LowPowerShot;
import frc.robot.commands.OuttakeCommand;
import frc.robot.commands.SetArmToBottom;
import frc.robot.commands.SetArmToTop;
import frc.robot.commands.SetWristAmp;
import frc.robot.commands.SetWristHighGoal;
import frc.robot.commands.SetWristIntake;
import frc.robot.commands.Shooter;
import frc.robot.commands.SpeakerPreset;
import frc.robot.commands.TrapPreset;
import frc.robot.commands.WristDown;
import frc.robot.commands.WristUp;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CANdleSystem;
import java.io.File;

import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{

  // The robot's subsystems and commands are defined here...

  // CommandJoystick rotationController = new CommandJoystick(1);
  // Replace with CommandPS4Controller or CommandJoystick if needed

  // CommandJoystick driverController   = new CommandJoystick(3);//(OperatorConstants.DRIVER_CONTROLLER_PORT);
  public final CommandXboxController joystick = new CommandXboxController(0); // My joystick
  public final CommandXboxController mechanisms = new CommandXboxController(1);
  private final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; 
                                                                 
  private final SendableChooser<Command> autoChooser;

  private double MaxSpeed = 9; // 6 meters per second desired top speed
  private double MaxAngularRate = 5 * Math.PI; // 3/4 of a rotation per second max angular velocity
  private final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
 
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {
    //create chooser for different autos

    // Configure the trigger bindings
    configureBindings();
    NamedCommands.registerCommand("Intake", new AutoIntakeCommand());
    NamedCommands.registerCommand("Outtake", new OuttakeCommand());
    NamedCommands.registerCommand("Feed", new Feed());
    NamedCommands.registerCommand("Shoot", new AutoShooter());

    NamedCommands.registerCommand("IntakePosition", new IntakePreset());
    NamedCommands.registerCommand("SpeakerPosition", new SpeakerPreset());
    NamedCommands.registerCommand("AmpPosition", new AmpPreset());
    NamedCommands.registerCommand("TrapPosition", new TrapPreset());

    

    autoChooser = AutoBuilder.buildAutoChooser(); // Default auto will be `Commands.none()`
    SmartDashboard.putData("Auto Chooser", autoChooser);
    }

  private void configureBindings()
  {
    drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
    drivetrain.applyRequest(() -> drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with
                                                                                      // negative Y (forward)
        .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
        .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
    ));
    

    Trigger beam = new Trigger(() -> IntakeSubsystem.getBeam());
    Trigger topSwitch = new Trigger (() -> ArmSubsystem.getTopSwitch());
    Trigger bottomSwitch = new Trigger (() -> ArmSubsystem.getBottomSwitch());

    joystick.leftTrigger().and(beam).and(joystick.rightTrigger().negate()).whileTrue(new IntakeCommand());
    joystick.leftTrigger().and(joystick.rightTrigger()).whileTrue(new IntakeCommand());
    joystick.rightTrigger().and(joystick.start()).whileTrue(new LowPowerShot());
    joystick.leftBumper().whileTrue(new OuttakeCommand());
    joystick.rightTrigger().and(joystick.start().negate()).whileTrue(new Shooter());
    joystick.rightTrigger().and(joystick.back()).whileTrue(new FullPowerShot());

    mechanisms.rightTrigger().whileTrue(new WristDown());
    mechanisms.leftTrigger().and(bottomSwitch).whileTrue(new ArmDown());
    mechanisms.rightBumper().whileTrue(new WristUp());
    mechanisms.leftBumper().and(topSwitch).whileTrue(new ArmUp());
    
    mechanisms.x().onTrue(new TrapPreset());
    mechanisms.a().onTrue(new IntakePreset());
    mechanisms.y().onTrue(new AmpPreset());
    mechanisms.b().onTrue(new SpeakerPreset());
    
    joystick.back().whileTrue(new ClimbersUp());
    joystick.start().whileTrue(new ClimbersDown());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}