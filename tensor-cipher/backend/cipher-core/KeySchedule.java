

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Key schedule generator that computes SHA-256 hash and derives shift and mask parameters.
 */
public class KeySchedule {
    private final int[] hashBytes;

    /**
     * Creates a KeySchedule from a string key.
     * Computes SHA-256 hash and stores bytes as unsigned integers.
     *
     * @param key the key string
     */
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

    /**
     * Gets shift values for a specific round and axis.
     * Logic varies by round:
     * - Round 1: uses bytes at offset, offset+1, offset+2, offset+3
     * - Round 2: uses specific pairs of indices per axis
     * - Round 3: uses specific indices distributed across the first 12 bytes
     *
     * @param round the round number (1-3)
     * @param axis  the axis (X, Y, or Z)
     * @return array of 4 shift values (one per layer)
     */
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

    /**
     * Gets the 16 fill bytes for mask creation (bytes 12-27 of SHA-256 hash).
     *
     * @return array of 16 bytes
     */
    public int[] getMaskFillBytes() {
        int[] result = new int[16];
        for (int i = 0; i < 16; i++) {
            result[i] = hashBytes[12 + i];
        }
        return result;
    }

    /**
     * Gets the 4 multiplier values for mask (bytes 28-31 of SHA-256 hash).
     *
     * @return array of 4 multiplier values
     */
    public int[] getMaskMultipliers() {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = hashBytes[28 + i];
        }
        return result;
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
}
