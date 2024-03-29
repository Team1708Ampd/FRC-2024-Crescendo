// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.robot.commands.ArmDown;
import frc.robot.commands.ArmUp;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.OuttakeCommand;
import frc.robot.commands.SetArmToBottom;
import frc.robot.commands.SetArmToTop;
import frc.robot.commands.Shooter;
import frc.robot.commands.WristDown;
import frc.robot.commands.WristUp;
import frc.robot.generated.TunerConstants;

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

  // Slew rate limiter used to limit "acceleration" of the controller speicifed drive input
  private SlewRateLimiter ctrlYLimiter = new SlewRateLimiter(0.5);
  private SlewRateLimiter ctrlXLimiter = new SlewRateLimiter(0.5);

  private double MaxSpeed = 6; // 6 meters per second desired top speed
  private double MaxAngularRate = 4 * Math.PI; // 3/4 of a rotations per second max angular velocity
  private final SwerveRequest.RobotCentric drive = new SwerveRequest.RobotCentric()
      .withDeadband(MaxSpeed * 0.2).withRotationalDeadband(MaxAngularRate * 0.2) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
  private final SendableChooser<Command> autoChooser;


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {
    //create chooser for different autos

    // Configure the trigger bindings
    configureBindings();
    NamedCommands.registerCommand("Intake", new IntakeCommand());
    NamedCommands.registerCommand("Outtake", new OuttakeCommand());
    NamedCommands.registerCommand("Wrist Up", new WristUp());
    NamedCommands.registerCommand("Wrist Down", new WristDown());
    NamedCommands.registerCommand("Arm Up", new ArmUp());
    NamedCommands.registerCommand("Arm Down", new ArmDown());
    NamedCommands.registerCommand("Shoot", new Shooter());

    autoChooser = AutoBuilder.buildAutoChooser(); // Default auto will be `Commands.none()`
    SmartDashboard.putData("Auto Mode", autoChooser);
  }

  private void configureBindings()
  {
    drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
    drivetrain.applyRequest(() -> drive.withVelocityX(ctrlYLimiter.calculate(-joystick.getLeftY()) * MaxSpeed) // Drive forward with
                                                                                      // negative Y (forward)
        .withVelocityY(ctrlXLimiter.calculate(-joystick.getLeftX()) * MaxSpeed) // Drive left with negative X (left)
        .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
    ));
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    // joystick.y().whileTrue(new ArmDown());
    // joystick.leftBumper().whileTrue(new WristUp());
    // joystick.rightBumper().whileTrue(new WristDown());
    // joystick.x().whileTrue(new IntakeCommand());
    // joystick.a().whileTrue(new ArmUp());
    // joystick.b().whileTrue(new OuttakeCommand());
    // joystick.rightTrigger().whileTrue(new Shooter()); 
    // joystick.start().onTrue(new SetArmToBottom());

    joystick.leftTrigger().whileTrue(new IntakeCommand());
    joystick.leftBumper().whileTrue(new OuttakeCommand());
    joystick.rightTrigger().whileTrue(new Shooter());

    mechanisms.rightTrigger().whileTrue(new WristDown());
    mechanisms.leftTrigger().whileTrue(new ArmDown());
    mechanisms.rightBumper().whileTrue(new WristUp());
    mechanisms.leftBumper().whileTrue(new ArmUp());
    mechanisms.y().onTrue(new SetArmToTop());
    mechanisms.a().onTrue(new SetArmToBottom());
    
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