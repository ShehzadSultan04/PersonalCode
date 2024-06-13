// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Limelight;
import frc.robot.Constants;
import frc.robot.commands.ShiftToLow;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FiveBallAuto extends SequentialCommandGroup {
  /** Creates a new StartToPickupFirstBall. */
  public FiveBallAuto(Intake intake, Chassis chassis, Tower tower, Shooter shooter, Limelight limelight) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new ResetPose(chassis, Constants.AutoConstants.pickUpBall1.getInitialPose()),
        new ShiftToLow(chassis),
        new FiveBallPath1(chassis, intake, tower),
        new AutoShoot(tower, shooter, limelight, intake),
        new FiveBallPath2(chassis, intake, tower, shooter),
        new AutoShoot(tower, shooter, limelight, intake)
        // new FiveBallPath3(chassis, intake, tower),// Could combine Paths 3 and 4, no stop in middle
        // new FiveBallPath4(chassis, intake, tower),
        // new AutoShoot(tower, shooter, limelight)
    );
  }
}
