package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.adapter.ImageSelectViewPagerAdapter;
import com.ipassistat.ipa.adapter.ImageSelectViewPagerAdapter.SelectChangedListener;
import com.ipassistat.ipa.bean.local.ImageDirectoryVo;
import com.ipassistat.ipa.bean.local.ImageFileVo;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.CropImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * 姐妹圈相册图片选择页面
 * 
 * @author LiuYuHang
 * @date 2014年11月5日
 */
public class MediaPhotoGridView extends BaseActivity implements OnClickListener {
	public static final String INTENT_STATE = "currentState";

	// 当前页面的三种状态
	public static final int STATE_MEDIA_DIRECTORY = 1;// 相片的目录列表
	public static final int STATE_MEDIA_GRID = 2;// 宫格的的相片浏览
	public static final int STATE_MEDIA_BROWSE = 3;// ViewPager的图片浏览
	public static final int STATE_MEDIA_PHOTO_PREVIEW = 4;// 点击宫格的照片预览

	public static final String INTENT_SELECT = "select_images";
	public static final String INTENT_MAX_SELECT = "max_select";
	public static final String INTENT_SELECT_POSITION = "select_position";

	private static final String[] STORE_IMAGES = { MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media._ID, };
	// private static final String[] THUMB_STORE_IMAGES = { Thumbnails.DATA,
	// Thumbnails._ID };

	// 当前的页面状态
	private int currentState;

	// private DisplayImageOptions options; // 配置图片加载及显示选项

	// private ImageWorker mImageWorker;
	public static int mMaxSelectNumber = 9;
	private List<ImageDirectoryVo> mImageDirectory;// 相册目录

	private ImageFileAdapter mAdapter;
	private ListView mDirectoryListView;
	private ViewPager mViewPager;
	
	private AQuery aQuery;

	// private BitmapUtils mBitmapUtils;
//	private CropImageListener mCropImageListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(layout.activity_imagegrid);
		findListeners(new int[] { id.left_imgv, id.right_imgv }, this);

		disableSlideClose();
		setTitleText("相片");
		hidLeftButton(true);
//		setTitleRightImage(drawable.button_cancel);
		setTitleRightText("取消", this);
		showRightSpace(true);

		if (getIntent() != null) {
			currentState = getIntent().getIntExtra(INTENT_STATE, STATE_MEDIA_DIRECTORY);
			mMaxSelectNumber = getIntent().getIntExtra(INTENT_MAX_SELECT, mMaxSelectNumber);
		}
		
		aQuery = new AQuery(getApplicationContext());

		// options = new
		// DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher)
		// // 在ImageView加载过程中显示图片
		// .showImageForEmptyUri(R.drawable.ic_launcher) // image连接地址为空时
		// .showImageOnFail(R.drawable.ic_launcher) // image加载失败
		// .cacheInMemory(true) // 加载图片时会在内存中加载缓存
		// .cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存
		// // .displayer(new RoundedBitmapDisplayer(20)) //
		// // 设置用户加载图片task(这里是圆角图片显示)
		// .build();

		// mBitmapUtils = new BitmapUtils(getApplicationContext());
		// mBitmapUtils.configDefaultLoadingImage(drawable.goods_default_img);
//		mCropImageListener = new CropImageListener();

		// ImageCacheParams cacheParams = new ImageCacheParams();
		// cacheParams.reqWidth = getResources().getDisplayMetrics().widthPixels
		// / 5;
		// // cacheParams.reqHeight = (int) (cacheParams.reqWidth * 1.5);
		// cacheParams.reqHeight = (int) (cacheParams.reqWidth);
		// cacheParams.loadingResId = drawable.goods_default_img;
		// // cacheParams.clearDiskCacheOnStart = true;
		// cacheParams.memCacheSize = (1024 * 1024 *
		// Utils.getMemoryClass(MediaPhotoGridView.this)) / 2;
		// cacheParams.clearDiskCacheOnStart = true;

