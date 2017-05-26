package org.uiieditt.noise;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.uiieditt.util.CustomDialog;

public class BerandaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beranda_activity);
		initClick();

        AdView homeads = (AdView) findViewById(R.id.homeads);
        AdRequest adRequest = new AdRequest.Builder().build();
        homeads.loadAd(adRequest);
    }

	private void initClick() {
		Button encrypt = (Button) findViewById(R.id.buttonEnskripsi);
		Button decrypt = (Button) findViewById(R.id.buttonDeskripsi);

		encrypt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(CreateNewNoise.class
						.getPackage().getName(), CreateNewNoise.class
						.getCanonicalName()));
				startActivity(intent);
			}
		});

		decrypt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(FileChooser.class
						.getPackage().getName(), FileChooser.class
						.getCanonicalName()));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.menu_beranda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_beranda_about:
				CustomDialog dialog = new CustomDialog(this);
				dialog.setTitle("About Noise");
				dialog.setMessage("Dibuat oleh Didit Velliz");
				dialog.setDefaultButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				dialog.showAlert(true);
				break;
			case R.id.menu_beranda_setting:
				CustomDialog setting = new CustomDialog(this);
				setting.setTitle("Noise Setting");
				setting.setMessage("Under Construction");
				setting.setDefaultButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				setting.showAlert(false);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
}
