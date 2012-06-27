package com.ketan.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TransparentImgcropActivity extends Activity implements
		OnClickListener, OnTouchListener {
	/** Called when the activity is first created. */
	/** Called when the activity is first created. */
	private Button btnSetImg, btnOrientation, btnCrop, btnSave, btnFramList,
			btnSaveFull;
	private int SELECT_PICTURE = 1;
	private int SELECT_FRAME = 0;
	private Bitmap bitmapPhoto = null;
	private Bitmap bitmapResult = null ;
	private ImageView imgPhoto, imgFrame;
	// These matrices will be used to move and zoom image
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	public static String fileNAME;
	public static int framePos = 0;

	private float scale = 0;
	private float newDist = 0;

	// Fields
	private String TAG = this.getClass().getSimpleName();

	private ProgressDialog progressDialog;
	// We can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;

	// Remember some things for zooming
	private PointF start = new PointF();
	private PointF mid = new PointF();
	float oldDist = 1f;
	private int rotate = 0;
	private static final int HORIZONTAL = 0;
	private static final int VERTICAL = 1;

	// /////Image view's and image height and width///////
	private int imgPhotoHeight = 0;
	private int imgPhotoWidth = 0;
	private float w = 0;
	private float h = 0;
	// private MyImageView myImageView;
	private RelativeLayout relativeFinalImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		btnSetImg = (Button) findViewById(R.id.btn_getImg);
		btnFramList = (Button) findViewById(R.id.btn_framelist);
		btnSave = (Button) findViewById(R.id.btn_save);
		btnSaveFull=(Button)findViewById(R.id.btn_savefull);
		btnCrop = (Button) findViewById(R.id.btn_crop);
		btnOrientation = (Button) findViewById(R.id.btn_orientation);

		btnCrop.setOnClickListener(this);
		relativeFinalImage = (RelativeLayout) findViewById(R.id.heyelloFrontRelImageLayoutmain);

		imgPhoto = (ImageView) findViewById(R.id.heyelloFrontImageViewPhoto);
		imgFrame = (ImageView) findViewById(R.id.heyelloFrontImageViewFrame);

		btnSave.setVisibility(8);
		btnCrop.setVisibility(8);
		btnSetImg.setVisibility(8);
		btnOrientation.setVisibility(8);
		btnSaveFull.setVisibility(8);

		btnFramList.setOnClickListener(this);
		btnSetImg.setOnClickListener(this);
		btnOrientation.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnSaveFull.setOnClickListener(this);

		imgPhoto.setVisibility(8);
		imgFrame.setVisibility(8);

		imgPhoto.setOnTouchListener(this);
		imgPhoto.setScaleType(ImageView.ScaleType.MATRIX);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PICTURE) {
			if (resultCode == RESULT_OK) {
				newDist = 0;
				Uri selectedImageUri = data.getData();
				Util.photoPath = getPath(selectedImageUri);
				setPhoto();
				// btnSetImg.setVisibility(8);
			}
		} else {
			if (resultCode == RESULT_OK) {
				Log.v("get data ", data.getIntExtra("pos", 0) + "");
				framePos = data.getIntExtra("pos", 0);
				btnFramList.setVisibility(8);
				imgFrame.setVisibility(1);
				btnSetImg.setVisibility(1);
				btnOrientation.setVisibility(1);
			}
		}

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void setPhoto() {

		imgPhoto.setVisibility(1);
		File file = new File(Util.photoPath);
		if (bitmapPhoto != null)
			bitmapPhoto.recycle();

		bitmapPhoto = null;

		imgPhotoHeight = imgPhoto.getHeight() - imgPhoto.getPaddingTop()
				- imgPhoto.getPaddingBottom();
		imgPhotoWidth = imgPhoto.getWidth() - imgPhoto.getPaddingLeft()
				- imgPhoto.getPaddingRight();

		scale = 1;
		Matrix mat = new Matrix();
		matrix.reset();
		try {

			final BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			if (o.outHeight >= 1944 || o.outWidth >= 1944) {
				scale = 2;

			} else if (o.outWidth * imgPhotoHeight > imgPhotoWidth
					* o.outHeight) {
				scale = (float) imgPhotoHeight / (float) o.outHeight;
			} else {
				scale = (float) imgPhotoWidth / (float) o.outWidth;
			}
			final BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = (int) scale;
			bitmapPhoto = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);
			imgPhoto.setImageBitmap(bitmapPhoto);
			// Util.bitmapFrame=bitmapPhoto;
			w = bitmapPhoto.getWidth();
			h = bitmapPhoto.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}

		float widthScale = Math.min(imgPhotoWidth / w, scale);
		float heightScale = Math.min(imgPhotoHeight / h, scale);
		float scale1 = Math.min(widthScale, heightScale);

		matrix.postConcat(mat);
		matrix.postScale(scale1, scale1);
		matrix.postTranslate((imgPhotoWidth - w * scale) / 2F,
				(imgPhotoHeight - h * scale) / 2F);
		savedMatrix.set(matrix);
		center(true, true, imgPhoto);
		imgPhoto.setImageMatrix(matrix);
		relativeFinalImage.setBackgroundDrawable(null);
	}

	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.MATRIX);

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: // first finger down only
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP: // first finger lifted
		case MotionEvent.ACTION_POINTER_UP: // second finger lifted
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // second finger down
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 5f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}

			break;

		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) { // movement of first finger
				matrix.set(savedMatrix);
				if (view.getLeft() >= -392) {
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);

				}
			} else if (mode == ZOOM) { // pinch zooming
				newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 5f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist; // **//thinking i need to
												// play around with this
												// value to limit it**
					matrix.postScale(scale, scale, mid.x, mid.y);
					center(true, true, view);
					Log.e(TAG, "ScaleValue" + scale + "\t\tMidX-" + mid.x
							+ "\t\tMidy-" + mid.y);
				}
			}

			break;
		}

		view.setImageMatrix(matrix);
		Util.photoMatrix = matrix;

		return true;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// private void doVertical() {
	// matrix.postRotate(90);
	// center(true, true, imgPhoto);
	// rotate = VERTICAL;
	// imgPhoto.setImageMatrix(matrix);
	// }
	//
	private void doHorizontal() {

		matrix.postRotate(-90);
		center(true, true, imgPhoto);
		rotate = HORIZONTAL;
		imgPhoto.setImageMatrix(matrix);

	}

	// /////////This function is added to translate the image in
	// center.///////////////
	protected void center(boolean horizontal, boolean vertical, ImageView view) {

		final Matrix m = matrix;

		RectF rect = new RectF(0, 0, w, h);

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = imgPhotoHeight;
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = imgPhotoHeight - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = imgPhotoWidth;
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}

		matrix.postTranslate(deltaX, deltaY);
		Util.photoMatrix = matrix;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSetImg) {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					(Intent.createChooser(intent, "Select Picture")),
					SELECT_PICTURE);
			// imgFrame.setVisibility(1);
			btnCrop.setVisibility(1);
			btnSaveFull.setVisibility(1);

		} else if (v == btnOrientation) {
			doHorizontal();
		} else if (v == btnCrop) {
			Log.v("tag ", "call on imgView");
			cropIMG();
			btnCrop.setVisibility(8);
			btnSave.setVisibility(1);
			btnSaveFull.setVisibility(8);
		} else if (v == btnSave) {
			seavImg();
			btnSave.setVisibility(8);
			btnFramList.setVisibility(1);
			btnSetImg.setVisibility(8);
			btnOrientation.setVisibility(8);
			Toast.makeText(TransparentImgcropActivity.this, "your img save ", Toast.LENGTH_SHORT).show();

		} else if (v == btnFramList) {
			Intent getFrame = new Intent(this, ListOfFrame.class);
			startActivityForResult(getFrame, SELECT_FRAME);
			imgPhoto.setVisibility(8);
			btnCrop.setVisibility(8);
			btnSave.setVisibility(8);
		}else if (v==btnSaveFull) {
			savaWithFrame();
			btnSaveFull.setVisibility(8);
			Toast.makeText(TransparentImgcropActivity.this, "your img save ", Toast.LENGTH_SHORT).show();
			btnFramList.setVisibility(1);
		}
	}

	private void savaWithFrame() {
		// TODO Auto-generated method stub
		Bitmap srcImage = bitmapPhoto;

		// Bitmap bitmapResult = Bitmap.createBitmap(imgPhoto.getWidth(),
		// imgPhoto.getHeight(), Bitmap.Config.ARGB_8888);
	
		bitmapResult = Bitmap.createBitmap(MyImageView.w, MyImageView.h,
				Bitmap.Config.ARGB_8888);
		// Path path = new Path();

		// This is my border
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);

		Canvas canvas = new Canvas(bitmapResult);
		canvas.drawBitmap(srcImage, matrix, paint);
		canvas.drawBitmap(MyImageView.mOriginalBitmap, 0, 0, null);
		
		
		String filename = String.valueOf(System.currentTimeMillis());
		String fpath = Environment.getExternalStorageDirectory().toString();
		OutputStream fOut = null;
		File file = new File(fpath, filename + "_cropIMG.png");
		fileNAME = fpath + "/" + filename + "_cropIMG.png";
		try {
			fOut = new FileOutputStream(file);
			bitmapResult.compress(Bitmap.CompressFormat.PNG, 90, fOut);
			fOut.flush();
			fOut.close();
			Log.v("save ", "done");
			MediaStore.Images.Media.insertImage(getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void seavImg() {
		// TODO Auto-generated method stub
		String filename = String.valueOf(System.currentTimeMillis());
		String fpath = Environment.getExternalStorageDirectory().toString();
		OutputStream fOut = null;
		File file = new File(fpath, filename + "_cropIMG.png");
		fileNAME = fpath + "/" + filename + "_cropIMG.png";
		try {
			fOut = new FileOutputStream(file);
			bitmapResult.compress(Bitmap.CompressFormat.PNG, 90, fOut);
			fOut.flush();
			fOut.close();
			Log.v("save ", "done");
			MediaStore.Images.Media.insertImage(getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void cropIMG() {
		// TODO Auto-generated method stub
		// imgFrame.setVisibility(8);
		progressDialog = ProgressDialog.show(TransparentImgcropActivity.this,
				"", "wait", true, false);
		btnCrop.setVisibility(8);
		btnSetImg.setVisibility(8);
		btnFramList.setVisibility(1);

		Bitmap srcImage = bitmapPhoto;

		// Bitmap bitmapResult = Bitmap.createBitmap(imgPhoto.getWidth(),
		// imgPhoto.getHeight(), Bitmap.Config.ARGB_8888);
	
		bitmapResult = Bitmap.createBitmap(MyImageView.w, MyImageView.h,
				Bitmap.Config.ARGB_8888);
		// Path path = new Path();

		// This is my border
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);

		Canvas canvas = new Canvas(bitmapResult);

		// path.setFillType(Path.FillType.EVEN_ODD);
		//
		// path.setLastPoint(MyImageView.xPoint.get(MyImageView.xPoint.size()-1)
		// ,MyImageView.yPoint.get(MyImageView.yPoint.size()-1));
		// path.lineTo(MyImageView.xPoint.get(0),MyImageView.yPoint.get(0));
		// for (int i = 1; i < MyImageView.xPoint.size(); i++) {
		// path.lineTo(MyImageView.xPoint.get(i),MyImageView.yPoint.get(i));
		//
		// }
		//
		// canvas.clipPath(path, Region.Op.INTERSECT);

		// The image is drawn within the area of two rectangles and a circle
		// Although I suppose that puting Paint object into drawBitmap()
		// method will add a red border on result image but it doesn't work
		canvas.drawBitmap(srcImage, matrix, paint);

		int Mw, Mh;
		Mw = MyImageView.w;
		Mh = MyImageView.h;

		// final int[] pixels = new int[w * h];
		// srcImage.getPixels(pixels, 0, w, 0, 0, w, h);

		// points = new ArrayList<Integer>();

		// xPoint = new ArrayList<Integer>();
		// yPoint = new ArrayList<Integer>();

		// Log.v("width", w + "");
		// Log.v("height", h + "");
		Log.v("size", MyImageView.xPoint.size() + "");

		// final Bitmap bitmapTry=srcImage.copy(Config.ARGB_8888, true);

		int i = 0;
		int k = MyImageView.xPoint.size();

		for (int x = 0; x < Mw; x++) {
			// int firstY = -1, lastY = -1;
			Log.v("X", "" + x);
			for (int y = 0; y < Mh; y++) {
				Log.v("y", "" + y);
				Log.v("macha point", x + " = " + MyImageView.xPoint.get(y));
				// if (x == MyImageView.xPoint.get(y)) {
				// Log.v("macha point", x + " = " + MyImageView.xPoint.get(y));
				// for (int k = 0; k < MyImageView.xPoint.size(); k++) {
				if (x == MyImageView.xPoint.get(i)
						&& y == MyImageView.yPoint.get(i)) {
					Log.v("macha point", x + " = " + MyImageView.xPoint.get(i)
							+ "  " + y + " = " + MyImageView.yPoint.get(i));

					i++;
					if (i == k) {
						i = 0;
					}

				} else {
					bitmapResult.setPixel(x, y, Color.TRANSPARENT);
				}
			}

		}

		imgPhoto.setImageBitmap(bitmapResult);
		imgFrame.setVisibility(8);
		progressDialog.dismiss();
	}
}