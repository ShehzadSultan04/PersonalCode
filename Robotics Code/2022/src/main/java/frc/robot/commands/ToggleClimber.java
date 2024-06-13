package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ToggleClimber extends CommandBase {
  /** Creates a new ToggleClimber. */
  private final Climber m_Climber;
  public ToggleClimber(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
   m_Climber = climber;
  addRequirements(m_Climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_Climber.isExtended())
    {
      m_Climber.bringIn();
    }
    else{
      m_Climber.extend();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true; 
  }
}