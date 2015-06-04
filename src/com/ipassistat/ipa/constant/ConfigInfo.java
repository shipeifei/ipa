package com.ipassistat.ipa.constant;

/***
 * 
 * @author shipeifei
 *
 */
public class ConfigInfo {

	public static boolean DEBUG = true;// 是否是debug模式
	// public static final boolean DEBUG_URL = true;// 测试地址还是正式地址

	/***
	 * 当前经度
	 */
	public static final String CURRENT_LONGITUDE="current_longitude";
	/***
	 * 当前纬度
	 */
	public static final String CURRENT_LATITUDE="current_latitude";
	public static final String CURRENT_CITY="current_city";//用户所在的当前城市
	public static final String CURRENT_PROVINCE="current_province";//用户所在的当前省份
	public static final String CURRENT_DISTRICT="current_district";//用户所在的当前地区
	public static final String IS_SEND_CONTACTER="isSendContacter";//是否已经发送了联系人信息
	public static final String IS_FIRST_START = "isFirstStart";
	public static final String PHONE_NUM = "4000190860"; // 客服电话
	public static final String SKIN_EXEPTE = "skin_expt_falg";
	public static final String BANNER_CODE = "Col140814100001";
	public static final String SKIN_EXEPTE_NAME = "skin_expt_name_flag";
	public static final String VERSION_CODE="SI2013";
	/**
	 * 
	 * appkey
	 */
	public static final String APIKEY = "iznms"; // iznms

	public static final String SCHEME = "ipa://"; //跳转的scheme协议地址
	/**
	 * 默认缓存的根目录
	 */
	public static final String DEFAULT_DISK_DIR = "/ipa/";// 相对路径，非绝对路径
	public static final String DEFAULT_DISK_DIR_TEMP = DEFAULT_DISK_DIR + "temp";// 临时目录
	public static final String DEFAULT_DISK_DIR_PHOTO = DEFAULT_DISK_DIR + "photo";// 缓存目录，缓存图片
	public static final String DEFAULT_DISK_DIR_CACHE = DEFAULT_DISK_DIR + "cache";// 缓存目录，缓存图片

	// 返回值，1表示成功，其他表示失败
	public static final int RESULT_OK = 1;
	public static final int RESULT_SERVER_ERROR = 969905915;// 服务端报错code
	public static final int RESULT_LOW_STOCKS = 941901003;
	public static final int RESULT_POST_DELETE = 934205117;

	// 推送的
	// 地址 jumpType jumpPosition
	// 通知 0 0
	// 商品详情 1 商品编号
	// 首页 2 0
	// 试用中心列表 2 6 　
	// 限时抢购 2 7 　
	// 个人中心 2 8
	// 活动详情 4 活动详情页地址 　
	// 免邮试用商品详情 5 试用商品编号+英文逗号+活动结束时间
	// 帖子详情 6 帖子编号
	// 付邮试用商品详情 7 试用商品编号+英文逗号+活动结束时间
	// 限时抢购商品详情 8 商品编号

	public static final String PUSH_TYPE_GOODS_DETAIL = "1";// 商品详情
	public static final String PUSH_TYPE_TRIALCENTER_OR_TIMELIMITE = "2";// 首页，试用中心列表，限时抢购，个人中心
	public static final String PUSH_TYPE_ACTIVITY_CENTER = "4";// 活动详情
	public static final String PUSH_TYPE_TRIALCENTER_FREE_DETAIL = "5";// 免邮试用商品详情
	public static final String PUSH_TYPE_SISTERGROUP_POST_DETAIL = "6";// 帖子详情
	public static final String PUSH_TYPE_TRIALCENTER_POST_DETAIL = "7";// 付邮试用商品详情
	public static final String PUSH_TYPE_TIMELIMIT_DETAIL = "8";// 限时抢购商品详情

