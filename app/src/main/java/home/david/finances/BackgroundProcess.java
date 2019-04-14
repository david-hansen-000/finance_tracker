package home.david.finances;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by david on 2/28/18.
 */

public class BackgroundProcess {
    private Bitmap original;
    private int color;
    private ImageView imageView;

    public BackgroundProcess(Bitmap original, int color, ImageView imageView) {
        this.original = original;
        this.color = color;
        this.imageView = imageView;
        imageView.setImageBitmap(original);
        new Background(original, color, imageView).execute("");
    }

    public void changeBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        new Background(bitmap, color, imageView).execute("");
    }

    private class Background extends AsyncTask<String, Void, Bitmap> {
        private Bitmap copy;
        private Bitmap original;
        private int width;
        private int height;
        private int color;
        private ImageView imageView;

        public Background(Bitmap bitmap, int color, ImageView imageView) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            original = bitmap;
            this.color = color;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            copy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int c = original.getPixel(x, y);
                    if (c == -1) {
                        copy.setPixel(x, y, color);
                    } else {
                        if (c==-16777216) {
                            copy.setPixel(x, y, -16777216);
                        } else {
                            copy.setPixel(x,y,-2147483648);
                        }
                    }
                }
            }
            return copy;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(copy);
        }
    }
}
