package in.co.praveenkumar.mdroid.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import in.co.praveenkumar.R;

/**
 * Created by praveen on 17.10.15.
 */
public class ImageLoader extends AsyncTask<Object, Void, Bitmap> implements Html.ImageGetter{
    final static String TAG = "ImageLoader";
    private LevelListDrawable mDrawable;

    Context mContext;
    TextView mTextView;

    /**
     * @param context
     *      Context of the activity
     * @param textView
     *      View where images are loaded into
     */
    public ImageLoader(Context context, TextView textView){
        this.mContext = context;
        this.mTextView = textView;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        String source = (String) params[0];
        mDrawable = (LevelListDrawable) params[1];
        Log.d(TAG, "doInBackground " + source);
        try {
            InputStream is = new URL(source).openStream();
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onPostExecute drawable " + mDrawable);
        Log.d(TAG, "onPostExecute bitmap " + bitmap);
        if (bitmap != null) {
            BitmapDrawable d = new BitmapDrawable(bitmap);
            mDrawable.addLevel(1, 1, d);
            mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            mDrawable.setLevel(1);
            // i don't know yet a better way to refresh TextView
            // mTv.invalidate() doesn't work as expected
            CharSequence t = mTextView.getText();
            mTextView.setText(t);
        }
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = mContext.getResources().getDrawable(R.drawable.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        this.execute(source, d);
        return d;
    }
}
