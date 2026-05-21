public class TensorMask {
    private final KeySchedule keySchedule;
    private Tensor3D mask;

    public TensorMask(KeySchedule keySchedule) {
        this.keySchedule = keySchedule;
        generateMask();
    }

    public Tensor3D getMask() {
        return mask.clone();
    }

    private void generateMask() {
        mask = new Tensor3D();
        int[] fillBytes = keySchedule.getMaskFillBytes();
        int[] multipliers = keySchedule.getMaskMultipliers();

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
