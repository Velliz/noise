package org.uiieditt.core.stegano;

import android.graphics.Bitmap;

public class Steganografi {

	private long feed;
	private int totalPixel;
	private Bitmap img;
	private int counter = 62;
	private int total = 4 * 16;
	private int[] TabPos = new int[counter + total];
	private int[] newTabPos = new int[counter + total];

	public Steganografi(long f, Bitmap b) {
		this.feed = f;
		this.totalPixel = b.getHeight() * b.getWidth();
		this.img = b;

	}

	public Bitmap getImg() {
		return img;
	}

	public void sisipKey(double key) {
		int pos, i, j;
		int rgb, rgb2;
		String bin, val;
		String binaryKey = Long.toBinaryString(Double.doubleToLongBits(key));

		for (int n = 1; n <= counter; n++) {
			pos = (int) (Math.pow(feed, n) % totalPixel);
			while (this.isExist(TabPos, pos)) {
				pos = pos + 1;
			}
			TabPos[n - 1] = pos;

			i = pos % img.getWidth();
			j = pos / img.getWidth();

			rgb = img.getPixel(i, j);
			bin = Integer.toBinaryString(rgb);

			// Penyisipan binaryKey pada LSB
			bin = bin.substring(0, bin.length() - 1);
			bin = bin.concat(binaryKey.substring(n - 1, n));

			long avoidOverflows = Long.parseLong(bin, 2);
			rgb = (int) avoidOverflows;

			img.setPixel(i, j, rgb);
		}
	}

	public double ambilKey() {
		int pos, i, j;
		int rgb;
		String binaryKey = "";
		String bin, digitAkhir;
		for (int n = 1; n <= counter; n++) {
			pos = (int) (Math.pow(feed, n) % totalPixel);

			while (this.isExist(newTabPos, pos)) {
				pos = pos + 1;
			}
			newTabPos[n - 1] = pos;
			i = pos % img.getWidth();
			j = pos / img.getWidth();

			rgb = img.getPixel(i, j);
			bin = Integer.toBinaryString(rgb);

			digitAkhir = bin.substring(bin.length() - 1, bin.length());
			binaryKey = binaryKey.concat(digitAkhir);
		}

		Long l = Long.parseLong(binaryKey, 2);
		Double key = Double.longBitsToDouble(l);

//		String banding = String.valueOf(key);
//		banding.replaceAll("0.", "");
//		if (!banding.equals(inputan)) {
//			
//		}
		
		return key;
	}

	public void saveCoordinat(int x, int y, int w, int h) {
		int pos, i, j, b;
		int rgb, awal, akhir;
		String bin;
		int[] temp = new int[4];
		int bit = 16;

		temp[0] = x;
		temp[1] = y;
		temp[2] = w;
		temp[3] = h;

		for (int a = 0; a < 4; a++) {
			String binaryKey = Integer.toBinaryString(0x10000 | temp[a])
					.substring(1);
			b = 1;
			awal = counter + 1 + a * bit;
			akhir = counter + (a + 1) * bit;

			for (int n = awal; n <= akhir; n++) {
				pos = (int) (Math.pow(feed, n) % totalPixel);
				while (this.isExist(TabPos, pos)) {
					pos = pos + 1;
				}
				TabPos[n - 1] = pos;

				i = pos % img.getWidth();
				j = pos / img.getWidth();

				rgb = img.getPixel(i, j);
				bin = Integer.toBinaryString(rgb);

				// Penyisipan binaryKey pada LSB
				bin = bin.substring(0, bin.length() - 1);
				bin = bin.concat(binaryKey.substring(b - 1, b));

				long avoidOverflows = Long.parseLong(bin, 2);
				rgb = (int) avoidOverflows;

				img.setPixel(i, j, rgb);
				b++;

			}
		}

	}

