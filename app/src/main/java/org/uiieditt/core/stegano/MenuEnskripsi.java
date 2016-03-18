package org.uiieditt.core.stegano;

import java.text.DecimalFormat;

public class MenuEnskripsi {

    private byte r;
    private byte size;
    private short[] randomKey;

    public MenuEnskripsi() {
        this.r = 4;
        this.size = 3;
    }

    public short[] generateRandomKey(double x0, int jumlah) {
        double y;
        randomKey = new short[jumlah];
        y = r * x0 * (1 - x0);
        randomKey[0] = potong(y);
        for (int i = 1; i < jumlah; i++) {
            y = r * y * (1 - y);
            randomKey[i] = potong(y);
        }
        return randomKey;
    }

    private short potong(double y) {
        short result = 0;
        double n;
        int nilai = 1;
        String format = "#.#";
        for (int i = 1; i < size; i++) {
            nilai = nilai * 10;
            format = format.concat("#");
        }
        DecimalFormat df = new DecimalFormat(format);

        n = Double.valueOf(df.format(y).replace(',','.'));
        if (n != 0) {
            while (n <= nilai) {
                n = n * 10;
            }
        }
        result = (short) n;

        return (short) (result % 256);
    }
}
