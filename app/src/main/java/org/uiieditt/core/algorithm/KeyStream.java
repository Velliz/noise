package org.uiieditt.core.algorithm;

import org.uiieditt.core.stegano.MenuEnskripsi;

public class KeyStream {

    private KunciRC4 kunciRC4;
    private MenuEnskripsi chaos;
    private short[] keystream;
    private int lengthRandomKey = 256;
    private boolean flip = false;

    public KeyStream() {
        kunciRC4 = new KunciRC4();
        chaos = new MenuEnskripsi();
    }

    public short[] generateKeyStream(double key, int length) {
        short[] randomKey;
        randomKey = chaos.generateRandomKey(key, lengthRandomKey);
        keystream = kunciRC4.generateKeyStream(randomKey, length);
        return keystream;
    }

    public short[] generateKeyStreamWithoutChaos(long key, int length) {
        short[] randomKey = paddingKey(key);
        keystream = kunciRC4.generateKeyStream(randomKey, length);
        return keystream;
    }

    public short[] generateKeyStreamWithoutChaos(short[] randomKey, int length) {
        return kunciRC4.generateKeyStream(randomKey, length);
    }

    public short[] paddingKey(long key) {
        short[] randomKey = new short[256];
        if (flip) {
            key = flipping(key);
        }
        String bin = Long.toBinaryString(key);
        while (bin.length() < 8) {
            bin = "0".concat(bin);
        }

        String binary = "";
        int j = 0;
        int x = 0;
        for (int i = 0; i < 2048; i++) {
            if (j >= bin.length()) {
                j = 0;
            }
            binary = binary.concat(bin.substring(j, j + 1));
            j++;
            if (i % 8 == 7) {
                randomKey[x] = Short.parseShort(binary, 2);
                binary = "";
                x++;
            }
        }
        return randomKey;
    }

    private long flipping(long l) {
        String s = Long.toBinaryString(l);
        String s2 = s.substring(0, s.length() - 1);
        String ch = s.substring(s.length() - 1, s.length());
        if (ch.equals("1")) {
            ch = "0";
        } else {
            ch = "1";
        }
        s2 = s2.concat(ch);
        Long lo = Long.parseLong(s2, 2);

        return lo;
    }
}
