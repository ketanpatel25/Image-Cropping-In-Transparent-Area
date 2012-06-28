package com.ketan.demo;

import android.graphics.Bitmap;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class ImageProcessor {
	Bitmap mImage, mImage1;

	boolean mIsError = false;

	public ImageProcessor(final Bitmap image, final Bitmap image1) {
		mImage = image.copy(image.getConfig(), image.isMutable());
		mImage1 = image1.copy(image1.getConfig(), image1.isMutable());

		if (mImage == null) {
			mIsError = true;
		}
	}

	public boolean isError() {
		return mIsError;
	}

	public void setImage(final Bitmap image) {
		mImage = image.copy(image.getConfig(), image.isMutable());
		if (mImage == null) {
			mIsError = true;
		} else {
			mIsError = false;
		}
	}

	public Bitmap getImage() {
		if (mImage == null) {
			return null;
		}
		return mImage.copy(mImage.getConfig(), mImage.isMutable());
	}

	public void free() {
		if (mImage != null && !mImage.isRecycled()) {
			mImage.recycle();
			mImage = null;
		}
	}

	public Bitmap replaceColor(int fromColor, int targetColor) {
		if (mImage == null) {
			return null;
		}

		int width = mImage.getWidth();
		int height = mImage.getHeight();
		int width1 = mImage1.getWidth();
		int height1 = mImage1.getHeight();


		int[] pixels = new int[width * height];
		int[] pixels1 = new int[width * height];
		mImage.getPixels(pixels, 0, width, 0, 0, width, height);
		mImage1.getPixels(pixels1, 0, width1, 0, 0, width1, height1);

		for (int x = 0; x < pixels.length; ++x) {
			pixels[x] = (pixels[x] != fromColor) ? fromColor : pixels1[x];

		}

		Bitmap newImage = Bitmap.createBitmap(width, height, mImage1.getConfig());
		newImage.setPixels(pixels, 0, width, 0, 0, width, height);

		return newImage;
	}
}
