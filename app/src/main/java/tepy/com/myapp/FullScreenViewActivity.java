package tepy.com.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FullScreenViewActivity extends AppCompatActivity {
    public static final String TAG = "LEGEND";
    private Cursor cursor;
    int columnIndex = 0;
    int columnIndex2 = 0;
    int columnIndex3 = 0;
    private PagerAdapter mPagerAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        init();
        Intent i = getIntent();
        int position = i.getExtras().getInt("position");
        Log.d(TAG, " Postion received is " + position);
        mPagerAdapter = new FullScreenImageAdapter(this, cursor);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(position);
    }

    public void init() {
        String[] mProjection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN};
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection, null, null, MediaStore.Images.Media.DATE_TAKEN);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        columnIndex2 = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        columnIndex3 = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
    }

    private class FullScreenImageAdapter extends PagerAdapter {

        private Activity _activity;
        private Cursor _cursor;
        private LayoutInflater inflater;

        // constructor
        public FullScreenImageAdapter(Activity activity,
                                      Cursor imagePaths) {
            this._activity = activity;
            this._cursor = imagePaths;
        }

        @Override
        public int getCount() {
            return this._cursor.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imgDisplay;
            Button btnClose;

            inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.layout_image, container, false);

            imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
            btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

            FullScreenViewActivity.this.cursor.moveToPosition(position);
            Log.d(TAG, " Initial postion at " + position);

            int imageID = FullScreenViewActivity.this.cursor.getInt(columnIndex);
            String imagePath = FullScreenViewActivity.this.cursor.getString(columnIndex2);
            String date = FullScreenViewActivity.this.cursor.getString(columnIndex3);
            long mills = Long.parseLong(date);
            Date d = new Date(mills);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            Log.d(TAG, " Date taken is " + df.format(d));
            //Log.d(TAG, " Image path " + imagePath);

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
//            imgDisplay.setImageBitmap(bitmap);

            imgDisplay.setImageURI(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageID));

            // close button click event
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.finish();
                }
            });

            ((ViewPager) container).addView(viewLayout);

            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }
    }
}