		// mImageWorker = ImageWorker.newInstance(MediaPhotoGridView.this);
		// mImageWorker.addParams(TAG, cacheParams);

		if (currentState == STATE_MEDIA_BROWSE) {
			MobclickAgent.onEvent(getApplicationContext(), "1046");
			int position = getIntent().getIntExtra(INTENT_SELECT_POSITION, 0);
			showImageAdapterView(currentState, null, position);
		} else {
			MobclickAgent.onEvent(getApplicationContext(), "1044");
			initData();// 读取媒体数据
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1044"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1044"); // 统计页面
	}

	/**
	 * 传入List就是显示图片宫格，传入null显示图片目录
	 *
	 * @param list
	 * @author LiuYuHang
	 * @date 2014年10月24日
	 */
	private void showImageAdapterView(int state, List<ImageFileVo> list, int ext) {
		mViewPager = (ViewPager) findViewById(id.viewpager);

		List<String> selectArray = getIntent().getStringArrayListExtra(INTENT_SELECT);
		switch (state) {
		case STATE_MEDIA_DIRECTORY:// 相册页面
			hidLeftButton(true);
			mDirectoryListView.setVisibility(View.VISIBLE);
//			setTitleRightImage(drawable.button_cancel);
			setTitleRightText("取消", this);
			setRightPopText(null);
			break;
		case STATE_MEDIA_GRID:// 图片页面
			hidLeftButton(false);
			mDirectoryListView.setVisibility(View.GONE);
//			setTitleRightImage(drawable.button_ok);
			setTitleRightText("完成", this);

			updateUI(list);
			setRightPopText(selectArray.size() + "");
			break;
		case STATE_MEDIA_BROWSE:
			hidLeftButton(false);
//			setTitleRightImage(drawable.button_ok);
			setTitleRightText("完成", this);
			mViewPager.setAdapter(new ImageSelectViewPagerAdapter(getApplicationContext(), null, selectArray, selectArray, new SelectChangedListener() {

				@Override
				public void onChanged(int count) {
					setRightPopText(count + "");
				}
			}));
			mViewPager.setCurrentItem(ext);
			mViewPager.setVisibility(View.VISIBLE);
			break;
		case STATE_MEDIA_PHOTO_PREVIEW:
			hidLeftButton(false);

			List<String> copyList = new ArrayList<String>();
			for (ImageFileVo imageFileVo : list) {
				copyList.add(imageFileVo.data);
			}
			mViewPager.setAdapter(new ImageSelectViewPagerAdapter(getApplicationContext(), null, copyList, selectArray, new SelectChangedListener() {

				@Override
				public void onChanged(int count) {
					setRightPopText(count + "");
				}

			}));
			mViewPager.setCurrentItem(ext);
			mViewPager.setVisibility(View.VISIBLE);
			setRightPopText(mAdapter.getSelectImages().size() + "");
			break;
		}
		currentState = state;
	}

	//private void initData() {