	public int[] loadCoordinat() {
		int[] temp = new int[4];
		int pos, i, j;
		int rgb, awal, akhir;
		String binaryKey;
		String bin, digitAkhir;
		int bit = 16;

		for (int a = 0; a < 4; a++) {
			awal = counter + 1 + a * bit;
			akhir = counter + (a + 1) * bit;
			binaryKey = "";

			for (int n = awal; n <= akhir; n++) {
				pos = (int) (Math.pow(feed, n) % totalPixel);

				while (this.isExist(newTabPos, pos)) {
					pos = pos + 1;
				}
				newTabPos[n - 1] = pos;
				i = pos % img.getWidth();
				j = pos / img.getWidth();

				rgb = img.getPixel(i, j);
				bin = Integer.toBinaryString(rgb);

				digitAkhir = bin.substring(bin.length() - 1, bin.length());
				binaryKey = binaryKey.concat(digitAkhir);
			}
			temp[a] = Integer.parseInt(binaryKey, 2);
		}

		return temp;
	}

	public boolean isExist(int[] posisi, int cek) {
		for (int i = 0; i < posisi.length; i++) {
			if (posisi[i] == cek) {
				return true;
			}
		}
		return false;
	}

	public void sisipKeyRC4(long key) {
		int pos, i, j;
		int rgb;
		String bin;

		String binaryKey = Long.toBinaryString(Double.doubleToLongBits(key));

		for (int n = 1; n <= counter; n++) {

			pos = (int) (Math.pow(feed, n) % totalPixel);

			while (this.isExist(TabPos, pos)) {
				pos = pos + 1;
			}
			TabPos[n - 1] = pos;

			i = pos % img.getWidth();
			j = pos / img.getWidth();

			rgb = img.getPixel(i, j);
			bin = Integer.toBinaryString(rgb);

			// Penyisipan binaryKey pada LSB
			bin = bin.substring(0, bin.length() - 1);
			bin = bin.concat(binaryKey.substring(n - 1, n));

			long avoidOverflows = Long.parseLong(bin, 2);
			rgb = (int) avoidOverflows;
			img.setPixel(i, j, rgb);
		}

	}

	public void sisipKeyRC4(short[] key) {
		int pos, i, j, b;
		int rgb, awal, akhir = 0, bit = 8;
		String bin;
		String binaryKey;
		counter = key.length * bit;
		TabPos = new int[counter + total];

		for (int idx = 0; idx < key.length; idx++) {
			b = 1;
			binaryKey = Integer.toBinaryString(0x100 | key[idx]).substring(1);

			awal = (idx * bit) + 1;
			akhir = awal + bit - 1;
			for (int n = awal; n <= akhir; n++) {

				pos = (int) (Math.pow(feed, n) % totalPixel);

				while (this.isExist(TabPos, pos)) {
					pos = pos + 1;
				}
				TabPos[n - 1] = pos;

				i = pos % img.getWidth();
				j = pos / img.getWidth();

				rgb = img.getPixel(i, j);
				bin = Integer.toBinaryString(rgb);

				// Penyisipan binaryKey pada LSB
				bin = bin.substring(0, bin.length() - 1);
				bin = bin.concat(binaryKey.substring(b - 1, b));

				long avoidOverflows = Long.parseLong(bin, 2);
				rgb = (int) avoidOverflows;
				img.setPixel(i, j, rgb);

				b++;
			}
		}
	}

	public short[] ambilKeyRC4() {
		int pos, i, j, length = 256;
		int rgb, awal, akhir = 0, bit = 8;
		String bin, digitAkhir;
		String binaryKey;
		counter = length * bit;
		newTabPos = new int[counter + total];
		short[] key = new short[length];

		for (int idx = 0; idx < length; idx++) {
			binaryKey = "";

			awal = (idx * bit) + 1;
			akhir = awal + bit - 1;

			for (int n = awal; n <= akhir; n++) {
				pos = (int) (Math.pow(feed, n) % totalPixel);

				while (this.isExist(newTabPos, pos)) {
					pos = pos + 1;
				}
				newTabPos[n - 1] = pos;
				i = pos % img.getWidth();
				j = pos / img.getWidth();

				rgb = img.getPixel(i, j);
				bin = Integer.toBinaryString(rgb);

				digitAkhir = bin.substring(bin.length() - 1, bin.length());
				binaryKey = binaryKey.concat(digitAkhir);
			}
			key[idx] = Short.parseShort(binaryKey, 2);
		}
		return key;
	}
}
