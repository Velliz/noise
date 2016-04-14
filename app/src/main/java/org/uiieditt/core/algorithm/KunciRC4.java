package org.uiieditt.core.algorithm;

public class KunciRC4 {

    private short[] S;
    private short[] U;

    private short[] keystream;

    private int length;

    private int LPT;

    public KunciRC4() {
        length = 256;
        S = new short[length];
    }

    /**
     * Algoritma RC4 Key adalah kunci yang digunakan untuk menyandi dan LPT
     * diperoleh dari perkalian panjang dan lebar Gambar
     */
    public short[] generateKeyStream(short[] key, int LPT) {
        this.U = new short[length];
        this.U = key;
        this.LPT = LPT;
        if (LPT <= Integer.MAX_VALUE - 5) {
            try {
                keystream = new short[LPT];
            } catch (Error e) {
                //JOptionPane.showMessageDialog(null, "Anda Memasukan Kunci Seed yang Salah. Silahkan Coba Lagi", "Kesalahan Kunci", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            //JOptionPane.showMessageDialog(null, "Gambar terlalu Besar. Maksimal besar Pixel 2^31 - 1", "Kesalahan Gambar", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        KeySchedulingAlgorithm();
        PseudoRandomGeneratedAlgorithm();
        return keystream;
    }

    private void KeySchedulingAlgorithm() {
        for (short i = 0; i < length - 1; i++) {
            S[i] = i;
        }
        short j = 0;
        for (short i = 0; i < length - 1; i++) {
            j = (short) ((j + S[i] + U[i]) % length);
            swap(S, i, j);
        }
    }

    private void PseudoRandomGeneratedAlgorithm() {
        short i = 0;
        short j = 0;
        for (int a = 0; a < LPT; a++) {
            i = (short) ((i + 1) % length);
            j = (short) ((j + S[i]) % length);
            swap(S, i, j);
            keystream[a] = S[(S[i] + S[j]) % length];
        }
    }

    private void swap(short[] S, short a, short b) {
        short temp;
        temp = S[a];
        S[a] = S[b];
        S[b] = temp;
    }
}
