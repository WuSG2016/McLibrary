package com.mdeveloper.serialtool;



import java.util.Arrays;

public class serial {
    static {
        System.loadLibrary("uart");
    }

    public native int Close();

    public native int Open(final int p0, final int p1, final int p2, final int p3, final int p4);

    public int OpenPort(final String[] array) {

        int n = 1;
        final int n2 = 0;
        int j = n2;
        return this.Open(n, Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]), j);
    }

    public native byte[] Read();

    public native int Write(final byte[] p0, final int p1);
}