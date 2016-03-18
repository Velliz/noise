package org.uiieditt.noise;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noise);
		initClick();

        AdView homeads = (AdView) findViewById(R.id.homeads);
        AdRequest adRequest = new AdRequest.Builder().build();
        homeads.loadAd(adRequest);
    }

	private void initClick() {
		Button enskripsi = (Button) findViewById(R.id.buttonEnskripsi);
		Button deskripsi = (Button) findViewById(R.id.buttonDeskripsi);

		enskripsi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(GoNoise.class
						.getPackage().getName(), GoNoise.class
						.getCanonicalName()));
				startActivity(intent);
			}
		});

		deskripsi.setOnClickListener(new OnClickListener() {

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
}
