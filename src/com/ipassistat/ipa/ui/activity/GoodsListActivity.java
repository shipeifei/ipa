package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.GoodsListviewAdapter;
import com.ipassistat.ipa.adapter.GoodsSortListviewAdapter;
import com.ipassistat.ipa.adapter.GoodsTypeListviewAdapter;
import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.response.GoodsListResponse;
import com.ipassistat.ipa.bean.response.GoodsTypeResponse;
import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.ProductCategory;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.bean.response.entity.Sort;
import com.ipassistat.ipa.business.GoodsModule;
import com.ipassistat.ipa.business.HmlShoppingCartController;
import com.ipassistat.ipa.business.ShopingCartObserver;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.ScreenInfoUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;
/**
 * 全部商品列表
 * @author WanRui
 */
@SuppressLint("InflateParams")
public class GoodsListActivity extends BaseActivity implements OnClickListener,PaginationListener,ShopingCartObserver<GoodsEntity>{
	private TitleBar mTitleBar;//标题栏
	private LinearLayout mNoGoodsLinear;//没有商品时的默认布局
	private RelativeLayout mTypeLayout;//分类布局
	private TextView mTypeShow,mNumShow,mSortShow;//分类，数量，排序方式
	private PaginationListView mGoodsListview;//商品列表
	private PopupWindow mTypePopupWindow,mSortPopupWindow;//选择分类，选择排序方式PopupWindow对话框
	private View typePopupView,sortPopupView;//选择分类，选择排序方式PopupWindow视图
	private ListView mTypeListview,mSortListview;//分类列表，排序列表
	private ImageView mTypeShowClick;//选择分类箭头
	private List<SaleProduct> goodslist;//商品数据集合
	private List <ProductCategory> goodstypelist;//分类数据集合
	private List<Sort> goodssortlist;//排序数据集合
	private GoodsListviewAdapter adapter;//商品列表适配器
	private GoodsModule mGoodsModule;//商品模块
	private int pageresultmore;//分页结果中是否还有更多
	private int offset;//请求第几页数据
	private int limit;//每页加载几条
	private String category = null;//分类编号
	private String sort = null;//排序方式编号
	private Integer width;//屏幕宽度
	private Context mContext;	
	
