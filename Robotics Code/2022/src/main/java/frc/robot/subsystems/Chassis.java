// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.StateSpaceUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class Chassis extends SubsystemBase {
  /** Creates a new Chassis. */

  private WPI_TalonFX m_leftMaster;
  private WPI_TalonFX m_leftSlaveA;
  private MotorControllerGroup m_left;

  private WPI_TalonFX m_rightMaster;
  private WPI_TalonFX m_rightSlaveA;
  private MotorControllerGroup m_right;

  // private DifferentialDrive m_drive;

  private final DifferentialDriveOdometry m_odometry;

  private final DoubleSolenoid m_rightGearShifter;
  private final DoubleSolenoid m_leftGearShifter;

  private final Compressor pcmCompressor;

  private boolean shifterInHigh;

  private final AHRS m_navX;

  // private double lEncoderCt;
  // private double rEncoderCt;

  // private double lPrevEncCt;
  // private double rPrevEncCt;

  // private double deltaLeft;
  // private double deltaRight;

  // private double lVelocity;
  // private double rVelocity;

  public Chassis() {

    // navX object instantiation
    m_navX = new AHRS(Port.kMXP);
    // m_navX.setAngleAdjustment(90);

    // Motor object instantiations, this year four Falcons were used
    m_leftMaster = new WPI_TalonFX(Constants.CANID.kLeftMaster);
    m_leftSlaveA = new WPI_TalonFX(Constants.CANID.kLeftSlaveA);
    m_rightMaster = new WPI_TalonFX(Constants.CANID.kRightMaster);
    m_rightSlaveA = new WPI_TalonFX(Constants.CANID.kRightSlaveA);

    // m_leftSlaveA.follow(m_leftMaster);
    // m_rightSlaveA.follow(m_rightMaster);
    m_left = new MotorControllerGroup(m_leftMaster, m_leftSlaveA);
    m_right = new MotorControllerGroup(m_rightMaster, m_rightSlaveA);
    m_leftMaster.setSafetyEnabled(true);
    m_leftSlaveA.setSafetyEnabled(true);
    m_rightMaster.setSafetyEnabled(true);
    m_rightSlaveA.setSafetyEnabled(true);
    // m_drive = new DifferentialDrive(m_left, m_right);
    
    resetEncoders();

    m_odometry = new DifferentialDriveOdometry(m_navX.getRotation2d());
    
    // Instantiation for Gear Shifter Double Solenoid Objects
    m_leftGearShifter = new DoubleSolenoid(Constants.CANID.kPCM, PneumaticsModuleType.REVPH,
        Constants.PCM.kLeftGearForward, Constants.PCM.kLeftGearReverse);
    m_rightGearShifter = new DoubleSolenoid(Constants.CANID.kPCM, PneumaticsModuleType.REVPH,
        Constants.PCM.kRightGearForward, Constants.PCM.kRightGearReverse);

    pcmCompressor = new Compressor(Constants.CANID.kPCM, PneumaticsModuleType.REVPH);
    pcmCompressor.enableAnalog(110, 120);
    // pcmCompressor.disable();


    m_rightMaster.setNeutralMode(NeutralMode.Brake);
    m_rightSlaveA.setNeutralMode(NeutralMode.Brake);

    m_leftMaster.setNeutralMode(NeutralMode.Brake);
    m_leftSlaveA.setNeutralMode(NeutralMode.Brake);

    m_rightMaster.setInverted(true);
    m_rightSlaveA.setInverted(true);
    m_leftMaster.setInverted(false);
    m_leftSlaveA.setInverted(false);

    

    m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    resetEncoders();
    // New drive characterization, all constants must be manually changed
    // in Constants.java class
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(NativeToMetersPerSecond(m_leftMaster.getSelectedSensorVelocity()),
        NativeToMetersPerSecond(m_rightMaster.getSelectedSensorVelocity()));
  }

  //Returns Heading From NavX, inverted and in degrees
  public double getHeading() {
    return m_navX.getRotation2d().getDegrees();

  }

  public Pose2d getPose(){
    return m_odometry.getPoseMeters();
  }

   /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    System.out.println(leftVolts + " " + rightVolts);
    m_left.setVoltage(leftVolts);
    m_right.setVoltage(rightVolts);
    
    // m_drive.feed();
  }

  public void resetOdometry(Pose2d pose) {
    System.out.println("Into Reset");
    resetEncoders();
    m_navX.setAngleAdjustment(pose.getRotation().unaryMinus().getDegrees());
    m_odometry.resetPosition(pose, m_navX.getRotation2d());
  }
  public void turnTo(double error){
      setSpeed(Input_Mode.PercentOut, 1.4 * error, -1.4 * error);
  }
  // sets speed of the chassis, takes input mode as a arugment to decrease error
  // during programming and to increase options for programming
  public void setSpeed(final Input_Mode im, double lSpeed, double rSpeed) {
    if (im == Input_Mode.Inches) {
      // m_leftMaster.set(ControlMode.Velocity, InchesToNative(lSpeed));
      // m_rightMaster.set(ControlMode.Velocity, InchesToNative(rSpeed));

    } else if (im == Input_Mode.PercentOut) {
      m_left.set(lSpeed);
      m_right.set(rSpeed);
      // m_leftMaster.set(ControlMode.PercentOutput, lSpeed);
      // m_rightMaster.set(ControlMode.PercentOutput, rSpeed);

    } else {
      // m_leftMaster.set(ControlMode.Velocity, lSpeed);
      // m_rightMaster.set(ControlMode.Velocity, rSpeed);
    }
  }

  public enum Input_Mode {
    PercentOut,
    Native,
    Inches
  }

  public void resetEncoders() {
    m_leftMaster.setSelectedSensorPosition(0);
    m_rightMaster.setSelectedSensorPosition(0);
  }

  // Shifts gear box to higher setting
  // Make sure PCMA constant and gear shifter constants are correct
  public void shiftToHigh() {
    m_leftGearShifter.set(Value.kForward);
    m_rightGearShifter.set(Value.kForward);
    shifterInHigh = true;
  }

  // Shifts gear box to lower setting
  // Make sure PCMA constant and gear shifter constants are correct
  public void shiftToLow() {
    m_leftGearShifter.set(Value.kReverse);
    m_rightGearShifter.set(Value.kReverse);
    shifterInHigh = false;
  }

 

  // Helper Method, coverts encoder count speed to inches per second
  public double NativeToMetersPerSecond(double speed) {
    return speed / Constants.DriveConstants.kEncoderCPR * Math.PI * Constants.DriveConstants.wheelDiameterMeters * 10 * (168.0/2520.0);
    // PPR is Pulses Per Rotation
    // divide by PPR to get rotations, * circ to get inches and * 10 to go 1s
  }

  public double NativeToMeters(double ticks) {
    return ticks / Constants.DriveConstants.kEncoderCPR * Math.PI * Constants.DriveConstants.wheelDiameterMeters * (168.0/2520.0);
    // PPR is Pulses Per Rotation
    // divide by PPR to get rotations, * circ to get inches
  }

  public String getGearState() {
    if (shifterInHigh)
      return "High";
    else
      return "Low";
  }

  public boolean getShifterState() {
    return shifterInHigh;
  }

  @Override

  public void periodic() {
    // This method will be called once per scheduler run

    m_odometry.update(Rotation2d.fromDegrees(getHeading()), NativeToMeters(m_leftMaster.getSelectedSensorPosition()),
    NativeToMeters(m_rightMaster.getSelectedSensorPosition()));
    // System.out.println(getWheelSpeeds());
    // Updating the current pose using navX angle and the delta left and right
    // calcuted
    // m_drive.setMaxOutput(0.3);

    // Constantly updating left and right side velocity
    // lVelocity = m_leftMaster.getSelectedSensorVelocity();
    // rVelocity = m_rightMaster.getSelectedSensorVelocity();

    SmartDashboard.putNumber("Robot Heading", getHeading());
    SmartDashboard.putString("Shifted State", getGearState());
    SmartDashboard.putNumber("Pressure", pcmCompressor.getPressure());

  }
}