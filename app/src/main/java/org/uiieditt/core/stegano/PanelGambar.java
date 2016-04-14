package org.uiieditt.core.stegano;

import org.uiieditt.io.DiskScanner;
import org.uiieditt.io.fileutil.FileController;
import org.uiieditt.core.algorithm.KeyStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.util.Log;

import org.uiieditt.noise.ProgressHandler;

public class PanelGambar extends BitmapFactory {

	private boolean status = true;
	public Bitmap newBmp;
	private KeyStream e;
	private int c1 = 10, c2 = 300, w = 300, h = 300;
	private int x, y;
	private long key;
	private double newkey;
	private short[] keyRC4;
	private long feed;
	private boolean onlyRC4 = false;
	private String TAG = "Kesalahan", fileNameDest = "RC4";

	public PanelGambar(File input) {
		Bitmap img = BitmapFactory.decodeFile(input.getAbsolutePath());
		// Create a bitmap of the same size
		newBmp = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
				Config.ARGB_8888);
		// Create a canvas for new bitmap
		Canvas c = new Canvas(newBmp);

		// Draw your old bitmap on it.
		c.drawBitmap(img, 0, 0, new Paint());
	}

	
	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public void setImage(File input) {
		Bitmap img = BitmapFactory.decodeFile(input.getAbsolutePath());
		// Create a bitmap of the same size
		newBmp = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
				Config.ARGB_8888);
		// Create a canvas for new bitmap
		Canvas c = new Canvas(newBmp);

		// Draw your old bitmap on it.
		c.drawBitmap(img, 0, 0, new Paint());
	}

	public void paint(Bitmap bitmap, Bitmap overlay) {
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(overlay, 0, 0, paint);
	}

	public void setFeed(String feed) {
		this.feed = new Long(feed);
	}

	public void setKey(String key) {
		this.key = new Long(key);
		keyRC4 = new KeyStream().paddingKey(this.key);
	}

	public void setNewKey(String key) {
		setKey(key);
		key = "0.".concat(key);
		this.newkey = new Double(key);
	}

	public Bitmap getImage() {
		return newBmp;
	}

	public void encryptAll(ProgressHandler hand) {
		if (hand != null)
			hand.setTotal(w * h);

		c1 = 0;
		c2 = 0;

        w = newBmp.getWidth();
		h = newBmp.getHeight();

		this.manipulate(hand);
		this.sisipKey();
	}

	public void Decrypt(ProgressHandler hand, String inputan) {
		hand.setTotal(w * h);
		this.ambilKeydanCoordinat(inputan);
		this.c1 = x;
		this.c2 = y;
		this.manipulate(hand);
	}

	public boolean manipulate(ProgressHandler hand) {
		short[] keystream;
		int length = w * h;
		int counter = 0;
		int rgb, pixel = 0;
		int r, g, b;

		e = new KeyStream();
		if (!onlyRC4) {
			keystream = e.generateKeyStream(newkey, length);
		} else {
			keystream = e.generateKeyStreamWithoutChaos(keyRC4, length);
		}

		if (keystream != null) {
			for (int i = c1; i < c1 + w; i++) {
				for (int j = c2; j < c2 + h; j++) {
					try {
						rgb = newBmp.getPixel(i, j);
					} catch (Exception e) {
						return false;
					}

					pixel = newBmp.getPixel(i, j);

					r = Color.red(pixel);
					g = Color.green(pixel);
					b = Color.blue(pixel);

					r = keystream[counter] ^ r;
					g = keystream[counter] ^ g;
					b = keystream[counter] ^ b;

					rgb = (r << 16) | (g << 8) | b;

					newBmp.setPixel(i, j, Color.rgb(r, g, b));
					counter++;
					hand.increment(1);
				}
			}
		}
		return true;
	}

	public void saveImage2(Context context) {
		try {
			FileOutputStream fos = new FileOutputStream(getOutputMediaFile2());
			newBmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            DiskScanner.getMedia(new FileController(getOutputMediaFile2()))
                    .startScan(context);

			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}
	}

	public void saveImage(Context context) {
		AndroidBmpUtil bmpUtil = new AndroidBmpUtil();
		bmpUtil.save(newBmp, getOutputMediaFile().toString());
        DiskScanner.getMedia(new FileController(getOutputMediaFile()))
                .startScan(context);

    }

	public File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Noise");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
		File mediaFile;
		String mImageName = "TK_RC4_" + timeStamp + ".bmp";
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mImageName);
		return mediaFile;
	}

	public File getOutputMediaFile2() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Noise");

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm")
				.format(new Date());
		File mediaFile;
		String mImageName = "TK_RC4_" + timeStamp + ".jpg";
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mImageName);
		return mediaFile;
	}

	public void sisipKey() {
		Steganografi s = new Steganografi(feed, newBmp);
		if (!onlyRC4) {
			s.sisipKey(newkey);
		} else {
			s.sisipKeyRC4(keyRC4);
		}
		s.saveCoordinat(x, y, w, h);
		newBmp = s.getImg();
		// this.repaint();
	}

	public void ambilKeydanCoordinat(String inputan) {
		Steganografi s = new Steganografi(feed, newBmp);
		if (!onlyRC4) {
			this.newkey = s.ambilKey();

			//pengecekan kesalahan kunci
			String banding = String.valueOf(this.newkey);
			String subBanding = banding.substring(2);

			if (!subBanding.equals(inputan)) {
				status = false;
			}

		} else {
			keyRC4 = s.ambilKeyRC4();

			//pengecekan kesalahan kunci
			String banding = String.valueOf(this.keyRC4);
			String subBanding = banding.substring(2);

			if (!subBanding.equals(inputan)) {
				status = false;
			}
		}
		int[] titik = s.loadCoordinat();
		this.x = titik[0];
		this.y = titik[1];
		this.w = titik[2];
		this.h = titik[3];
	}

	public int[] loadCoordinat() {
		Steganografi s = new Steganografi(feed, newBmp);
		return s.loadCoordinat();
	}

}