	private GoodsTypeListviewAdapter mGoodsTypesAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);
		init();
		initPopuptWindow();
		register(); 
		initData();
		getGoodsTypeData(mContext);
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("1024");//统计页面
	    MobclickAgent.onResume(this);//统计时长
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("1024");//保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		HmlShoppingCartController.instance(getApplicationContext()).unRegisterListener(this);//反注册
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mContext = this;
		width = ScreenInfoUtil.getScreenWidth(GoodsListActivity.this);// 屏幕宽度
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);//标题栏
		mTitleBar.setTitleText("全部商品");//设置文字标题
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.button_goods_sort); //排序按钮图片
		Drawable shoppingcartdrawable = mContext.getResources().getDrawable(R.drawable.button_goodsdetail_shoppingcart); //购物车按钮图片
		mTitleBar.setImageBackGround(TitleBarButton.shareImgv,drawable);//设置排序按钮图片
		mTitleBar.setImageBackGround(TitleBarButton.rightImgv,shoppingcartdrawable);//设置购物车按钮图片
		mTitleBar.setVisibility(TitleBarButton.shareImgv, View.VISIBLE);// 设置排序按钮显示
		mTitleBar.setVisibility(TitleBarButton.shoppingcart_num_Textview, View.VISIBLE);// 设置购物车商品数量文本显示
		mNoGoodsLinear = (LinearLayout) findViewById(R.id.goods_null_linear);//没有商品时的布局

		mGoodsModule = new GoodsModule(this);//商品模块
		mTypeLayout = (RelativeLayout) findViewById(R.id.type_layout);//分类布局
		mTypeShow = (TextView) findViewById(R.id.type_show);//类型
		mNumShow = (TextView) findViewById(R.id.goods_num_show);//商品数量
		mSortShow = (TextView) findViewById(R.id.sort_show);//排序方式
		mTypeShowClick = (ImageView) findViewById(R.id.type_show_click);//选择类型箭头

		mGoodsListview = (PaginationListView) findViewById(R.id.goods_show);//商品列表
		mGoodsListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//点击商品列表每个Item跳转到商品详情页面
				/*SaleProduct product = adapter.getItem(position - 1);
				MobclickAgent.onEvent(mContext, "1205", product.getId());
				IntentUtil.startGoodsDetail(mContext, product.getId(),"0");*/
			}
		});
	}

	/**
	 * PopuptWindow初始化
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public void initPopuptWindow(){
		//选择分类PopupWindow
		typePopupView = getLayoutInflater().inflate(R.layout.item_popupwindow_type, null);
		mTypePopupWindow = new PopupWindow(typePopupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mTypePopupWindow.setTouchable(true);
		mTypePopupWindow.setOutsideTouchable(true);
		mTypePopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
		mTypeListview = (ListView) typePopupView.findViewById(R.id.type_listview);
		mTypeListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProductCategory goodstype = (ProductCategory) mTypeListview
						.getItemAtPosition(position);
				MobclickAgent.onEvent(mContext, "1146");
				String typename = goodstype.getName();//得到分类名称
				String typeid = goodstype.getId();//得到分类ID
				mTypeShow.setText(typename);//显示已选择的分类名称
				category = typeid;//将选择的分类ID赋值给请求数据时的ID
				onRefresh();//刷新数据
				mGoodsTypesAdapter.setSelectPosition(position);
				mGoodsListview.showLoadingView(true);
				mTypePopupWindow.dismiss();
			}
		});

		//选择排序方式PopupWindow
		sortPopupView = getLayoutInflater().inflate(R.layout.item_popupwindow_sort, null);
		mSortPopupWindow = new PopupWindow(sortPopupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mSortPopupWindow.setTouchable(true);
		mSortPopupWindow.setOutsideTouchable(true);
		mSortPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
		mSortListview = (ListView) sortPopupView.findViewById(R.id.sort_listview);
		mSortListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Sort goodssort = (Sort) mSortListview
						.getItemAtPosition(position);
				String sortname = goodssort.getName();//得到排序名称
				if(sortname.equals("销量")){
					MobclickAgent.onEvent(mContext, "1144");
				}
				String sortid = goodssort.getId();//得到排序ID
				mSortShow.setText(sortname+"排序");//显示已选择的排序名称
				sort = sortid;//将选择的排序ID赋值给请求数据时的ID
				onRefresh();//刷新数据
				mGoodsListview.showLoadingView(true);
				mSortPopupWindow.dismiss();	
			}
		});
	}

	/**
	 * 监听分类列表PopupWindow
	 */
	private void detectormTypePopupWindow(){
		mTypePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				//对话框隐藏，箭头显示向下
				mTypeShowClick.setBackgroundResource(R.drawable.img_goodstype_off);
			}
		});
		if (mTypePopupWindow.isShowing()) {
			//如果对话框是弹出状态，箭头显示向上
			mTypeShowClick.setBackgroundResource(R.drawable.img_goodstype_on);
		}else {
			//如果对话框是隐藏状态，箭头显示向下
			mTypeShowClick.setBackgroundResource(R.drawable.img_goodstype_off);
		}
	}

	/**
	 * 注册监听
	 */
	private void register() {
		mTitleBar.setButtonClickListener(TitleBarButton.leftImgv, this);
		mTitleBar.setButtonClickListener(TitleBarButton.rightImgv, this);
		mTitleBar.setButtonClickListener(TitleBarButton.shareImgv, this);
		mGoodsListview.setOnPaginationListener(this);
		mTypeLayout.setOnClickListener(this);
		HmlShoppingCartController.instance(getApplicationContext()).registerListener(this);//注册购物车监听
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			//返回按钮
//			MobclickAgent.onEvent(mContext, "1069");
			finish();
			break;
		case R.id.right_imgv:
			//购物车按钮
//			MobclickAgent.onEvent(mContext, "1173");
		/*	Intent intentshoppingcart = new Intent(mContext, ShoppingCartActivity.class);
			startActivity(intentshoppingcart);*/
			break;
		case R.id.share_imgv:
			//选择排序方式
//			MobclickAgent.onEvent(mContext, "1068");
			mSortPopupWindow.showAsDropDown(v);
			break;
		case R.id.type_layout:
			//选择分类
//			MobclickAgent.onEvent(mContext, "1067");
			mTypePopupWindow.showAsDropDown(v);
			detectormTypePopupWindow();
			break;
		default:
			break;
		}
	}

	/**
	 * 请求参数成功时回调方法
	 */
	@Override
	public void onMessageSucessCalledBack(String url,Object object) {
		super.onMessageSucessCalledBack(url,object);
		if(url.equals(ConfigInfo.API_GOODS_LIST)){
			//商品列表接口	
			GoodsListResponse goodslistObject = (GoodsListResponse) object;
			int page = (Integer) goodslistObject.getTag(); // 获得页数的标记
			goodslist = goodslistObject.getProducts();//得到商品列表
			PageResults pageresult = goodslistObject.getPaged();//得到分页结果	
			if(pageresult != null){
				pageresultmore = pageresult.getMore();//获得分页结果中是否还有更多的值
				int count = pageresult.getTotal();//得到商品总数
				mNumShow.setText("共"+count+"件");
			}
			if(goodslist != null && goodslist.size()>0){
				//有商品
				mGoodsListview.setVisibility(View.VISIBLE);//显示商品列表
				mNoGoodsLinear.setVisibility(View.GONE);//隐藏没有商品的布局
				if (adapter == null || page == 0) {
					adapter = new GoodsListviewAdapter(mContext, goodslist);
					mGoodsListview.setAdapter(adapter); // 填充数据
				}else {	
					adapter.loadNextPage(goodslist);
				}
				mGoodsListview.delegatePageCheck(goodslistObject.getPaged());
			}else{
				//无商品
				mGoodsListview.setVisibility(View.GONE);//隐藏商品列表
				mNoGoodsLinear.setVisibility(View.VISIBLE);//显示没有商品的布局
			}
			mGoodsListview.onRefreshComplete();//刷新结束			
		}else if(url.equals(ConfigInfo.API_GOODS_TYPE)){
			//商品分类接口
			GoodsTypeResponse goodstypeObject = (GoodsTypeResponse) object;
			goodstypelist = goodstypeObject.getCategories();//得到分类列表
			goodssortlist = goodstypeObject.getSort();//得到排序列表
			if(goodstypelist != null){		
				mGoodsTypesAdapter = new GoodsTypeListviewAdapter(mContext,goodstypelist);
				mTypeListview.setAdapter(mGoodsTypesAdapter);
			}
			if(goodssortlist != null){
				mSortListview.setAdapter(new GoodsSortListviewAdapter(mContext,goodssortlist));
			}
		}
	}
	/**
	 * 请求参数失败时回调方法
	 */
	@Override
	public void onMessageFailedCalledBack(String url,Object object) {
		super.onMessageFailedCalledBack(url, object);
		mGoodsListview.onRefreshComplete();//刷新结束	
		mGoodsListview.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
				getGoodsTypeData(mContext);
			}
		});
	}
	/**
	 * 无网
	 */
	@Override
	public void onNoNet() {
		super.onNoNet();
		mGoodsListview.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
				getGoodsTypeData(mContext);
			}
		});
	}
	/**
	 * 超时
	 */
	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mGoodsListview.noNet(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
				getGoodsTypeData(mContext);
			}
		});
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		offset = 0;//请求第一页数据
		limit = 20;//每页加载20条
		mGoodsModule.postGoodsListData(mContext, offset, limit, category, sort,width,true);
	}
	
	/**
	 * 下拉刷新
	 */
	public void initData() {
		offset = 0;//请求第一页数据
		limit = 20;//每页加载20条
		mGoodsModule.postGoodsListData(mContext, offset, limit, category, sort,width);
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onRequestNextPage(int page) {
		if(NetUtil.isNetworkConnected(mContext))
		{
		if(pageresultmore == ConfigInfo.PAGE_RESULT_MORE){					
			offset++;//请求页数加一
			limit = 20;//每页加载20条
			mGoodsModule.postGoodsListData(mContext, offset, limit, category, sort,width);
		}
		}
		else {
			ToastUtil.showToast(mContext, "请求服务器失败!");
			mGoodsListview.onRefreshComplete();//刷新结束	
		}
	}


	/**
	 * 得到商品分类接口数据
	 */
	private void getGoodsTypeData(Context context){
		mGoodsModule.postGoodsTypeData(context);
	}

	/**
	 * 购物车回调
	 */
	@Override
	public void onShopCartCountChanged(List<GoodsEntity> productArray,
			int totalCount, int changeCount) {
		if (totalCount > 99) {
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_shoppingcart_longnum);
			mTitleBar.setImageBackGround(TitleBarButton.shoppingcart_num_Textview, drawable);
			mTitleBar.setshoppingcartNumTextViewText("99+");// 购物车数量大于99显示99+
			
		} else if(totalCount > 9){
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_shoppingcart_longnum);
			mTitleBar.setImageBackGround(TitleBarButton.shoppingcart_num_Textview, drawable);
			mTitleBar.setshoppingcartNumTextViewText(totalCount + "");// 没有大于99显示当前数量
		}else {
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_shoppingcart_num);
			mTitleBar.setImageBackGround(TitleBarButton.shoppingcart_num_Textview, drawable);
			mTitleBar.setshoppingcartNumTextViewText(totalCount + "");// 没有大于99显示当前数量
		}	
	}

	@Override
	public void onWarning(String message) {
		
	}

	@Override
	public void onError(int code, String message) {
		
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
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
}
