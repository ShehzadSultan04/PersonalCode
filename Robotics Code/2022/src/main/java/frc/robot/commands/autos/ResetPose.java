// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class ResetPose extends CommandBase {
  /** Creates a new ResetPose. */
  Chassis m_chassis;
  Pose2d m_pose;
  public ResetPose(Chassis chassis, Pose2d pose) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_chassis = chassis;
    m_pose = pose;
    addRequirements(m_chassis);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_chassis.resetOdometry(m_pose);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_chassis.resetOdometry(m_pose);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
