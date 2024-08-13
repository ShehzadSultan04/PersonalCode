// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class ShooterConsUpDown extends CommandBase {
  /** Creates a new ShooterConsUpDown. */
  Shooter m_shooter; 
  String m_change; 
  public ShooterConsUpDown(Shooter shooter, String change) {
    m_shooter = shooter;
    m_change = change;
    addRequirements(m_shooter);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_change == "Up"){
      Constants.ShooterConstants.shooterConstant += 0.02;
    }

    if (m_change == "Down"){
      Constants.ShooterConstants.shooterConstant -= 0.02;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
