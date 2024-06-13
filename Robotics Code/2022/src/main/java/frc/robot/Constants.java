// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class Limelight {
        public static final double kTargetHeight = 105;
        public static final double kLimeLightHeight = 28.5;
        public static final double kLimelightAngle = 27;//degrees
    }

    public static final class DriveConstants {
        public static final double kEncoderCPR = 2048;
        public static final double kWheelDiameterMeters =  0.152;
        public static final double kEncoderDistancePerPulse =
            // Assumes the encoders are directly mounted on the wheel shafts
            (kWheelDiameterMeters * Math.PI) / (double) kEncoderCPR;

        public static final double wheelDiameterMeters = 0.152;
        public static final double wheelDiameterInches = 6;

        public static final double ksVolts = 0.61035;
        public static final double kvVoltSecondsPerMeter = 3.3901;
        public static final double kaVoltSecondsSquaredPerMeter = 0.31232;
    
        public static final double kPDriveVel = 4;

        public static final double kTrackWidth = 0.74346; 
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidth);
    }

    public static final class Joystick { 
        public static final int leftStick = 0;
        public static final int rightStick = 1;
        public static final int xboxController = 2;

        public static final int gearShiftButton = 7;
        public static final int toggleServo = 9; 

        public static final int turnButton = 8; // temp for testing turn 180
        public static final int intakeOutButton = 10;

        public static final int upShooterConstant = 7;
        public static final int downShooterConstant = 8; 
    }

    public static final class IntakeC {

    }


    public static final class ServoC {
        public static final double lowAngle = 55; //need to change these
        public static final double mediumAngle = 90;
        public static final double highAngle = 180;
    }

    public static final class ShooterConstants {
        public static final int wheelRadius = 2;
        public static final double PPR = 2048; 
        public static final double kF = 0.0682;
        public static final double kP = 0.00125;
        public static final double stallTime = 3;
        public static final double HorAnglerkP = 0.12;
        public static final double HorAnglerkD = 0.1;
        public static boolean isReady = false;
        public static double horizontalAnglerCPR = 4096;
        public static double horizontalGearRatio = 14; //find this out. 
        public static double currentShooterSpeed = 0;
        public static double prevHorizontalAdjustment = 0;
        public static double shooterConstant = 1.15; 
    }

    public static final class HorizontalAngler {
        public static final double PPR = 2048;
        public static final double kF = 0.0682;
        public static final double kP = 0.00125;
        public static final double radius = 10;
        public static final double kRightOffset = 5;
        public static final double kLeftOffset = 5;
    }

    public static final class VerticalAngler {
        public static final double PPR = 4096;
        public static final double anglerPIDkP = 0.01;
        public static final double radius = 1;
    }

    public static final class DIO {
        public static final int kShooterLeftLimit = 2;
        public static final int kShooterRightLimit = 3;

        public static final int kVerticalLimit = 1; 

        public static final int kTowerIn = 4; 
        public static final int kTowerOut = 0;

        public static final int kLeftMin = 6; //Proxes for climber
        public static final int kLeftMax = 0;
        public static final int kRightMin = 5;
        public static final int kRightMax = 0;
    }

    public static final class CANID {
        public static final int kPCM = 14;

        public static final int kLeftMaster = 2; //Left Drive Train Falcons
        public static final int kLeftSlaveA = 3;

        public static final int kRightMaster = 5; //Right Drive Train Falcons
        public static final int kRightSlaveA = 6;

        public static final int kIntake = 7; //Intake 775Pro

        public static final int kTower = 8; //Tower 775Pro

        public static final int kHorizontalAngler = 10;  //Turret Horizontal 775Pro
        public static final int kVerticalAngler = 11; //Turret Vertical 775Pro

        public static final int kShooter1 = 12; //Shooter Falcons
        public static final int kShooter2 = 13;

        public static final int kHorizontalCANCoder = 17; //CANCoder For ID 10
        // public static final int kVerticalCANCoder = 17; //CANCoder for ID 11
       
        public static final int kClimberExtenderM = 18; //Climber Master Falcon
        public static final int kClimberExtenderS = 19; //Climber Slave Falcon 
    }

    public static final class PCM {
        public static final int kLeftGearForward = 1;
        public static final int kLeftGearReverse = 0;

        public static final int kRightGearForward = 3;
        public static final int kRightGearReverse = 2;

        public static final int kRightIntakeLifterForward = 5;
        public static final int kRightIntakeLifterReverse = 4;

        public static final int kLeftIntakeLifterForward = 7;
        public static final int kLeftIntakeLifterReverse = 6;

        public static final int kLeftClimberForward = 10; //make these real numbers when we have a robot
        public static final int kRightClimberForward = 8;

        public static final int kLeftClimberReverse = 11;
        public static final int kRightClimberReverse = 9;
    }

    public static final class ClimberConstants {
        // public static final double kF = 0.00976;
        public static final double kP = 0.0001;
        public static final double kD = 0.01;
        public static final double lowerLimit = 10;
        public static final double upperLimit = 50000;
        
        public static final double extenderLowerLimit = 0;
        public static final double extenderUpperLimit = 0;
    }

    public static final class AutoConstants {
        public static final double kMaxSpeedMetersPerSecond = 3; 
        public static final double kMaxAccelerationMetersPerSecondSquared = 1.5; 

        public static final double kRamseteB = 2; 
        public static final double kRamseteZeta = 0.7; 

        public static Trajectory pickUpBall1;
        public static Trajectory pickUpBall2From1;
        public static Trajectory pickUpBall3From2;
        public static Trajectory travelToShootPoint;

        public static Trajectory twoBallPath1;
    }
}