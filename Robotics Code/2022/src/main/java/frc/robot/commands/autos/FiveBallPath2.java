// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.commands.CenterTurret;
import frc.robot.commands.IdleTower;
import frc.robot.commands.IntakeDown;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FiveBallPath2 extends ParallelRaceGroup {
  /** Creates a new RamsetePath2. */
  public FiveBallPath2(Chassis chassis, Intake intake, Tower tower, Shooter Shooter) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    RamseteController disabledRamsete = new RamseteController();
    disabledRamsete.setEnabled(false);

    RamseteCommand pickUpBall2From1 = new RamseteCommand(
        // exampleTrajectory,
        Constants.AutoConstants.pickUpBall2From1,
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

    addCommands(pickUpBall2From1.andThen(() -> chassis.tankDriveVolts(0, 0)), 
        new IntakeDown(intake),
        new IdleTower(tower)
        // new CenterTurret(Shooter)
        );
  }
}
