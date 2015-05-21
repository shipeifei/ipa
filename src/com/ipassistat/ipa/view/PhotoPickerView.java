package com.ipassistat.ipa.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.array;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.PhotoPickerAdapter;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.ui.activity.MediaPhotoGridView;
import com.ipassistat.ipa.ui.activity.SearchProductActivity;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.FileUtis;
import com.ipassistat.ipa.util.ImageUtil;
import com.ipassistat.ipa.util.PhotoPickerHandler;
import com.ipassistat.ipa.view.PopMenu.OnItemClickListener;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 添加照片的工具类（包括图片和化妆品）
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 */
public class PhotoPickerView extends LinearLayout implements OnClickListener {
	public static final String INTENT_KEY_PRODUCT = "intent_product";
	// private static final String TAG = "PhotoPickerView";

	// 返回的数据，外部可以获取
	// private List<SoftReference<Bitmap>> mBitmaps;
	private SaleProduct mProduct;
	// private ImageWorker mImageWorker;
	private BitmapUtils mBitmapUtils;

	private ArrayList<String> mSelectMediaBitmapPathArray;// 选中的媒体文件

	private int mMaxPhotoSelectNumber = 9;
    
	public PhotoPickerView(Context context) {
		super(context);
		init();
	}

	public PhotoPickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PhotoPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("InflateParams")
	private void init() {
		setGravity(Gravity.CENTER_VERTICAL);
		
		
		View root = LayoutInflater.from(getContext()).inflate(R.layout.activity_photo_picker, null);
		root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(root);

		findViewById(id.button_image_add).setOnClickListener(this);
		findViewById(id.button_product_add).setOnClickListener(this);

		
		mBitmapUtils = BitmapOptionsFactory.getInstance(getContext());
		mSelectMediaBitmapPathArray = new ArrayList<String>();
		
		// 删除之前的拍照缓存
		PhotoPickerHandler.deleteAllCameraCache();
	}

	/**
	 * 隐藏选择商品的按钮
	 * 
	 * @param hide
	 * @author LiuYuHang
	 * @date 2014年11月24日
	 */
	public void hideProduct(boolean hide) {
		findViewById(id.button_product_add).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);

