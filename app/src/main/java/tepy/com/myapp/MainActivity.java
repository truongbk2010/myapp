package tepy.com.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LEGEND";
    Cursor cursor;
    int columnIndex = 0;
    int columnIndex2 = 0;
    int width = 0;
    int columnWidth = 0;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;

        Log.d(TAG, " Width of screen is " + width);

        String[] mProjection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN};
        // String[] mProjection = {MediaStore.Images.Thumbnails._ID};
//        long from = 1432141200000L;
//        long to = 1432227599000L;
//        Date d = new Date(to);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
//        Log.d(TAG, " All wedding at " + df.format(d));

//        String selection = MediaStore.Images.Media.DATE_TAKEN + ">? and " + MediaStore.Images.Media.DATE_TAKEN + "<?";
//        String[] args = new String[]{from + "", to + ""};

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection, null, null, MediaStore.Images.Media.DATE_TAKEN);
        //cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, mProjection, null, null, MediaStore.Images.Thumbnails._ID);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        columnIndex2 = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
        //columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

        Log.d(TAG, "Number of images is : " + cursor.getCount());
        //grid view
        GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());

        columnWidth = (int) ((width - (4 * padding)) / 3);

        sdcardImages.setColumnWidth(columnWidth);
        sdcardImages.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        sdcardImages.setHorizontalSpacing((int) padding);
        sdcardImages.setVerticalSpacing((int) padding);

        //sdcardImages.setAdapter(new ImageAdapter(this));

        List<DailyItem> listItems = new ArrayList<>();
        DailyItem dailyItem ;

        int tempDay = -1;
        int tempYear = -1;

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String date = cursor.getString(columnIndex2);
            long mills = Long.parseLong(date);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(new Date(mills));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            Log.d(TAG, " Date taken is " + df.format(cal1.getTime()));
            int day = cal1.get(Calendar.DAY_OF_YEAR);
            int year = cal1.get(Calendar.YEAR);


            Log.d(TAG, " Date taken is " + df.format(cal1.getTime()) + " DAY : " + day);
            if (tempYear == -1 && tempYear== -1 ) {
                tempDay =day;
                tempYear = year;
                dailyItem= new DailyItem();
                //dailyItem.
            }else{
                if(day == tempDay && year == tempYear){
                  //  dailyItem.

                }else{




                }
            }

        }


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    private class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context localContext) {
            context = localContext;
        }

        public int getCount() {
            return cursor.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                // Log.d(TAG, " Image view is null");
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                picturesView.setPadding(4, 4, 4, 4);
                picturesView.setLayoutParams(new GridView.LayoutParams(columnWidth, columnWidth));
                picturesView.setOnClickListener(new OnImageClickListener(position));
            } else {
                picturesView = (ImageView) convertView;
            }
            // Move cursor to current position
            cursor.moveToPosition(position);
            Log.d(TAG, " row index of image is " + position);
            // Get the current value for the requested column
            int imageID = cursor.getInt(columnIndex);

            // String[] mProjection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN};
            // Cursor cursor2 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection, null, null, MediaStore.Images.Media.DATE_TAKEN);
            //  int columnIndex2 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            //  cursor2.moveToPosition(position);

            String date = cursor.getString(columnIndex2);
            long mills = Long.parseLong(date);
            Date d = new Date(mills);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            Log.d(TAG, " Date taken is " + df.format(d));
            Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), imageID,
                    MediaStore.Images.Thumbnails.MINI_KIND, null);
//            picturesView.setImageURI(Uri.withAppendedPath(
//                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
            picturesView.setImageBitmap(b);
            // Uri uri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
            //Picasso.with(context).load(uri).memoryPolicy(MemoryPolicy.NO_CACHE ).resize(columnWidth, columnWidth).centerCrop().into(picturesView);
            //cursor2.close();
            //imageLoader.displayImage().toString(), picturesView, options);
            return picturesView;
        }

        class OnImageClickListener implements View.OnClickListener {
            int _postion;

            public OnImageClickListener(int position) {
                this._postion = position;
            }

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullScreenViewActivity.class);
                i.putExtra("position", _postion);
                context.startActivity(i);
            }
        }


    }


}
