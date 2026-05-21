

/**
 * Generates a TENSOR-MASK-XOR tensor (4×4×4) based on key schedule parameters.
 * The mask is constructed by filling layers with key bytes and applying multipliers.
 */
public class TensorMask {
    private final KeySchedule keySchedule;
    private Tensor3D mask;

    /**
     * Creates a TensorMask from a KeySchedule and generates the mask tensor.
     *
     * @param keySchedule the key schedule
     */
    public TensorMask(KeySchedule keySchedule) {
        this.keySchedule = keySchedule;
        generateMask();
    }

    /**
     * Gets the mask tensor.
     *
     * @return the generated mask tensor
     */
    public Tensor3D getMask() {
        return mask.clone();
    }

    private void generateMask() {
        mask = new Tensor3D();
        int[] fillBytes = keySchedule.getMaskFillBytes();
        int[] multipliers = keySchedule.getMaskMultipliers();

        // Each of the 4 X-layers is filled identically with the same 16 bytes (y→z order)
        for (int x = 0; x < 4; x++) {
            int byteIndex = 0;
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    mask.set(x, y, z, fillBytes[byteIndex++] & 0xFF);
                }
            }
        }

        for (int x = 0; x < 4; x++) {
            int multiplier = multipliers[x];
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    int value = mask.get(x, y, z);
                    mask.set(x, y, z, (value * multiplier) % 256);
                }
            }
        }
    }
}
