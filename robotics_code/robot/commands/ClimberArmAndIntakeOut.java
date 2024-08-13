// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;

public class ClimberArmAndIntakeOut extends CommandBase {
  /** Creates a new ClimberArmAndIntakeOut. */
  Climber m_climber; 
  Intake m_intake; 
  XboxController m_XboxController;
  public ClimberArmAndIntakeOut(Climber climber, Intake intake, XboxController xboxController) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_climber = climber;
    m_intake = intake; 
    m_XboxController = xboxController; 
    addRequirements(m_climber, m_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intake.down();
    m_climber.extend();
    
    if (m_XboxController.getLeftY() < -0.2) {
      m_climber.up();
    }

    else if (m_XboxController.getLeftY() > 0.2) {
      m_climber.down();
    }

    else if (m_XboxController.getAButton() ){
    m_climber.climb();
    }
    else{
    m_climber.stop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.up();
    m_climber.bringIn();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
