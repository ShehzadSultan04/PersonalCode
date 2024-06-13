// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Tower;

public class DefaultTower extends CommandBase {

  private final Tower m_tower;
  private final XboxController m_xboxController;

  boolean occupied;

  /** Creates a new AutoIndexer. */
  public DefaultTower(Tower indexer, XboxController xboxController) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_tower = indexer;
    addRequirements(m_tower);
    m_xboxController = xboxController;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    occupied = m_tower.isOccupied();

    if (m_tower.getEnt()) {
      m_tower.towerStop();
    } else if (m_xboxController.getLeftTriggerAxis() > 0.5) {
      m_tower.towerIdle();
    } else
      m_tower.towerStop();

    // Sends ball up the tower, shooter is already running and at speed
    if (m_xboxController.getRightTriggerAxis() > 0.5) {
      m_tower.towerUp();
      m_tower.occupied = false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
