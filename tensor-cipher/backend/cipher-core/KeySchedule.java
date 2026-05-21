import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeySchedule {
    private final int[] hashBytes;

    public KeySchedule(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes());
            this.hashBytes = new int[hash.length];
            for (int i = 0; i < hash.length; i++) {
                this.hashBytes[i] = hash[i] & 0xFF;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public int[] getAxisShifts(int round, Axis axis) {
        int[] shifts = new int[4];
        int offset = axis.offset;

        if (round == 1) {
            for (int i = 0; i < 4; i++) {
                shifts[i] = hashBytes[offset + i];
            }
        } else if (round == 2) {
            int[] indices = getIndicesForRound2(axis);
            for (int i = 0; i < 4; i++) {
                shifts[i] = hashBytes[indices[i]];
            }
        } else if (round == 3) {
            int[] indices = getIndicesForRound3(axis);
            for (int i = 0; i < 4; i++) {
                shifts[i] = hashBytes[indices[i]];
            }
        }
        return shifts;
    }

    private int[] getIndicesForRound2(Axis axis) {
        return switch (axis) {
            case X -> new int[]{0, 1, 6, 7};
            case Y -> new int[]{2, 3, 8, 9};
            case Z -> new int[]{4, 5, 10, 11};
        };
    }

    private int[] getIndicesForRound3(Axis axis) {
        return switch (axis) {
            case X -> new int[]{0, 3, 6, 9};
            case Y -> new int[]{1, 4, 7, 10};
            case Z -> new int[]{2, 5, 8, 11};
        };
    }

    public int[] getMaskFillBytes() {
        int[] result = new int[16];
        for (int i = 0; i < 16; i++) {
            result[i] = hashBytes[12 + i];
        }
        return result;
    }

    public int[] getMaskMultipliers() {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = hashBytes[28 + i];
        }
        return result;
    }
}
