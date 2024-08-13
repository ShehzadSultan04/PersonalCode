// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.Angle_Motor;
import frc.robot.subsystems.Shooter.Input_Mode;

public class AutoAimChassis extends CommandBase {
  /** Creates a new AutoAim. */
  double startTime;
  private Chassis m_chassis;
  private Shooter m_shooter; 
  private Limelight m_limelight; 
  double tx;
  double horizontalAdjustment; //adjusmtnet in native units
  
  double regression(double dist){
    // return 8433 - 49.3 * dist + 0.394 * dist * dist - 3.25E-4 * dist * dist * dist;
    return (7455 -28.2 * dist + 0.248 * dist * dist) * Constants.ShooterConstants.shooterConstant;
  }
  public AutoAimChassis(Chassis chassis, Shooter shooter, Limelight limelight) {
    m_chassis = chassis;
    m_shooter = shooter; 
    m_limelight = limelight;
    addRequirements(m_chassis, m_shooter, m_limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_chassis.turnTo(m_limelight.getTx());
    m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, regression(m_limelight.getDist()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("It Should Zero Here");
    Constants.ShooterConstants.prevHorizontalAdjustment = -horizontalAdjustment;
    

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Timer.getFPGATimestamp() - startTime >= Constants.ShooterConstants.stallTime;
  }
}
