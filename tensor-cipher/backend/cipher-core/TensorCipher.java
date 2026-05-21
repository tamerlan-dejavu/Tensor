import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TensorCipher {
    private static final int BLOCK_SIZE = 64;
    private static final char PADDING_CHAR = 'X';

    public String encrypt(String plaintext, String key) {
        String upperText = plaintext.toUpperCase();
        int[] asciiCodes = stringToAscii(upperText);
        asciiCodes = padToBlockSize(asciiCodes);

        KeySchedule keySchedule = new KeySchedule(key);
        TensorMask tensorMask = new TensorMask(keySchedule);

        List<Integer> encryptedData = new ArrayList<>();

        for (int blockStart = 0; blockStart < asciiCodes.length; blockStart += BLOCK_SIZE) {
            int[] block = new int[BLOCK_SIZE];
            System.arraycopy(asciiCodes, blockStart, block, 0, BLOCK_SIZE);

            Tensor3D tensor = new Tensor3D();
            tensor.fromFlatArray(block);

            for (int round = 1; round <= 3; round++) {
                applyRoundShifts(tensor, keySchedule, round);
            }

            tensor.xorWith(tensorMask.getMask());

            int[] flatArray = tensor.toFlatArray();
            for (int value : flatArray) {
                encryptedData.add(value & 0xFF);
            }
        }

        byte[] encryptedBytes = new byte[encryptedData.size()];
        for (int i = 0; i < encryptedData.size(); i++) {
            encryptedBytes[i] = (byte) (encryptedData.get(i) & 0xFF);
        }

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String ciphertext, String key) {
        byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
        int[] encryptedData = new int[encryptedBytes.length];
        for (int i = 0; i < encryptedBytes.length; i++) {
            encryptedData[i] = encryptedBytes[i] & 0xFF;
        }

        KeySchedule keySchedule = new KeySchedule(key);
        TensorMask tensorMask = new TensorMask(keySchedule);

        List<Integer> decryptedData = new ArrayList<>();

        for (int blockStart = 0; blockStart < encryptedData.length; blockStart += BLOCK_SIZE) {
            int[] block = new int[BLOCK_SIZE];
            System.arraycopy(encryptedData, blockStart, block, 0, BLOCK_SIZE);

            Tensor3D tensor = new Tensor3D();
            tensor.fromFlatArray(block);

            tensor.xorWith(tensorMask.getMask());

            for (int round = 3; round >= 1; round--) {
                applyReverseRoundShifts(tensor, keySchedule, round);
            }

            int[] flatArray = tensor.toFlatArray();
            for (int value : flatArray) {
                decryptedData.add(value & 0xFF);
            }
        }

        int[] asciiCodes = new int[decryptedData.size()];
        for (int i = 0; i < decryptedData.size(); i++) {
            asciiCodes[i] = decryptedData.get(i);
        }

        asciiCodes = removePadding(asciiCodes);
        return asciiToString(asciiCodes);
    }

    private void applyRoundShifts(Tensor3D tensor, KeySchedule keySchedule, int round) {
        applyAxisShifts(tensor, keySchedule, round, Axis.X);
        applyAxisShifts(tensor, keySchedule, round, Axis.Y);
        applyAxisShifts(tensor, keySchedule, round, Axis.Z);
    }

    private void applyReverseRoundShifts(Tensor3D tensor, KeySchedule keySchedule, int round) {
        applyAxisShiftsReverse(tensor, keySchedule, round, Axis.Z);
        applyAxisShiftsReverse(tensor, keySchedule, round, Axis.Y);
        applyAxisShiftsReverse(tensor, keySchedule, round, Axis.X);
    }

    private void applyAxisShifts(Tensor3D tensor, KeySchedule keySchedule, int round, Axis axis) {
        int[] shifts = keySchedule.getAxisShifts(round, axis);
        for (int layer = 0; layer < 4; layer++) {
            switch (axis) {
                case X -> tensor.shiftLayerX(layer, shifts[layer]);
                case Y -> tensor.shiftLayerY(layer, shifts[layer]);
                case Z -> tensor.shiftLayerZ(layer, shifts[layer]);
            }
        }
    }

    private void applyAxisShiftsReverse(Tensor3D tensor, KeySchedule keySchedule, int round, Axis axis) {
        int[] shifts = keySchedule.getAxisShifts(round, axis);
        for (int layer = 0; layer < 4; layer++) {
            int reverseShifts = (4 - (shifts[layer] % 4)) % 4;
            switch (axis) {
                case X -> tensor.shiftLayerX(layer, reverseShifts);
                case Y -> tensor.shiftLayerY(layer, reverseShifts);
                case Z -> tensor.shiftLayerZ(layer, reverseShifts);
            }
        }
    }

    private int[] stringToAscii(String str) {
        int[] result = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            result[i] = str.charAt(i);
        }
        return result;
    }

    private String asciiToString(int[] codes) {
        StringBuilder sb = new StringBuilder();
        for (int code : codes) {
            sb.append((char) code);
        }
        return sb.toString();
    }

    private int[] padToBlockSize(int[] data) {
        int paddingNeeded = (BLOCK_SIZE - (data.length % BLOCK_SIZE)) % BLOCK_SIZE;
        int[] padded = new int[data.length + paddingNeeded];
        System.arraycopy(data, 0, padded, 0, data.length);
        for (int i = data.length; i < padded.length; i++) {
            padded[i] = PADDING_CHAR;
        }
        return padded;
    }

    private int[] removePadding(int[] data) {
        int endIndex = data.length;
        while (endIndex > 0 && data[endIndex - 1] == PADDING_CHAR) {
            endIndex--;
        }
        int[] result = new int[endIndex];
        System.arraycopy(data, 0, result, 0, endIndex);
        return result;
    }
}
