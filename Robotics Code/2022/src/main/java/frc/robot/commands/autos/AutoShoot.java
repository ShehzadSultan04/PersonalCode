// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.commands.AutoAim;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.TowerUpAuto;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tower;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoShoot extends ParallelRaceGroup {
  /** Creates a new AutoShoot. */
  public AutoShoot(Tower tower, Shooter shooter, Limelight limelight, Intake intake) {
    super(
      (new AutoAim(shooter, limelight)),
      (new TowerUpAuto(tower)), 
      (new IntakeDown(intake))
    );
     // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands();
  }
}