	public static final String TYPE_MESSAG_ACTION_COMMENT = "449746920001";// 评论帖子
	public static final String TYPE_MESSAG_ACTION_REPLY_COMMENT = "449746920004";// 回复评论
	public static final String TYPE_MESSAG_ACTION_UP_COMMENT = "449746920003";// 赞评论
	public static final String TYPE_MESSAG_ACTION_UP_POST = "449746920002";// 赞帖子

	public static final String POST_TAG_OFFICIAL = "449746760001";// 官方帖子
	public static final String POST_TAG_ISSESSENCE = "449746770001";// 精华帖子
	public static final String POST_TAG_HOT = "449746880001";// 热门帖子
	// 是否点赞
	public static final String ACTION_UP_OK = "449746870001";
	// 是否收藏
	public static final String ACTION_COLLECT_OK = "449746860001";

	public static final String ACTION_NO = "449746870002";

	// 分页结果返回值，还有更多返回1，没有更多返回0
	public static final int PAGE_RESULT_MORE = 1;

	public static final String API_GET_WELCOME_LIST = "com_cmall_newscenter_beauty_api_StratPageApi";

	// 获取系统消息，与我相关
	public static final String API_MESSAGE_SYSTEM = "com_cmall_newscenter_beauty_api_MessageSystemApi";
	public static final String API_MESSAGE_MINE = "com_cmall_newscenter_beauty_api_MessageAboutMeApi";
	public static final String API_MESSAGE_READ_STATE = "com_cmall_newscenter_beauty_api_MessageSystemNewApi";
	public static final String API_MESSAGE_READ_MODIFY = "com_cmall_newscenter_beauty_api_UpdateMessageStatusApi";

	// 帖子操作，点赞，收藏，追贴，发帖，评论列表
	public static final String API_POST_OPERATION_UP = "com_cmall_newscenter_beauty_api_PostsPraiseApi";
	public static final String API_POST_OPERATION_COLLECT = "com_cmall_newscenter_beauty_api_PostsCollectApi";
	public static final String API_POST_OPERATION_FOLLOW_ADD = "com_cmall_newscenter_beauty_api_PostsFollowAddApi";
	public static final String API_POST_OPERATION_SEND = "com_cmall_newscenter_beauty_api_PostsAddApi";
	public static final String API_POST_OPERATION_COMMENT_SEND = "com_cmall_newscenter_beauty_api_PostsReplyAddApi";
	public static final String API_POST_OPERATION_COMMENT_REPLY = "com_cmall_newscenter_beauty_api_PostCommentReplyAddApi";
	public static final String API_POST_OPERATION_COMMENTLIST = "com_cmall_newscenter_beauty_api_PostCommentListApi";
	public static final String API_POST_OPERATION_DETAIL = "com_cmall_newscenter_beauty_api_PostDetailListApi";
	public static final String API_POST_OPERATION_LIST_ALL = "com_cmall_newscenter_beauty_api_PostAllListApi";
	public static final String API_POST_OPERATION_LIST_OTHER = "com_cmall_newscenter_beauty_api_PostListApi";
	public static final String API_POST_OPERATION_SEND_TAGS = "com_cmall_newscenter_beauty_api_PostLabelListApi";

	// 帖子 - 评论操作 - 点赞，回复
	public static final String API_POST_OPERATION_COMMENT_UP = "com_cmall_newscenter_beauty_api_CommentPraiseApi";
	// public static final String API_POST_OPERATION_COMMENT_REPLY =
	// "com_cmall_newscenter_beauty_api_PostListApi";

	public static final String API_POST_OPERATION_REPORT = "com_cmall_newscenter_beauty_api_BeautyReportApi";

	/**
	 * 首页的广告条list
	 */
	public static final String API_POST_HOME_BANNER_LIST = "com_cmall_newscenter_beauty_api_AdvertiseListApi";

	// 商品操作
	public static final String API_PRODUCT_SEARCH = "com_cmall_newscenter_beauty_api_SearchProductsListApi";

