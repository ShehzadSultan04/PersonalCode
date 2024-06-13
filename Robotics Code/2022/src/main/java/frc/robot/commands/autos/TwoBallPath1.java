// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

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
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.commands.IdleTower;
import frc.robot.commands.IntakeDown;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Tower;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TwoBallPath1 extends ParallelRaceGroup {
  /** Creates a new TwoBallPath1. */
  public TwoBallPath1(Chassis chassis, Intake intake, Tower tower) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    chassis.resetOdometry(Constants.AutoConstants.twoBallPath1.getInitialPose());
    // RamseteController disabledRamsete = new RamseteController();
    // disabledRamsete.setEnabled(false);

    // Create a voltage constraint to ensure we don't accelerate too fast
    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(
            Constants.DriveConstants.ksVolts,
            Constants.DriveConstants.kvVoltSecondsPerMeter,
            Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
        Constants.DriveConstants.kDriveKinematics,
        10);

    // Create config for trajectory
    // TrajectoryConfig config = new TrajectoryConfig(
    //     Constants.AutoConstants.kMaxSpeedMetersPerSecond,
    //     Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
    //     // Add kinematics to ensure max speed is actually obeyed
    //     .setKinematics(Constants.DriveConstants.kDriveKinematics)
    //     // Apply the voltage constraint
    //     .addConstraint(autoVoltageConstraint);

    // // An example trajectory to follow. All units in meters.
    // Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
    //     // Start at the origin facing the +X direction
    //     new Pose2d(0, 0, new Rotation2d(0)),
    //     // Pass through these two interior waypoints, making an 's' curve path
    //     // List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
    //     List.of(new Translation2d(2, 0)),
    //     // End 3 meters straight ahead of where we started, facing forward
    //     new Pose2d(3, 0, new Rotation2d(0)),
    //     // Pass config
    //     config);

    RamseteCommand twoBallPath1 = new RamseteCommand(
        // exampleTrajectory,
        Constants.AutoConstants.twoBallPath1,
        chassis::getPose,
        new RamseteController(Constants.AutoConstants.kRamseteB,
        Constants.AutoConstants.kRamseteZeta),
        // disabledRamsete,
        new SimpleMotorFeedforward(
            Constants.DriveConstants.ksVolts,
            Constants.DriveConstants.kvVoltSecondsPerMeter,
            Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
        Constants.DriveConstants.kDriveKinematics,
        chassis::getWheelSpeeds,
        new PIDController(Constants.DriveConstants.kPDriveVel, 0, 0),
        new PIDController(Constants.DriveConstants.kPDriveVel, 0, 0),
        // RamseteCommand passes volts to the callback
        chassis::tankDriveVolts,
        chassis);
    addCommands(twoBallPath1.andThen(() -> chassis.tankDriveVolts(0, 0)), 
    new IntakeDown(intake), 
    new IdleTower(tower));
  }
}
