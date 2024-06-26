// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.Angle_Motor;
import frc.robot.subsystems.Shooter.Input_Mode;

public class DefaultShooter extends CommandBase {

  private final Shooter m_shooter;
  private final XboxController m_xboxController;

  private double shootSpeedLow;
  private double shootSpeedHigh;
  private double shootSpeedHighReal;
  private double idleSpeed;
  private double currentTargetSpeed;

  // private boolean atMinVert;
  private boolean atMaxLeft;
  private boolean atMaxRight;
  // private double horCt;

  /** Creates a new DefaultShooter. */
  public DefaultShooter(Shooter shooter, XboxController xboxController) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = shooter;
    m_xboxController = xboxController;
    addRequirements(m_shooter);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //idleSpeed = 6000; // 6000
    idleSpeed = 7800;
    // idleSpeed = 4000;
     //idleSpeed = 0;
    shootSpeedLow = 5400;
    // shootSpeedHigh = 9000;
    shootSpeedHighReal = 9100;

    // Set's shooter to idling speed, small percent of max velocity
    m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, idleSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   
    // Constantly updating boolean values for limits
    // atMinVert = m_shooter.atVerticalLimit();
    atMaxLeft = m_shooter.atLeftLimit();
    atMaxRight = m_shooter.atRightLimit();

    // Vertical Encoder Count For Soft Limit
    // vertCt = m_shooter.getVerticalCt();

    // Horizontal count for soft limit
    // horCt = m_shooter.getHorizontalAngle() * 14;

    if (Math.abs((currentTargetSpeed - m_shooter.getShooterSpeed()) / currentTargetSpeed) * 100 <= 4) {
      Constants.ShooterConstants.isReady = true;
    } else {
      Constants.ShooterConstants.isReady = false;
    }
    
    if (m_xboxController.getXButtonPressed()) {
      m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, shootSpeedLow);
      currentTargetSpeed = shootSpeedHigh;
    }

    if (m_xboxController.getXButtonReleased()) {
      //m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, idleSpeed);
      m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, idleSpeed);
    }

    if (m_xboxController.getYButtonPressed()) {
      m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, shootSpeedHighReal);
      currentTargetSpeed = shootSpeedHigh;
    }

    if (m_xboxController.getYButtonReleased()) {
      m_shooter.setSpeed(Input_Mode.Native, Angle_Motor.ShootMotors, idleSpeed);
    }    

    // Increases speed by set value every time Y is pressed, prints value on Smart
    // Dashboard, ONLY FOR TESTING
    // if (m_xboxController.getYButtonPressed()) {
    // shootSpeedHigh += 50;
    // currentTargetSpeed = shootSpeedHigh; 
    // }

    // Decreases speed by set value every time A is pressed, prints value on
    // Smart Dashboard, ONLY FOR TESTING
    // else if (m_xboxController.getBButtonPressed() ) {
    // shootSpeedHigh -= 50;
    // currentTargetSpeed = shootSpeedHigh; 
    // }

    if (m_xboxController.getRightX() >= 0.1 && !atMaxRight) {
    m_shooter.setSpeed(Input_Mode.PercentOut, Angle_Motor.HorizontalAngler,
    m_xboxController.getRightX()*0.5);
    }

    else if (m_xboxController.getRightX() <= -0.1 && !atMaxLeft) {
    m_shooter.setSpeed(Input_Mode.PercentOut, Angle_Motor.HorizontalAngler,
    m_xboxController.getRightX()*0.5);
    }

    else if ((m_xboxController.getRightX() <= 0.1 && m_xboxController.getRightX()
    >= 0)) {
    m_shooter.setSpeed(Input_Mode.PercentOut, Angle_Motor.HorizontalAngler, 0);
    }

    else if ((m_xboxController.getRightX() >= -0.1 && m_xboxController.getRightX()
    <= 0)) {
    m_shooter.setSpeed(Input_Mode.PercentOut, Angle_Motor.HorizontalAngler, 0);
    }

    Constants.ShooterConstants.currentShooterSpeed = currentTargetSpeed; 
    // SmartDashboard.putNumber("Current Shooter Speed", shootSpeedHigh);

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