	/**
	 * 设置为默认地址
	 */
	public static final String API_POST_SET_DEFAULT_ADDRESS = "com_cmall_newscenter_beauty_api_AddressSelectApi";
	/**
	 * 护肤类型
	 */
	public static final String API_SKINTYPE = "com_cmall_newscenter_beauty_api_GetSkinTypeApi";
	/**
	 * 护肤需求
	 */
	public static final String API_SKINEXPLAIN = "com_cmall_newscenter_beauty_api_GetSkinHopefulApi";
	// 性别，皮肤类型，期望是以String形式给的数据
	/** 默认为男 */
	public static final long SEX = 4497465100010002L;
	public static final long SKINTYPE = 449746650001L;
	public static final long HOPEFUL = 449746660001L;

	public static final String PERSONALINFOWHICH = "com.ichsy.mboss.personal.which";
	public static final String PERSONALINFOCHOOSE = "com.ichsy.mboss.personal.choose";
	// 皮肤类型
	public static final String PERSONALINFOSKINNAME = "com.ichsy.mboss.personal.skinname";
	// 用户头像
	public static final String PERSONALINFOUSERPHOTO = "com.ichsy.mboss.personal.userphoto";
	public static final String PERSONALINFOSKINEXPLAINSELECT = "com.ichsy.mboss.personal.skinexplainselect";
	public static final String PERSONALINFOTITLE = "com.ichsy.mboss.personal.title";
	public static final String PERSONALINFOORGIN = "com.ichsy.mboss.personal.orgin";
	// 选择标志
	public static final String PERSONALINFOFLAG = "com.ichsy.mboss.personal.select";
	// 护肤需求
	public static final String PERSONALINFOSKINEXPLAIN = "com.ichsy.mboss.personal.skinexplain";
	// 全部商品
	public static final String API_GOODS_LIST = "com_cmall_newscenter_beauty_api_ProductsListApi";// 商品列表
	public static final String API_GOODS_TYPE = "com_cmall_newscenter_beauty_api_ProductCategoryApi";// 商品分类
	// 商品详情
	public static final String API_GOODS_DETAIL = "com_cmall_newscenter_beauty_api_ProductInfoOneApi";// 商品详情
	public static final String API_GOODS_PRICE = "com_cmall_newscenter_beauty_api_ProductPriceApi";// 商品价格
	public static final String API_GOODS_LABEL = "com_cmall_newscenter_beauty_api_ProductCommentlabelApi";// 印象标签
	public static final String API_GOODS_COMMENT = "com_cmall_newscenter_beauty_api_ProductCommentListApi";// 商品评论列表
	public static final String API_GOODS_COLLECTION = "com_cmall_newscenter_beauty_api_ProductFavApi";// 商品收藏
	// 个人中心
	public static final String API_USERCENTER = "com_cmall_newscenter_beauty_api_PersonCenter";
	// 个人资料获取
	public static final String API_USERINFO_GET = "com_cmall_newscenter_beauty_api_GetPersonInformation";
	// 个人资料修改
	public static final String API_USERINFO_UPDATA = "com_cmall_newscenter_beauty_api_UpdatePersonInformation";
	// 个人中心 -> 我的收藏
	public static final String API_MYCOLLECTION = "com_cmall_newscenter_beauty_api_UserFavListApi";
	// 限时抢购
	public static final String API_TIMESCAREDBUY = "com_cmall_newscenter_beauty_api_TimedScareBuyingListApi";
	// 限时抢购详情
	public static final String API_TIMESCAREDBUYGOODINFO = "com_cmall_newscenter_beauty_api_TimedScareBuyingDetailListApi";
	// 试用中心
	public static final String API_TRYOUTCENTER = "com_cmall_newscenter_beauty_api_TryOutCenterApi";
	// 试用商品详情
	public static final String API_TRYOUTGOODINFO = "com_cmall_newscenter_beauty_api_TryOutGoodInfoApi";
	// 付邮试用详情
	public static final String API_PAYTRYOUTINFO = "com_cmall_newscenter_beauty_api_PayTryOutInfoApi";
	// 免费试用详情
	public static final String API_FREETRYOUTINFO = "com_cmall_newscenter_beauty_api_FreeTryOutInfoApi";
	// shared status request
	public static final String API_PRODUCTSHARESTATUS = "com_cmall_newscenter_beauty_api_ProductShareStatusApi";
	// post shared status
	public static final String API_PRODUCTSHARE = "com_cmall_newscenter_beauty_api_ProductShareApi";
	// 官方活动
	public static final String API_OFFICIAL_LIST = "com_cmall_newscenter_beauty_api_OfficialActivityApi";