//		List<String> selectArray = getIntent().getStringArrayListExtra(INTENT_SELECT);
//
//		mImageDirectory = new ArrayList<ImageDirectoryVo>();
//
//		List<ImageFileVo> list = new ArrayList<ImageFileVo>();
//		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null, MediaStore.Images.Media.DATE_ADDED);
//		// Cursor cursor =
//		// getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI,
//		// THUMB_STORE_IMAGES, null, null, Thumbnails.DEFAULT_SORT_ORDER);
//		cursor.moveToFirst();
//		int counter = cursor.getCount();
//		for (int i = 0; i < counter; i++) {
//			ImageFileVo imageFileVo = new ImageFileVo();
//
//			imageFileVo.id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
//			imageFileVo.data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//			// LogUtil.outLogDetail("image filename:" + imageFileVo.data);
//
//			String dir = imageFileVo.data.substring(0, imageFileVo.data.lastIndexOf("/"));
//			// LogUtil.outLogDetail("image dir:" + dir);
//
//			ImageDirectoryVo directoryVo = new ImageDirectoryVo();
//			directoryVo.path = dir;
//			directoryVo.filePath = imageFileVo.data;
//
//			ImageDirectoryVo resultDirectoryVo = instertDirectory(selectArray, directoryVo);
//			if (resultDirectoryVo != null) {
//				ImageFileVo imageFileVo2 = new ImageFileVo();
//				imageFileVo2.data = directoryVo.filePath;
//
//				if (selectArray != null && selectArray.size() > 0) {
//					for (String imageUrl : selectArray) {
//						if (imageUrl.equals(imageFileVo2.data)) {
//							imageFileVo2.select = true;
//						}
//					}
//				}
//
//				resultDirectoryVo.files.add(imageFileVo2);
//			}
//
//			/*
//			 * imageFileVo.displayName =
//			 * cursor.getString(cursor.getColumnIndex(MediaStore
//			 * .Images.Media.DISPLAY_NAME));
//			 * 
//			 * imageFileVo.id =
//			 * cursor.getString(cursor.getColumnIndex(Thumbnails._ID));
//			 * imageFileVo.data =
//			 * cursor.getString(cursor.getColumnIndex(Thumbnails.DATA));
//			 * 
//			 * LogUtil.outLogDetail("===start====");
//			 * LogUtil.outLogDetail(imageFileVo.id =
//			 * cursor.getString(cursor.getColumnIndex
//			 * (MediaStore.Images.Media._ID)));
//			 * LogUtil.outLogDetail(imageFileVo.displayName =
//			 * cursor.getString(cursor
//			 * .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
//			 * LogUtil.outLogDetail("===end====");
//			 */
//
//			cursor.moveToNext();
//
//			list.add(imageFileVo);
//		}
//		cursor.close();
//
//		// updateUI(list);
//		// setTitleText("已选择" + mAdapter.getSelectImages().size() + "张");
//
//		mDirectoryListView = (ListView) findViewById(id.listview);
//		mDirectoryListView.setAdapter(new ImageFiledAdapter(mImageDirectory));
//
//		mDirectoryListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				MobclickAgent.onEvent(getApplicationContext(), "1129");
//				ImageDirectoryVo selectDirectoryVo = mImageDirectory.get(position);
//
//				showImageAdapterView(STATE_MEDIA_GRID, selectDirectoryVo.files, -1);
//			}
//		});
	//}

	private ImageDirectoryVo instertDirectory(List<String> selectArray, ImageDirectoryVo directoryVo) {
		for (int i = 0; i < mImageDirectory.size(); i++) {
			ImageDirectoryVo positionVo = mImageDirectory.get(i);
			if (positionVo.path.equals(directoryVo.path)) return positionVo;
		}
		directoryVo.files = new ArrayList<ImageFileVo>();
		ImageFileVo imageFileVo = new ImageFileVo();
		imageFileVo.data = directoryVo.filePath;

		if (selectArray != null && selectArray.size() > 0) {
			for (String imageUrl : selectArray) {
				if (imageUrl.equals(imageFileVo.data)) {
					imageFileVo.select = true;
				}
			}
		}

		directoryVo.files.add(imageFileVo);

		directoryVo.name = directoryVo.path.replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");

		mImageDirectory.add(directoryVo);
		return null;
	}

	/**
	 * 
	 *
	 * @param list
	 * @author LiuYuHang
	 * @date 2014年10月28日
	 */
	private void updateUI(final List<ImageFileVo> list) {
		ArrayList<String> selectArray = getIntent().getStringArrayListExtra(INTENT_SELECT);

		GridView imageGridView = (GridView) findViewById(id.gridview);

		mAdapter = new ImageFileAdapter(getApplicationContext(), list, selectArray);
		imageGridView.setAdapter(mAdapter);

		imageGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MobclickAgent.onEvent(getApplicationContext(), "1131");

				showImageAdapterView(STATE_MEDIA_PHOTO_PREVIEW, list, position);
				// mAdapter.setSelect(position);
			}
		});
	}

	/**
	 * 宫格的adapter
	 * 
	 * @author LiuYuHang
	 * @date 2014年11月26日
	 */
	private class ImageFileAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private List<ImageFileVo> mList;

		private ArrayList<String> mSelectBitmapsPath;

		public ImageFileAdapter(Context context, List<ImageFileVo> list, ArrayList<String> selectImageUrl) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			mSelectBitmapsPath = selectImageUrl;
			if (mSelectBitmapsPath == null) mSelectBitmapsPath = new ArrayList<String>();
		}

		/**
		 * 设置view是否被选中
		 *
		 * @param position
		 *
		 * @author LiuYuHang
		 * @date 2014年10月8日
		 */
		public void setSelect(int position) {
			if (mList.get(position).select) {
				mSelectBitmapsPath.remove(mList.get(position).data);
			} else {
				if (getSelectImages().size() >= mMaxSelectNumber) return;
				mSelectBitmapsPath.add(mList.get(position).data);
			}
			mList.get(position).select = !mList.get(position).select;
			notifyDataSetChanged();
		}

		public ArrayList<String> getSelectImages() {
			return mSelectBitmapsPath;
		}

		// public int getSelectCount() {
		// return mSelectBitmapsPath.size();
		// }

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = mInflater.inflate(layout.adapter_image_select, null);
				holder.imageView = (CropImageView) convertView.findViewById(id.imageview_image);
				holder.selectView = convertView.findViewById(id.view_select);

				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			ImageFileVo imageFileVo = (ImageFileVo) getItem(position);

			// ImageLoader.getInstance().displayImage(imageFileVo.data,
			// holder.imageView);
			// mBitmapUtils.display(holder.imageView, imageFileVo.data,
			// mCropImageListener);

