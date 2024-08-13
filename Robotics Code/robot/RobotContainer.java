// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.commands.DefaultTower;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.DefaultIntake;
import frc.robot.commands.DefaultShooter;
import frc.robot.commands.AutoAim;
// import frc.robot.commands.AutoAimChassis;
// import frc.robot.commands.ClimberArmAndIntakeOut;
import frc.robot.commands.DefaultClimber;
import frc.robot.commands.JoystickDriving;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.ShootAndMoveBackPar;
import frc.robot.commands.ShooterConsUpDown;
import frc.robot.commands.ToggleGear;
import frc.robot.commands.autos.FiveBallPath1;
import frc.robot.commands.autos.ShootAndMoveBack;
import frc.robot.commands.autos.TwoBallPathRight;
import frc.robot.commands.autos.TwoballPath;
import frc.robot.commands.autos.FiveBallAuto;
import frc.robot.subsystems.Tower;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  private final Shooter m_shooter = new Shooter();
  private final Tower m_tower = new Tower();
  private final Intake m_intake = new Intake();
  private final Chassis m_chassis = new Chassis();
  private final Limelight m_limelight = new Limelight();
  private final Climber m_climber = new Climber();

  // private final CameraServo m_servo = new CameraServo();
  // private final TempClimber m_climber = new TempClimber();

  private final Joystick m_leftStick = new Joystick(Constants.Joystick.leftStick);
  private final Joystick m_rightStick = new Joystick(Constants.Joystick.rightStick);
  private final XboxController m_xboxController = new XboxController(2);

  private final JoystickButton m_gearShiftButton = new JoystickButton(m_leftStick, Constants.Joystick.gearShiftButton);
  private final JoystickButton m_intakeOutButton = new JoystickButton(m_leftStick, Constants.Joystick.intakeOutButton);

  private final JoystickButton m_upConstant = new JoystickButton(m_rightStick, Constants.Joystick.upShooterConstant);
  private final JoystickButton m_downConstant = new JoystickButton(m_rightStick, Constants.Joystick.downShooterConstant);


  // private final Button m_intakeBallButton = new Button(() -> m_xboxController.getLeftTriggerAxis() > 0.75);

  private final JoystickButton m_autoAim = new JoystickButton(m_xboxController, 5); // Change this to x button

  // private final JoystickButton m_climberAndIntakeOut = new JoystickButton(m_xboxController, 6);

  

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    m_chassis.setDefaultCommand(new JoystickDriving(m_chassis, m_leftStick, m_rightStick));
    m_shooter.setDefaultCommand(new DefaultShooter(m_shooter, m_xboxController));
    m_intake.setDefaultCommand(new DefaultIntake(m_intake, m_xboxController));
    m_tower.setDefaultCommand(new DefaultTower(m_tower, m_xboxController));
    m_climber.setDefaultCommand(new DefaultClimber(m_climber, m_xboxController));

    // m_servo.setDefaultCommand(new ToggleServo(m_servo, m_leftStick)); //Was
    // removed between Bridgewater and Monty

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    m_gearShiftButton.whenPressed(new ToggleGear(m_chassis));
    // m_intakeBallButton.whenPressed(new IntakeDown(m_intake));
    m_intakeOutButton.whileHeld(new ReverseIntake(m_intake, m_tower, m_leftStick));
    m_autoAim.whileHeld(new AutoAim(m_shooter, m_limelight));
    // m_autoAim.whileHeld(new AutoAimChassis(m_chassis, m_shooter, m_limelight));
    m_upConstant.whenPressed(new ShooterConsUpDown(m_shooter, "Up"));
    m_downConstant.whenPressed(new ShooterConsUpDown(m_shooter, "Down"));
    // m_climberAndIntakeOut.whileHeld(new ClimberArmAndIntakeOut(m_climber, m_intake, m_xboxController));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

  // public Command getAutonomousCommand() {
  // return m_autoCommand;
  // }

  public Command OneBallLowTaxiTimed(){
    return new ShootAndMoveBack(m_chassis, m_shooter, m_tower);
  }

  public Command FiveBallPath(){
    m_chassis.resetOdometry(Constants.AutoConstants.pickUpBall1.getInitialPose());
    return new FiveBallAuto(m_intake, m_chassis, m_tower, m_shooter, m_limelight);
  }

  public Command TwoBallPath(){
    m_chassis.resetOdometry(Constants.AutoConstants.twoBallPath1.getInitialPose());
    return new TwoballPath(m_intake, m_chassis, m_tower, m_shooter, m_limelight);
  }

  public Command TwoBallPathRight(){
    m_chassis.resetOdometry(Constants.AutoConstants.twoBallPath1.getInitialPose());
    return new TwoBallPathRight(m_intake, m_chassis, m_tower, m_shooter, m_limelight);
  }


  public Command getAutonomousCommand() {

    // Reset odometry to the starting pose of the trajectory.
    m_chassis.resetOdometry(Constants.AutoConstants.pickUpBall1.getInitialPose());

    // Run path following command, then stop at the end.
    //return new RamsetePath(m_chassis);
    return new FiveBallAuto(m_intake, m_chassis, m_tower, m_shooter, m_limelight);
  }
}