	// 印象标签
	public static final String API_COMMENT_LABEL = "com_cmall_newscenter_beauty_api_ProductCommentlabelApi";

	// 添加商品评论
	public static final String API_GOODS_COMMENT_ADD = "com_cmall_newscenter_beauty_api_ProductCommentAddApi";

	// 获取订单列表
	// public static final String API_ORDER_LIST =
	// "com_cmall_familyhas_api_ApiOrderList";
	// public static final String API_ORDER_LIST =
	// "com_cmall_newscenter_api_ApiOrderList";
	public static final String API_ORDER_LIST = "com_cmall_newscenter_beauty_api_ApiOrderList";

	// 用户注销登录
	public static final String API_LOGOUT = "com_cmall_membercenter_api_UserLogout";

	/**
	 * 获取首页内容列表
	 */
	public static final String API_POP_POST_LIST = "com_cmall_newscenter_young_api_HomeChannelListApi";

	// 获取收货地址接口
	public static final String API_ADDR_REC_NAME = "com_cmall_newscenter_beauty_api_GetAddress";

	// 提交收货地址接口
	public static final String API_ORDER_SUBMIT = "com_cmall_familyhas_api_APiCreateOrderForBeautiful";

	// 我的试用
	public static final String API_MYTRYOUTCENTERAPI = "com_cmall_newscenter_beauty_api_MyTryOutCenterApi";
	/**
	 * 忘记密码
	 */
	public static final String API_FORGET_PWD = "com_cmall_membercenter_api_ForgetPassword";

	/**
	 * 用户-修改密码
	 */
	public static final String API_MODIFY_PWD = "com_cmall_membercenter_api_ChangePassword";

	/**
	 * 申请试用——免费试用
	 */
	public static final String API_TRYOUTAPPLYAPI = "com_cmall_newscenter_beauty_api_TryOutApplyApi";

	/**
	 * 免费试用的状态-未申请
	 */
	public static final String TRYOUT_FREE_STATUS_NOTAPPLY = "449746890001";
	/**
	 * 免费试用的状态-已申请
	 */
	public static final String TRYOUT_FREE_STATUS_ALEADYAPPLY = "449746890002";
	/**
	 * 免费试用的状态-申请通过
	 */
	public static final String TRYOUT_FREE_STATUS_APPLYSUCCESS = "449746890003";
	/**
	 * 免费试用的状态-已结束
	 */
	public static final String TRYOUT_FREE_STATUS_END = "449746890004";
	/**
	 * 免费试用的状态-已发货
	 */
	public static final String TRYOUT_FREE_STATUS_ALREADYGET = "449746890005";

	/***
	 * 付邮试用的状态-未申请
	 */
	public static final String TRYOUT_POSTAGE_STATUS_NOTAPPLY = "449746890001"; //
	/**
	 * 付邮试用的状态-已结束
	 */
	public static final String TRYOUT_POSTAGE_STATUS_END = "449746890004";
	/**
	 * 付邮试用的状态-已发货
	 */
	public static final String TRYOUT_POSTAGE_STATUS_ALREADYGET = "449746890005";
	/**
	 * 付邮试用的状态-已试用
	 */
	public static final String TRYOUT_POSTAGE_STATUS_ALREADAPPLY = "449746890006";
	/**
	 * 删除全部收藏商品
	 */
	public static final String API_FAVDELETEAPI = "com_cmall_newscenter_beauty_api_FavDeleteApi";