		LinearLayout parentView = (LinearLayout) findViewById(id.picker_view);
		parentView.setGravity(Gravity.CENTER_VERTICAL);
	}

	/**
	 * 从onActivityResult传入的参数
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("onActivityResult requestCode: " + requestCode + " resultCode:" + resultCode + " ");
		if (resultCode == PhotoPickerHandler.RESULT_NONE)
			return;

		if (requestCode == PhotoPickerHandler.RESULT_PRODUCT) {
			if (data == null)
				return;
			mProduct = (SaleProduct) data.getSerializableExtra(INTENT_KEY_PRODUCT);
			// System.out.println("product name:" + mProduct.title);
			updateUI();
		} else if (requestCode == PhotoPickerHandler.TYPE_MEDIA) {
			// if (mBitmaps != null) mBitmaps.clear();// 清除之前选择的
			// ImageCache imageCache = ImageCache.createCache();
			mSelectMediaBitmapPathArray = data.getStringArrayListExtra("images");
			// for (String imageUrl : mSelectMediaBitmapPath) {
			// Bitmap bitmap = imageCache.decodeBitmap(imageUrl);
			// addBitmapArray(bitmap);
			// }
			updateUI();
		} else {
			// 拍照
			if (requestCode == PhotoPickerHandler.RESULT_PHOTO_GRAPH) {
				File lastCameraFile = new File(PhotoPickerHandler.getLastCameraPath());
				// 设置文件保存路径
				// File picture = new
				// File(Environment.getExternalStorageDirectory() + "/" +
				// PhotoPickerHandler.DEFAULT_IMAGE_TEMP_FILE_NAME);
				/**
				 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
				 */
				int degree = ImageUtil.readPictureDegree(lastCameraFile.getAbsolutePath());
				if (degree != 0) {
					Bitmap oldBitmap = BitmapFactory.decodeFile(lastCameraFile.getAbsolutePath(), new BitmapFactory.Options());
					Bitmap saveBitmap = ImageUtil.rotaingImageView(degree, oldBitmap);
					FileUtis.saveMyBitmap(lastCameraFile.getAbsolutePath(), saveBitmap);
				}
				mBitmapUtils.clearCache(lastCameraFile.getAbsolutePath());
				mSelectMediaBitmapPathArray.add(lastCameraFile.getAbsolutePath());
				updateUI();
			}

			if (data != null) {
				// 读取相册缩放图片
				if (requestCode == PhotoPickerHandler.RESULT_PHOTO_ZOOM) {
					PhotoPickerHandler.StartPhotoZoom((Activity) getContext(), data.getData());
				}
			}

			// 处理结果---以上代码都可以忽略，不管从相机还是相册获取的图片都需要进行裁剪，所以直接进入下面
			if (requestCode == PhotoPickerHandler.RESULT_PHOTO_RESULT) {
				// if (requestCode == PhotoPickerHandler.RESULT_PHOTO_GRAPH) {
				// File picture = new
				// File(Environment.getExternalStorageDirectory() +
				// "/temp.jpg");
				// photo =
				// ImageUtil.decodeSampledBitmapFromFile(picture.getPath(), 100,
				// 100);
				// } else {
				// Bundle extras = data.getExtras();
				// Bitmap photo = extras.getParcelable("data");
				// }
				// addBitmapArray(photo);

				updateUI();
				/*
				 * if (extras != null) { Bitmap photo =
				 * extras.getParcelable("data"); ByteArrayOutputStream stream =
				 * new ByteArrayOutputStream();
				 * photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);//
				 * (0-100)压缩文件 //
				 * 此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin
				 * /archive/2011/12/28/2304940.html
				 * imageView.setImageBitmap(photo); // 把图片显示在ImageView控件上 }
				 */
			}
		}

	}

	/**
	 * 获取选择的Bitmap
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 * 
	 * @return
	 */
	// public List<SoftReference<Bitmap>> getBitmaps() {
	// if (mBitmaps == null || mBitmaps.size() == 0) return null;
	// return mBitmaps;
	// }

	public List<String> getBitmapPath() {
		if (mSelectMediaBitmapPathArray == null || mSelectMediaBitmapPathArray.size() == 0)
			return null;
		return mSelectMediaBitmapPathArray;
	}

	/**
	 * 获取选择的商品
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 * 
	 * @return
	 */
	public SaleProduct getProduct() {
		return mProduct;
	}

	/**
	 * 清除选择的数据 包括选择的图片和商品
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月28日
	 */
	public void clear() {
		// if (mBitmaps != null) mBitmaps.clear();
		if (mSelectMediaBitmapPathArray != null)
			mSelectMediaBitmapPathArray.clear();
		mProduct = null;
		updateUI();
	}

	/**
	 * 根据当前的数据，刷新UI
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 * 
	 */
	private void updateUI() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		View pickerView = findViewById(id.picker_view);
		GridView imagesGridView = (GridView) findViewById(id.gridview_images);
		View productView = findViewById(id.view_product);

		if (mSelectMediaBitmapPathArray != null && mSelectMediaBitmapPathArray.size() != 0) {
			pickerView.setVisibility(View.GONE);
			productView.setVisibility(View.GONE);
			imagesGridView.setVisibility(View.VISIBLE);

			// mBitmapUtils.clearCache();

			PhotoPickerAdapter adapter = (PhotoPickerAdapter) imagesGridView.getAdapter();

			if (adapter == null) {
				adapter = new PhotoPickerAdapter(getContext(), mSelectMediaBitmapPathArray, mBitmapUtils);
			} else {
				adapter.notifyWithData(mSelectMediaBitmapPathArray);
			}
			adapter.setMaxNumber(mMaxPhotoSelectNumber);

			adapter.setOnAddImageListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popPhotoPicerkWindow(v);
				}
			});

			adapter.setImageListener(new OnItemClickListener() {

				@Override
				public void onItemClick(View v, final int position) {
					Intent intent = new Intent(getContext(), MediaPhotoGridView.class);
					intent.putStringArrayListExtra(MediaPhotoGridView.INTENT_SELECT, mSelectMediaBitmapPathArray);
					intent.putExtra(MediaPhotoGridView.INTENT_STATE, MediaPhotoGridView.STATE_MEDIA_BROWSE);
					intent.putExtra(MediaPhotoGridView.INTENT_SELECT_POSITION, position);
					((Activity) getContext()).startActivityForResult(intent, PhotoPickerHandler.TYPE_MEDIA);

					// PopMenu menu = new PopMenu(getContext());
					// PopMenuItem item = new
					// PopMenuItem(getResources().getString(string.menu_delete),
					// new OnItemClickListener() {
					//
					// @Override
					// public void onItemClick(View v, int p) {
					// mSelectMediaBitmapPath.remove(position);
					// // adapter.removeItem(position);
					// updateUI();
					// }
					// });
					// menu.addItem(item);
					// menu.show(v);
				}
			});

			imagesGridView.setAdapter(adapter);
		} else if (mProduct != null) {
			pickerView.setVisibility(View.GONE);
			productView.setVisibility(View.VISIBLE);
			imagesGridView.setVisibility(View.GONE);

			ViewFiller.fillProductView(mProduct, productView, mBitmapUtils);
			productView.setOnClickListener(this);
		} else {
			pickerView.setVisibility(View.VISIBLE);
			productView.setVisibility(View.GONE);
			imagesGridView.setVisibility(View.GONE);
		}
	}

	private void popPhotoPicerkWindow(View v) {
		String menuList[] = getResources().getStringArray(array.photo_from);
		// PopMenu menu = new PopMenu(getContext());
		//
		// for (int i = 0; i < menuList.length; i++) {
		// PopMenuItem item = new PopMenuItem(menuList[i], new
		// OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(View v, int position) {
		// if (position == 0) {
		// PhotoPickerHandler.open((Activity) getContext(),
		// PhotoPickerHandler.TYPE_CAMERA);
		// } else {
		// Intent intent = new Intent(getContext(), MediaPhotoGridView.class);
		// intent.putStringArrayListExtra(MediaPhotoGridView.INTENT_SELECT,
		// mSelectMediaBitmapPath);
		// ((Activity) getContext()).startActivityForResult(intent,
		// PhotoPickerHandler.TYPE_MEDIA);
		// }
		// }
		// });
		// menu.addItem(item);
		// }
		// menu.show(v);

		AlertDialog dialog = new AlertDialog(getContext());
		dialog.setItems(menuList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					PhotoPickerHandler.open((Activity) getContext(), PhotoPickerHandler.TYPE_CAMERA);
				} else {
					Intent intent = new Intent(getContext(), MediaPhotoGridView.class);
					intent.putStringArrayListExtra(MediaPhotoGridView.INTENT_SELECT, mSelectMediaBitmapPathArray);
					intent.putExtra(MediaPhotoGridView.INTENT_MAX_SELECT, mMaxPhotoSelectNumber);
					((Activity) getContext()).startActivityForResult(intent, PhotoPickerHandler.TYPE_MEDIA);
				}
			}

		});
		dialog.setNegativeButton(getContext().getString(string.menu_cancel), null);

		dialog.show();
	}

	/**
	 * 给列表添加图片
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 * 
	 * @param bitmap
	 */
	// private void addBitmapArray(Bitmap bitmap) {
	// if (mBitmaps == null) mBitmaps = new ArrayList<SoftReference<Bitmap>>();
	// mBitmaps.add(new SoftReference<Bitmap>(bitmap));
	// }

	@Override
	public void onClick(View v) {
		Activity activity;
		if (getContext() instanceof Activity) {
			activity = (Activity) getContext();
		} else {
			throw new ClassCastException("context 不能转换为 activity");
		}

		switch (v.getId()) {
		case R.id.button_image_add:
			MobclickAgent.onEvent(getContext(), "1149");
			popPhotoPicerkWindow(v);
			break;
		case R.id.button_product_add:
			MobclickAgent.onEvent(getContext(), "1150");
			Intent searchIntent = new Intent(getContext(), SearchProductActivity.class);
			activity.startActivityForResult(searchIntent, PhotoPickerHandler.RESULT_PRODUCT);
			break;
		case id.view_product:
			AlertDialog dialog = new AlertDialog(getContext());
			dialog.setTitle("确定要删除？");
			dialog.setPositiveButton(getContext().getString(string.menu_delete), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mProduct = null;
					updateUI();
				}
			});
			dialog.setNegativeButton(getContext().getString(string.menu_cancel), null);
			dialog.show();

			// PopMenu menu = new PopMenu(getContext());
			// PopMenuItem item = new
			// PopMenuItem(getResources().getString(string.menu_delete), new
			// OnItemClickListener() {
			//
			
			// @Override
			// public void onItemClick(View v, int position) {
			// mProduct = null;
			// updateUI();
			// }
			// });
			// menu.addItem(item);
			// menu.show(v);
			break;
		}

	}

	/**
	 * 设置图片最大选择数量
	 * 
	 * @param i
	 * @author LiuYuHang
	 * @date 2014年11月5日
	 */
	public void setMaxPhotoNumber(int max) {
		this.mMaxPhotoSelectNumber = max;
	}
}