//			ImageLoader.getInstance().displayImage("file://" + imageFileVo.data, holder.imageView, BitmapOptionsFactory.getImageOption(), new ImageLoaderCallbackFactory());

//			BitmapOptionsFactory.getInstance(getApplicationContext()).display(holder.imageView, imageFileVo.data);
			aQuery.id(holder.imageView).image(imageFileVo.data, true, true, GlobalUtil.displayMetrics.widthPixels/5, drawable.default_goods_img);
//			
//			new AQuery(holder.imageView).image(imageFileVo.data, true, true, getResources().getDisplayMetrics().widthPixels / 4, 0);
			
//			AQuery aq = new AQuery(holder.imageView);
//			aQuery.id(holder.imageView).image( imageFileVo.data, true, true, 200, drawable.goods_default_img);

			
//			new AQuery(holder.imageView).shouldDelay(position, convertView, arg2, imageFileVo.data);
			
			// mImageWorker.loadDiskBitmap(TAG, imageFileVo.data,
			// holder.imageView);
			// holder.selectView.setVisibility(imageFileVo.select ? View.VISIBLE
			// : View.GONE);
			holder.selectView.setBackgroundResource(imageFileVo.select ? drawable.icon_image_select : drawable.icon_image_normal);

			holder.selectView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					setSelect(position);
					setRightPopText(mAdapter.getSelectImages().size() + "");
					
					
				}
			});

			return convertView;
		}

		private class Holder {
			CropImageView imageView;
			View selectView;
		}

	}

	/**
	 * 目录的adapter
	 * 
	 * @author LiuYuHang
	 * @date 2014年11月26日
	 */
	private class ImageFiledAdapter extends BaseAdapter {
		private List<ImageDirectoryVo> list;

		public ImageFiledAdapter(List<ImageDirectoryVo> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(getApplicationContext(), layout.adapter_photo_folder, null);

				holder.imageView = (CropImageView) convertView.findViewById(id.image);
				holder.label = (TextView) convertView.findViewById(id.folderName);
				holder.countView = (TextView) convertView.findViewById(id.photosCount);

				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			ImageDirectoryVo directoryVo = (ImageDirectoryVo) getItem(position);

			// ImageLoader.getInstance().displayImage(directoryVo.files.get(0).data,
			// holder.imageView);
			// mImageWorker.loadDiskBitmap(TAG, directoryVo.files.get(0).data,
			// holder.imageView);
			// mBitmapUtils.display(holder.imageView,
			// directoryVo.files.get(0).data, mCropImageListener);

//			ImageLoader.getInstance().displayImage("file://" + directoryVo.files.get(0).data, holder.imageView, BitmapOptionsFactory.getImageOption(), new ImageLoaderCallbackFactory());

			aQuery.id(holder.imageView).image(directoryVo.files.get(0).data, true, true, GlobalUtil.displayMetrics.widthPixels/5, drawable.default_goods_img);
//			BitmapOptionsFactory.getInstance(getApplicationContext()).display(holder.imageView, directoryVo.files.get(0).data);
			
			holder.label.setText(directoryVo.name);
			holder.countView.setText(" (" + directoryVo.files.size() + ")");

			return convertView;
		}

		class Holder {
			CropImageView imageView;
			TextView label, countView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.left_imgv:
			if (getCurrentState() == STATE_MEDIA_GRID) {
				showImageAdapterView(STATE_MEDIA_DIRECTORY, null, -1);
			} else if (getCurrentState() == STATE_MEDIA_PHOTO_PREVIEW) {
				hidLeftButton(false);
				mViewPager.setVisibility(View.GONE);
				setRightPopText(mAdapter.getSelectImages().size() + "");
				mDirectoryListView.setVisibility(View.GONE);
				
//				setTitleRightImage(drawable.button_ok);
				setTitleRightText("完成", this);
				
				currentState = STATE_MEDIA_GRID;

				// updateUI(list);
				// setRightPopText(selectArray.size() + "");
			} else {
				MobclickAgent.onEvent(getApplicationContext(), "1130");
				finish();
			}
			break;
		case id.right_textview:
			if (getCurrentState() == STATE_MEDIA_GRID) {
				Intent intent = new Intent();
				intent.putStringArrayListExtra("images", mAdapter.getSelectImages());
				setResult(33, intent);
			} else if (getCurrentState() == STATE_MEDIA_BROWSE) {
				mViewPager = (ViewPager) findViewById(id.viewpager);
				ImageSelectViewPagerAdapter adapter = (ImageSelectViewPagerAdapter) mViewPager.getAdapter();

				Intent intent = new Intent();
				intent.putStringArrayListExtra("images", adapter.getSelectList());
				setResult(33, intent);
			} else if (getCurrentState() == STATE_MEDIA_PHOTO_PREVIEW) {
				mViewPager = (ViewPager) findViewById(id.viewpager);
				ImageSelectViewPagerAdapter adapter = (ImageSelectViewPagerAdapter) mViewPager.getAdapter();

				Intent intent = new Intent();
				intent.putStringArrayListExtra("images", adapter.getSelectList());
				setResult(33, intent);
			}
			finish();
			break;
		case id.right_imgv:
//			if (getCurrentState() == STATE_MEDIA_GRID) {
//				Intent intent = new Intent();
//				intent.putStringArrayListExtra("images", mAdapter.getSelectImages());
//				setResult(33, intent);
//			} else if (getCurrentState() == STATE_MEDIA_BROWSE) {
//				mViewPager = (ViewPager) findViewById(id.viewpager);
//				ImageSelectViewPagerAdapter adapter = (ImageSelectViewPagerAdapter) mViewPager.getAdapter();
//
//				Intent intent = new Intent();
//				intent.putStringArrayListExtra("images", adapter.getSelectList());
//				setResult(33, intent);
//			} else if (getCurrentState() == STATE_MEDIA_PHOTO_PREVIEW) {
//				mViewPager = (ViewPager) findViewById(id.viewpager);
//				ImageSelectViewPagerAdapter adapter = (ImageSelectViewPagerAdapter) mViewPager.getAdapter();
//
//				Intent intent = new Intent();
//				intent.putStringArrayListExtra("images", adapter.getSelectList());
//				setResult(33, intent);
//			}
//			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取当前页面的状态
	 *
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月28日
	 */
	private int getCurrentState() {
		return currentState;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		List<String> selectArray = getIntent().getStringArrayListExtra(INTENT_SELECT);

		mImageDirectory = new ArrayList<ImageDirectoryVo>();

		List<ImageFileVo> list = new ArrayList<ImageFileVo>();
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null, MediaStore.Images.Media.DATE_ADDED);
		// Cursor cursor =
		// getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI,
		// THUMB_STORE_IMAGES, null, null, Thumbnails.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int counter = cursor.getCount();
		for (int i = 0; i < counter; i++) {
			ImageFileVo imageFileVo = new ImageFileVo();

			imageFileVo.id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
			imageFileVo.data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			// LogUtil.outLogDetail("image filename:" + imageFileVo.data);

			String dir = imageFileVo.data.substring(0, imageFileVo.data.lastIndexOf("/"));
			// LogUtil.outLogDetail("image dir:" + dir);

			ImageDirectoryVo directoryVo = new ImageDirectoryVo();
			directoryVo.path = dir;
			directoryVo.filePath = imageFileVo.data;

			ImageDirectoryVo resultDirectoryVo = instertDirectory(selectArray, directoryVo);
			if (resultDirectoryVo != null) {
				ImageFileVo imageFileVo2 = new ImageFileVo();
				imageFileVo2.data = directoryVo.filePath;

				if (selectArray != null && selectArray.size() > 0) {
					for (String imageUrl : selectArray) {
						if (imageUrl.equals(imageFileVo2.data)) {
							imageFileVo2.select = true;
						}
					}
				}

				resultDirectoryVo.files.add(imageFileVo2);
			}

			/*
			 * imageFileVo.displayName =
			 * cursor.getString(cursor.getColumnIndex(MediaStore
			 * .Images.Media.DISPLAY_NAME));
			 * 
			 * imageFileVo.id =
			 * cursor.getString(cursor.getColumnIndex(Thumbnails._ID));
			 * imageFileVo.data =
			 * cursor.getString(cursor.getColumnIndex(Thumbnails.DATA));
			 * 
			 * LogUtil.outLogDetail("===start====");
			 * LogUtil.outLogDetail(imageFileVo.id =
			 * cursor.getString(cursor.getColumnIndex
			 * (MediaStore.Images.Media._ID)));
			 * LogUtil.outLogDetail(imageFileVo.displayName =
			 * cursor.getString(cursor
			 * .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
			 * LogUtil.outLogDetail("===end====");
			 */

			cursor.moveToNext();

			list.add(imageFileVo);
		}
		cursor.close();

		// updateUI(list);
		// setTitleText("已选择" + mAdapter.getSelectImages().size() + "张");

		mDirectoryListView = (ListView) findViewById(id.listview);
		mDirectoryListView.setAdapter(new ImageFiledAdapter(mImageDirectory));

		mDirectoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MobclickAgent.onEvent(getApplicationContext(), "1129");
				ImageDirectoryVo selectDirectoryVo = mImageDirectory.get(position);

				showImageAdapterView(STATE_MEDIA_GRID, selectDirectoryVo.files, -1);
			}
		});
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}

	// /**
	// * 判断是否在相册选的的位置
	// *
	// * @return
	// * @author LiuYuHang
	// * @date 2014年10月24日
	// */
	// private boolean isInDirectoryState() {
	// return mDirectoryListView.getVisibility() == View.VISIBLE;
	// }

}