	// 购物车同步
	public static final String API_SHOPPINGCART = "com_cmall_familyhas_api_APiAddSkuToShopCart";
	// 预结算
	public static final String API_ORDER_SETTLEMENT = "com_cmall_newscenter_beauty_api_OrderSettlementListApi";

	/**
	 * 验证码判断
	 */
	public static final String API_VERIFY_CODE_CHECK = "com_cmall_newscenter_beauty_api_VerifyCodeIsTrueApi";
	/**
	 * 获得验证码
	 */
	public static final String API_GET_VERIFY_CODE = "com_cmall_systemcenter_api_MsgSend";
	/**
	 * check update
	 */
	public static final String API_UPDATE_CHECK = "com_cmall_newscenter_api_VersionAppMsg";
	/**
	 * 付邮试用的商品类型
	 */
	public static final String TYPEPAYTRYOUT = "2";

	/**
	 * 0
	 */
	public static final String ZERO = "0";

	/**
	 * 订单详情
	 */
	// public static final String
	// API_ORDER_DETAIL="com_cmall_familyhas_api_ApiOrderDetails";
	public static final String API_ORDER_DETAIL = "com_cmall_newscenter_api_ApiOrderDetails";

	/**
	 * 删除订单
	 */
	public static final String API_ORDER_REMOVE = "com_cmall_newscenter_beauty_api_DeleteOrderApi";

	/**
	 * 确认收货
	 */
	public static final String API_GOODS_RECEIVE_CONFIRM = "com_cmall_newscenter_beauty_api_OperateOrderApi";

	public static final String TRYPROCESSING = "449746890002";
	public static final String TRYSUCCESS = "449746890003";
	public static final String TRYFAIL = "449746890004";

	// 获取收货地址列表
	public static final String addrRecApiName = "com_cmall_newscenter_beauty_api_GetAddress";

	// 确认订单接口
	public static final String API_ORDER_CONFIRM = "com_cmall_familyhas_api_APiOrderConfirm";
	/**
	 * 短信推荐 处所使用的服务器地址
	 */
	public static final String MSG_SHARE_SERVICE = "http://app116.ichsy.com:8181";
	/**
	 * 发送短信推荐人
	 */
	public static final String MSG_SHARE_SENDSHARELIST = "com_cmall_groupcenter_recommend_api_ApiRecommendContacts";
	/**
	 * 获取推荐人
	 */
	public static final String GET_SHARED_CONTACTS = "com_cmall_groupcenter_recommend_api_ApiGetRecommendLog";

	public static final String API_ADDRESS_SAVE = "com_cmall_newscenter_beauty_api_AddAddress"; // 保存收货地址

	public static final String API_ADDRESS_EDIT = "com_cmall_newscenter_beauty_api_AddressUpdateApi"; // 保存修改收货地址接口

	public static final String API_ADDRESS_DEL = "com_cmall_newscenter_beauty_api_AddressDeleteApi"; // 删除收货地址接口

	public static final String API_LOGIN = "com_cmall_membercenter_api_UserLogin"; // 登录接口

	/**
	 * 娱乐模块接口
	 */
	public static final String API_VIDEO_CHANNEL = "com_cmall_newscenter_young_api_VideoChannelApi"; // 频道名称接口

	public static final String API_VIDEO_LIST = "com_cmall_newscenter_young_api_VideoListApi"; // 视频列表接口
	
	
	
	/***
	 * 通讯录模块接口
	 * 
	 */
	public static final String POST_CONTACTER_INFO="";//提交联系人信息到服务器
}
