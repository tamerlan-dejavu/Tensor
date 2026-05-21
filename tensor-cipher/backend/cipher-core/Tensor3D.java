

/**
 * A 3D tensor wrapper for a 4×4×4 integer array.
 * Provides basic operations for tensor manipulation including shifts and XOR operations.
 */
public class Tensor3D {
    private static final int SIZE = 4;
    private final int[][][] data;

    /**
     * Creates a new Tensor3D initialized with zeros.
     */
    public Tensor3D() {
        this.data = new int[SIZE][SIZE][SIZE];
    }

    /**
     * Gets the value at position (x, y, z).
     *
     * @param x x-coordinate (0-3)
     * @param y y-coordinate (0-3)
     * @param z z-coordinate (0-3)
     * @return the value at the specified position
     */
    public int get(int x, int y, int z) {
        return data[x][y][z];
    }

    /**
     * Sets the value at position (x, y, z).
     *
     * @param x     x-coordinate (0-3)
     * @param y     y-coordinate (0-3)
     * @param z     z-coordinate (0-3)
     * @param value the value to set
     */
    public void set(int x, int y, int z, int value) {
        data[x][y][z] = value;
    }

    /**
     * Performs a cyclic left shift of elements in a layer along the X axis.
     * The layer is indexed by the Y-Z plane.
     *
     * @param layer  the layer index (0-3)
     * @param shifts number of positions to shift left
     */
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

    /**
     * Performs a cyclic left shift of elements in a layer along the Y axis.
     * The layer is indexed by the X-Z plane.
     *
     * @param layer  the layer index (0-3)
     * @param shifts number of positions to shift left
     */
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

    /**
     * Performs a cyclic left shift of elements in a layer along the Z axis.
     * The layer is indexed by the X-Y plane.
     *
     * @param layer  the layer index (0-3)
     * @param shifts number of positions to shift left
     */
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

    /**
     * Performs element-wise XOR operation with another tensor.
     *
     * @param mask the tensor to XOR with
     */
    public void xorWith(Tensor3D mask) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    data[x][y][z] ^= mask.get(x, y, z);
                }
            }
        }
    }

    /**
     * Creates a deep copy of this tensor.
     *
     * @return a new Tensor3D with the same values
     */
    public Tensor3D clone() {
        Tensor3D result = new Tensor3D();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    result.set(x, y, z, this.get(x, y, z));
                }
            }
        }
        return result;
    }

    /**
     * Converts the tensor to a flat array in x→y→z order.
     *
     * @return an array of 64 integers
     */
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

    /**
     * Fills the tensor from a flat array in x→y→z order.
     *
     * @param flatArray an array of 64 integers
     */
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
