import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Tensor3DTest {

    @Test
    void testShiftLayerX() {
        Tensor3D tensor = new Tensor3D();
        // Fill layer 0 of X axis with known values
        for (int y = 0; y < 4; y++) {
            for (int z = 0; z < 4; z++) {
                tensor.set(y, 0, z, y * 4 + z);
            }
        }
        int before10 = tensor.get(1, 0, 0);
        int before20 = tensor.get(2, 0, 0);

        tensor.shiftLayerX(0, 1);

        // After left shift by 1: position x gets what was at position x+1
        assertEquals(before10, tensor.get(0, 0, 0));
        assertEquals(before20, tensor.get(1, 0, 0));
    }

    @Test
    void testXorWith() {
        Tensor3D a = new Tensor3D();
        Tensor3D b = new Tensor3D();
        a.set(0, 0, 0, 0b10101010);
        b.set(0, 0, 0, 0b11001100);
        a.xorWith(b);
        assertEquals(0b10101010 ^ 0b11001100, a.get(0, 0, 0));
        // All other cells should still be 0 XOR 0 = 0
        assertEquals(0, a.get(1, 1, 1));
    }

    @Test
    void testFlatArrayRoundTrip() {
        Tensor3D original = new Tensor3D();
        int[] flat = new int[64];
        for (int i = 0; i < 64; i++) {
            flat[i] = i * 3 + 7;
        }
        original.fromFlatArray(flat);
        int[] result = original.toFlatArray();
        assertArrayEquals(flat, result);
    }
}
