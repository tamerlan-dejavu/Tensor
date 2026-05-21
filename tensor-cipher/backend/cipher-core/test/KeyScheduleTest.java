import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KeyScheduleTest {

    @Test
    void testHashLength() {
        KeySchedule ks = new KeySchedule("anykey");
        // getMaskFillBytes (16) + getMaskMultipliers (4) + 12 shift bytes = 32 total
        assertEquals(16, ks.getMaskFillBytes().length);
        assertEquals(4, ks.getMaskMultipliers().length);
    }

    @Test
    void testGetAxisShiftsRound1() {
        KeySchedule ks = new KeySchedule("SECRET");
        int[] shiftsX = ks.getAxisShifts(1, Axis.X);
        int[] shiftsY = ks.getAxisShifts(1, Axis.Y);
        int[] shiftsZ = ks.getAxisShifts(1, Axis.Z);
        assertEquals(4, shiftsX.length);
        assertEquals(4, shiftsY.length);
        assertEquals(4, shiftsZ.length);
    }

    @Test
    void testMaskMultipliersLength() {
        KeySchedule ks = new KeySchedule("TESTKEY");
        int[] multipliers = ks.getMaskMultipliers();
        assertEquals(4, multipliers.length);
        for (int m : multipliers) {
            assertTrue(m >= 0 && m <= 255, "Multiplier must be unsigned byte: " + m);
        }
    }
}
