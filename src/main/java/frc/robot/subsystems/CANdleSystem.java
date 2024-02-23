package frc.robot.subsystems;

import edu.wpi.first.hal.CANData;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;

import frc.robot.subsystems.IntakeSubsystem;

public class CANdleSystem extends SubsystemBase {
    
    private final int CANDLE_ID = 6; 
    private final int NUM_LEDS = 68;
    private final String CAN_BUS = "rio";
    private final CANdle m_candle_1 = new CANdle(CANDLE_ID, CAN_BUS);

    private IntakeSubsystem intakeRef;

    public CANdleSystem(IntakeSubsystem intake)
    {
        intakeRef = intake;

        // Do config
        CANdleConfiguration config = new CANdleConfiguration();
        config.stripType = LEDStripType.RGB;
        config.statusLedOffWhenActive = true;
        config.brightnessScalar = 1;
        config.vBatOutputMode = VBatOutputMode.On;
        config.disableWhenLOS = true;
        m_candle_1.configAllSettings(config, 100);

    }

    public void SetColor(int r, int g, int b)
    {
        m_candle_1.setLEDs(r, g, b, 0, 0, NUM_LEDS);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        // Check for beam broken
        if (intakeRef.getBeam())
        {
            SetColor(255, 0, 0);
        }
        else
        {
            SetColor(0, 255, 0);
        }
    }

}
