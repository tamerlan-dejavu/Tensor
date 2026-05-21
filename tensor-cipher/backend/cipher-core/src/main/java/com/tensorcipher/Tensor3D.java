package com.tensorcipher;

public class Tensor3D {
    private static final int SIZE = 4;
    private final int[][][] data;

    public Tensor3D() {
        this.data = new int[SIZE][SIZE][SIZE];
    }

    public int get(int x, int y, int z) {
        return data[x][y][z];
    }

    public void set(int x, int y, int z, int value) {
        data[x][y][z] = value;
    }

    public void shiftLayerX(int layer, int shifts) {
        shifts = shifts % SIZE;
        if (shifts == 0) return;

        int[] temp = new int[SIZE];
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                temp[x] = data[x][layer][z];
            }
            for (int x = 0; x < SIZE; x++) {
                data[x][layer][z] = temp[(x + shifts) % SIZE];
            }
        }
    }

    public void shiftLayerY(int layer, int shifts) {
        shifts = shifts % SIZE;
        if (shifts == 0) return;

        int[] temp = new int[SIZE];
        for (int z = 0; z < SIZE; z++) {
            for (int y = 0; y < SIZE; y++) {
                temp[y] = data[layer][y][z];
            }
            for (int y = 0; y < SIZE; y++) {
                data[layer][y][z] = temp[(y + shifts) % SIZE];
            }
        }
    }

    public void shiftLayerZ(int layer, int shifts) {
        shifts = shifts % SIZE;
        if (shifts == 0) return;

        int[] temp = new int[SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                temp[z] = data[x][layer][z];
            }
            for (int z = 0; z < SIZE; z++) {
                data[x][layer][z] = temp[(z + shifts) % SIZE];
            }
        }
    }

    public void xorWith(Tensor3D mask) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    data[x][y][z] ^= mask.get(x, y, z);
                }
            }
        }
    }

    public int[] toFlatArray() {
        int[] result = new int[SIZE * SIZE * SIZE];
        int index = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    result[index++] = data[x][y][z];
                }
            }
        }
        return result;
    }

    public void fromFlatArray(int[] flatArray) {
        if (flatArray.length != SIZE * SIZE * SIZE) {
            throw new IllegalArgumentException("Array must have 64 elements");
        }
        int index = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    data[x][y][z] = flatArray[index++];
                }
            }
        }
    }
}
