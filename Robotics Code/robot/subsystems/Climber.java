// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ResourceBundle.Control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {
  private final WPI_TalonFX m_RightArm;
  private final WPI_TalonFX m_LeftArm;

  private final DoubleSolenoid m_leftAngler;
  private final DoubleSolenoid m_rightAngler;

  // private final DigitalInput downLimitA;
  // private final DigitalInput downLimitB;
  // private final DigitalInput upperLimitA;
  // private final DigitalInput upperLimitB;

  private final DigitalInput downLimitSwitchA;
  private final DigitalInput downLimitSwitchB;

  // private double eEncoderCt;
  // private boolean isMax = false;
  private boolean isMinA = false;
  private boolean isMinB = false;
  private boolean extended = false;

  private boolean goingUp = false;

  /** Creates a new Climber. */
  public Climber() {
    m_RightArm = new WPI_TalonFX(Constants.CANID.kClimberExtenderM); //Right
    m_LeftArm = new WPI_TalonFX(Constants.CANID.kClimberExtenderS); //Left

    m_RightArm.enableVoltageCompensation(true);
    m_LeftArm.enableVoltageCompensation(true);
    m_RightArm.configVoltageCompSaturation(11);
    m_LeftArm.configVoltageCompSaturation(11);

    // m_slaveArm.follow(m_Arm);
    m_RightArm.setInverted(true);

    m_RightArm.setNeutralMode(NeutralMode.Brake);
    m_LeftArm.setNeutralMode(NeutralMode.Brake);

    m_LeftArm.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    m_RightArm.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    // m_Arm.config_kP(0, Constants.ClimberC.kP); Not Needed?

    // downLimitA = new DigitalInput(Constants.DIO.kLeftMin);
    // upperLimitA = new DigitalInput(Constants.DIO.kLeftMax);

    downLimitSwitchA = new DigitalInput(Constants.DIO.kLeftMin); // Left
    downLimitSwitchB = new DigitalInput(Constants.DIO.kRightMin); //Right

    // downLimitB = new DigitalInput(Constants.DIO.kRightMin);
    // upperLimitB = new DigitalInput(Constants.DIO.kRightMax);

    m_leftAngler = new DoubleSolenoid(Constants.CANID.kPCM, PneumaticsModuleType.REVPH,
        Constants.PCM.kLeftClimberForward,
        Constants.PCM.kLeftClimberReverse);
    m_rightAngler = new DoubleSolenoid(Constants.CANID.kPCM, PneumaticsModuleType.REVPH,
        Constants.PCM.kRightClimberForward, 
        Constants.PCM.kRightClimberReverse);
  }

  // public boolean isMax() {
  //   return isMax;
  // }

  public boolean isMinA() {
    return isMinA;
  }

  public boolean isMinB() {
    return isMinB;
  }

  public boolean isExtended() {
    return extended;
  }

  public void up() {
    m_RightArm.set(ControlMode.PercentOutput, 0.5);
    m_LeftArm.set(ControlMode.PercentOutput, 0.54);
    goingUp = true;
  }

  public void down() {
    if(isMinA)
      m_LeftArm.set(0);
    else 
      m_LeftArm.set(ControlMode.PercentOutput, -0.4);
    if(isMinB)
      m_RightArm.set(0);
    else
      m_RightArm.set(ControlMode.PercentOutput, -0.4);
    goingUp = false;
  }

  public void climb() {
    if(isMinA)
      m_LeftArm.set(0);
    else 
      m_LeftArm.set(ControlMode.PercentOutput, -0.7);
    if(isMinB)
      m_RightArm.set(0);
    else
      m_RightArm.set(ControlMode.PercentOutput, -0.75);
    
    goingUp = false;
  }

  public void stop() {
    m_RightArm.set(ControlMode.PercentOutput, 0);
    m_LeftArm.set(ControlMode.PercentOutput, 0);
    // goingUp = false;
    
  }

  public void extend() {
    m_leftAngler.set(Value.kForward);
    m_rightAngler.set(Value.kForward);
    extended = true;
  }

  public void bringIn() {
    m_leftAngler.set(Value.kReverse);
    m_rightAngler.set(Value.kReverse);
    extended = false;
  }

  @Override
  public void periodic() {

    isMinA = downLimitSwitchA.get();
    isMinB = downLimitSwitchB.get();


    // if (downLimitA.get() && !goingUp){
    // m_Arm.set(0);
    // }

    // if (upperLimitA.get() && goingUp){
    // m_Arm.set(0);
    // }

    // if (downLimitB.get() && !goingUp ){
    // m_Arm.set(0);
    // }

    // if (upperLimitB.get() && goingUp){
    // m_Arm.set(0);
    // }
    // SmartDashboard.putBoolean("Going Up", goingUp);
    // if (isMinA && !goingUp) {
    //   m_slaveArm.set(0);
    // }

    // if (isMinB && !goingUp) {
    //   m_Arm.set(0);
    // }

    SmartDashboard.putBoolean("Left Arm Limit", downLimitSwitchA.get());
    SmartDashboard.putBoolean("Right Arm Limit", downLimitSwitchB.get());

  }
}
