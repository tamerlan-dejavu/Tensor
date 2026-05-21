package com.tensorcipher;

public enum Axis {
    X(0),
    Y(4),
    Z(8);

    public final int offset;

    Axis(int offset) {
        this.offset = offset;
    }
}
