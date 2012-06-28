package com.ketan.demo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	private Context cont;
	private WindowManager windowManager;
	private int width, height;
	public static int w, h;
	public static Bitmap mOriginalBitmap = null;
	public static int[] listOfFrame = {
			R.drawable.antique_look_hand_carved_mirror_frames,
			R.drawable.back_and_gold_frame, R.drawable.balloons_frame,
			R.drawable.blue_photo_frame, R.drawable.flowers_frame,
			R.drawable.frame_carton, R.drawable.frame_for_photo_barbie,
			R.drawable.frame, R.drawable.fss_fall_frame, R.drawable.gold_frame,
			R.drawable.gold_rose_frame, R.drawable.heart_wedding_flower_frame,
			R.drawable.love_picture_frame, R.drawable.mickey_frame,
			R.drawable.miss_you, R.drawable.mysticmorning_frame,
			R.drawable.photo_frame, R.drawable.pink_yellow_scrapbook_frame,
			R.drawable.wc_toocute_frame, R.drawable.wedding_frame,
			R.drawable.wedding_photo_frame,R.drawable.yellow_dotted_frame };


	// private ArrayList<Integer> points;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.cont = context;
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		windowManager = (WindowManager) this.cont
				.getSystemService(Context.WINDOW_SERVICE);
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		// Load image from resource
		mOriginalBitmap = BitmapFactory.decodeResource(getResources(),
				listOfFrame[TransparentImgcropActivity.framePos], options);
		// Scale to target size
		int iW = mOriginalBitmap.getWidth();
		int iH = mOriginalBitmap.getHeight();
		Log.v("iW", iW + "");
		Log.v("iH", iH + "");

		if (width <= iW) {
			w = width;
		} else {
			w = iW;
		}
		if (height <= iH) {
			h = height;
		} else {
			h = iH;
		}

		mOriginalBitmap = Bitmap
				.createScaledBitmap(mOriginalBitmap, w, h, true);

		canvas.drawBitmap(mOriginalBitmap, 0, 0, null);

		final int[] pixels = new int[w * h];
		mOriginalBitmap.getPixels(pixels, 0, w, 0, 0, w, h);


	}

}
