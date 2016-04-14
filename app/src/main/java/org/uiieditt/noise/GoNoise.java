package org.uiieditt.noise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import org.uiieditt.core.stegano.PanelGambar;
import org.uiieditt.io.MobiProgressBar;

public class GoNoise extends Activity implements OnClickListener {

    private PanelGambar imagePanel;

    private Bitmap bitmaps = null;
    private String picturePath = null;

    private Context context;
    private MobiProgressBar progressBar;
    private final Handler handler = new Handler();

    private long endTime;

    private int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gonoise);
        context = this;
        initClick();
    }

    private void initClick() {
        Button pilihGambar = (Button) findViewById(R.id.enc_pilihGambar);
        pilihGambar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Button enkrip = (Button) findViewById(R.id.enc_buttonEnskripsi);
        enkrip.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = (ImageView) findViewById(R.id.enc_imageRC4);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            bitmaps = BitmapFactory.decodeFile(picturePath);

            cursor.close();
            imageView.setImageBitmap(bitmaps);
        }
    }

    final Runnable kesalahanKunci = new Runnable() {
        public void run() {
            progressBar.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Kunci Tidak Boleh Kosong")
                    .setCancelable(false).setPositiveButton(context.getText(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GoNoise.this.finish();
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
            builder.setMessage(
                    "Noise dibuat.! Waktu : " + endTime / 1000000000.0
                            + " detik")
                    .setCancelable(false).setPositiveButton(context.getText(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GoNoise.this.finish();
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

    @Override
    public void onClick(View v) {
        TextView arc4_key = (TextView) findViewById(R.id.enc_kunciRC4);
        String kunci_arc4 = arc4_key.getText().toString();

        if (kunci_arc4 != null) {

            File f = new File(picturePath);
            imagePanel = new PanelGambar(f);
            imagePanel.setNewKey(kunci_arc4);
            imagePanel.setFeed(kunci_arc4);

            progressBar = new MobiProgressBar(GoNoise.this);
            progressBar.setMax(100);
            progressBar.setCancelable(false);
            progressBar.setMessage(context .getString(R.string.gonoise));
            progressBar.show();

            Thread tt = new Thread(new Runnable() {
                long start = System.nanoTime();

                public void run() {
                    imagePanel.encryptAll(new ProgressHandler() {
                        private int fromSize;
                        private int actualSize;

                        @Override
                        public void setTotal(int tot) {
                            fromSize = tot / 50;
                            handler.post(mInitializeProgress);
                        }

                        @Override
                        public void increment(int inc) {
                            actualSize += inc;
                            if (actualSize % fromSize == 0)
                                handler.post(mIncrementProgress);
                        }

                        @Override
                        public void finished() {

                        }
                    });

                    handler.post(mSetInderminate);
                    bitmaps = imagePanel.getImage();
                    endTime = System.nanoTime() - start;
                    imagePanel.saveImage(getBaseContext());
                    handler.post(waktuEnskripsi);
                }
            });
            tt.start();
        } else {
            Toast.makeText(context, "Kesalahan, Belum mengisi kunci RC4 dan Kunci Feed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
