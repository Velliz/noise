package org.uiieditt.noise;

import java.io.File;

import org.uiieditt.core.stegano.PanelGambar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class RevertBackNoise extends Activity {

	private String gambar = null;
	private PanelGambar panel;

	private final Handler handler = new Handler();
	private org.uiieditt.io.MobiProgressBar progressBar;
	Context context;
	long endtime = 0;

	String seedString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.revert_back_noise);
		context = this;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gambar = extras.getString("fileSelected");
		}
		initClick();

        AdView revertads = (AdView) findViewById(R.id.revertads);
        AdRequest adRequest = new AdRequest.Builder().build();
        revertads.loadAd(adRequest);

	}

	private void initClick() {
		TextView lokasi = (TextView) findViewById(R.id.dec_lokasiGambar);

		lokasi.setText(gambar);

		Button desc = (Button) findViewById(R.id.dec_buttonDeskripsi);
		desc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				TextView seed = (TextView) findViewById(R.id.dec_seedkey);
				seedString = seed.getText().toString();

				panel = new PanelGambar(new File(gambar));
				panel.setFeed(seedString);

				progressBar = new org.uiieditt.io.MobiProgressBar(RevertBackNoise.this);
				progressBar.setMax(100);
				progressBar.setCancelable(false);
				progressBar.setMessage(context.getString(R.string.revertnoise));
				progressBar.show();

				Thread tt = new Thread(new Runnable() {
					@Override
					public void run() {
						long start = System.nanoTime();
						panel.Decrypt(new ProgressHandler() {
							private int mysize;
							private int actualSize;

							@Override
							public void setTotal(int tot) {
								mysize = tot / 50;
								handler.post(mInitializeProgress);

							}

							@Override
							public void increment(int inc) {
								actualSize += inc;
								if (actualSize % mysize == 0)
									handler.post(mIncrementProgress);

							}

							@Override
							public void finished() {

							}
						}, seedString);
						endtime = System.nanoTime() - start;
						handler.post(mSetInderminate);
						if(!panel.isStatus()){
							handler.post(kesalahanKunci2);
						} else {
							handler.post(waktuEnskripsi);
						}
						panel.saveImage2(getBaseContext());
					}
				});
				tt.start();
			}
		});

	}

	final Runnable kesalahanKunci = new Runnable() {
		public void run() {
			progressBar.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Kunci Tidak Boleh Kosong")
					.setCancelable(false)
					.setPositiveButton(context.getText(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									RevertBackNoise.this.finish();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
		}
	};

	final Runnable kesalahanKunci2 = new Runnable() {
		public void run() {
			progressBar.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Kunci Yang Anda Masukan Salah")
					.setCancelable(false)
					.setPositiveButton(context.getText(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							});

			AlertDialog alert = builder.create();
			alert.show();
		}
	};

	final Runnable waktuEnskripsi = new Runnable() {
		public void run() {
			progressBar.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Revert Back Success");
			builder.setMessage(
					"Time Elapsed " + Math.floor(endtime / 1000000000.0) + " Seconds")
					.setCancelable(false)
					.setPositiveButton(context.getText(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									RevertBackNoise.this.finish();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
		}
	};

	final Runnable mIncrementProgress = new Runnable() {
		public void run() {
			progressBar.incrementProgressBy(1);
		}
	};

	final Runnable mInitializeProgress = new Runnable() {
		public void run() {
			progressBar.setMax(100);
		}
	};

	final Runnable mSetInderminate = new Runnable() {
		public void run() {
			progressBar.setMessage(context.getString(R.string.saving));
			progressBar.setIndeterminate(true);
		}
	};
}
