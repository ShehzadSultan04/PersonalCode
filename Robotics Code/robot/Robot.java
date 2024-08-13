// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private SendableChooser<Command> m_sendableChooser;

  String trajectoryJSON1 = "paths/PickUpBall1.wpilib.json";
  Trajectory trajectory1 = new Trajectory();

  String trajectoryJSON2 = "paths/PickUpBall2From1.wpilib.json";
  Trajectory trajectory2 = new Trajectory();

  String trajectoryJSON3 = "paths/PickUpBall3From2.wpilib.json";
  Trajectory trajectory3 = new Trajectory();

  String trajectoryJSON4 = "paths/MoveFromBall3ToShoot.wpilib.json";
  Trajectory trajectory4 = new Trajectory();

  String twoBallPathTrajString = "paths/TwoBallPath1.wpilib.json";
  Trajectory twoBallPathTraj = new Trajectory();

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.

    m_robotContainer = new RobotContainer();
    m_sendableChooser = new SendableChooser<>();

    try {
      Path trajectoryPath1 = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON1);
      trajectory1 = TrajectoryUtil.fromPathweaverJson(trajectoryPath1);
      System.out.println("Caught Path 1");
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON1, ex.getStackTrace());
    }
    Constants.AutoConstants.pickUpBall1 = trajectory1;

    try {
      Path trajectoryPath2 = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON2);
      trajectory2 = TrajectoryUtil.fromPathweaverJson(trajectoryPath2);
      System.out.println("Caught Path 2");
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON2, ex.getStackTrace());
    }
    Constants.AutoConstants.pickUpBall2From1 = trajectory2;

    try {
      Path trajectoryPath3 = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON3);
      trajectory3 = TrajectoryUtil.fromPathweaverJson(trajectoryPath3);
      System.out.println("Caught Path 3");
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON3, ex.getStackTrace());
    }
    Constants.AutoConstants.pickUpBall3From2 = trajectory3;

    try {
      Path trajectoryPath4 = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON4);
      trajectory4 = TrajectoryUtil.fromPathweaverJson(trajectoryPath4);
      System.out.println("Caught Path 4");
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON4, ex.getStackTrace());
    }
    Constants.AutoConstants.travelToShootPoint = trajectory4;

    try {
      Path twoBallPathJson = Filesystem.getDeployDirectory().toPath().resolve(twoBallPathTrajString);
      twoBallPathTraj = TrajectoryUtil.fromPathweaverJson(twoBallPathJson);
      System.out.println("Caught 2 Ball Path");
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + twoBallPathTrajString, ex.getStackTrace());
    }
    Constants.AutoConstants.twoBallPath1 = twoBallPathTraj;

    m_sendableChooser.setDefaultOption("3 Ball", m_robotContainer.FiveBallPath());
    m_sendableChooser.addOption("2 Ball Left", m_robotContainer.TwoBallPath());
    m_sendableChooser.addOption("2 Ball Right", m_robotContainer.TwoBallPathRight());
    m_sendableChooser.addOption("1 Low Move Back Timed", m_robotContainer.OneBallLowTaxiTimed());

    SmartDashboard.putData("Auto Selector", m_sendableChooser);

    // CameraServer.startAutomaticCapture();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.

    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    // m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    m_autonomousCommand = m_sendableChooser.getSelected();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
