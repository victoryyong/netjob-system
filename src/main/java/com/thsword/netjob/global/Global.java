package com.thsword.netjob.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.pojo.Setting;
import com.thsword.netjob.util.AmapUtil;
import com.thsword.utils.file.FileUtil;
public class Global{
	private static Map<String, Object> settings=new HashMap<String, Object>();
	
	private static JSONObject map=null;
	//token常量
	/**ADMIN session过期时间*/
	public static final long JWT_ADMIN_SESSION_TIME_OUT = 2*60*60*1000; 
	/**APP session过期时间*/
	public static final long JWT_APP_SESSION_TIME_OUT = 7*24*60*60*1000; 
	/** token宽限时间*/
	public static final long JWT_EXTEND_TIME = 5*60*1000;
    /** token过期时间*/
    public static final long JWT_ADMIN_EXPIRESS_TIME=2*60*60*1000;
    /** token过期时间*/
    public static final long JWT_APP_EXPIRESS_TIME=2*60*60*1000;
    /** 用户信息redis过期时间*/
    public static final int USER_INFO_REDIS_TIMEOUT = 24*60*60;;
	/** token秘钥*/
    public static final String JWT_SECRET_ADMIN_KEY="SECRET.ADMIN.KEY";
    /** token秘钥*/
    public static final String JWT_SECRET_APP_KEY="SECRET.APP.KEY";
    /** token签发者*/
    public static final String JWT_ISSUERT="NETJOB";
    /**后台token*/
    public static final String JWT_SUBJECT_ADMIN="admin";
    /**app token*/
    public static final String JWT_SUBJECT_APP="app";
    
    
    //后台域名
    public static final String SYSTEM_ADMIN_DOMAIN="SYSTEM.ADMIN.DOMAIN";
    
    //角色、权限类型
    /** 系统级别*/
    public static final int SYS_ADMIN_ROLE_PERM_TYPE_1=1;
    /** 代理人级别*/
    public static final int SYS_ADMIN_ROLE_PERM_TYPE_2=1;
    
    //七牛常量
    /** 七牛accessKey */
    public static final String QINIU_UPLOAD_ACCESS_KEY="QINIU.ACCESS.KEY";
    /** 七牛bocket名称 */
    public static final String QINIU_UPLOAD_BOCKET_NAME="netjob";
    
    //高德地图常量
    public static final String AMAP_ACCESS_KEY="AMAP.ACCESS.KEY";
    public static final String AMAP_COUNTRY_NAME="中国";
    public static final String AMAP_HTTP_URL="http://restapi.amap.com/v3/config/district";
    
    //极光IM
    /** 极光Key */
    public static final String JPUSH_ACCESS_KEY="JPUSH.ACCESS.KEY";
    /** 极光SECRET */
    public static final String JPUSH_ACCESS_SECRET="JPUSH.ACCESS.SECRET";
    
    //聚合网认证
    /** 证件识别*/
    public static final String JUHE_PERSONCARD_URL = "http://v.juhe.cn/certificates/query.php";
    public static final String JUHE_ACCESS_PERSONCARD_KEY = "JUHE.ACCESS.PERSONCARD.KEY";
    
    //网约业务配置
    //现金-网币 比例
  	public static final String SYS_MEMBER_MONEY_COIN_RATE="NETJOB.MONEYTOCOIN.RATE";
  	public static final String SYS_MEMBER_SERVE_TIP_RATE="NETJOB.SERVE.TIP.RATE";
    
    //微信支付配置
    /**微信支付appId*/
    public static final String WX_PAY_APPID="WX.PAY.APPID";
    /**微信支付商户号ID*/
    public static final String WX_PAY_MCHID="WX.PAY.MCHID";
    /**微信支付密钥*/
    public static final String WX_PAY_SECRET_KEY="WX.PAY.SECRET.KEY";
    /** 微信支付回调地址*/
    public static final String WX_PAY_CALLBACK="http://wy-168.com/netjob/app/rechangeWx/callback";
    /** 是否适用沙箱环境*/
    public static String WX_PAY_USESANDBOX="WX.PAY.USESANDBOX";
    
    /**微信支付appId*/
    public static final String ALIPAY_PAY_APPID="ALIPAY.PAY.APPID";
    /**微信支付商户号ID*/
    public static final String ALIPAY_PAY_UID="ALIPAY.PAY.UID";
    /**微信支付密钥*/
    public static final String ALIPAY_PAY_PRIVATE_KEY="ALIPAY.PAY.PRIVATE.KEY";
    /**微信支付公钥*/
    public static final String ALIPAY_PAY_PUBLIC_KEY="ALIPAY.PAY.PUBLIC.KEY";
    /** 微信支付回调地址*/
    public static final String ALIPAY_PAY_CALLBACK="http://wy-168.com/netjob/app/rechangeAlipay/callback";
    /** 是否适用沙箱环境*/
    public static String ALIPAY_PAY_USESANDBOX="ALIPAY.PAY.USESANDBOX";
    
    //网约信息
    public static final String NETJOB_PAY_APP_NAMA="网约";
    //网约充值信息
    public static final String NETJOB_PAY_RECHARGE_NAMA="网约-账户充值";
    //网约保证金充值信息
    public static final String NETJOB_PAY_RECHARGE_PROMISE_NAMA="网约-保证金充值";
    
    //权限类型常量 
    /** 权限类型 一级菜单 */
    public static final Integer SYS_PERMISSION_TYPE_MENU1=1;
    /** 权限类型 二级菜单 */
    public static final Integer SYS_PERMISSION_TYPE_MENU2=2; 
    /** 权限类型 操作权限 */
    public static final Integer SYS_PERMISSION_TYPE_OPRT=3;
    
    //系统文件路径
	public static final String SYS_FILE_ROOT_PATH = "/usr/netjob";
	/** 文件系统路径*/
    public static final String SYS_FILE_PATH = "file";
    /** 会员文件路径*/
	public static final String SYS_FILE_MANAGER_PATH = "member";
	/** 广告文件路径*/
	public static final String SYS_FILE_BANNER_PATH = "banner";
	/** 菜单文件路径*/
	public static final String SYS_FILE_MENU_PATH = "menu";

	
	//会员类型
	/** 会员类型 微信注册 */
	public static final String SYS_MEMBER_TYPE_WX="wx";
	/** 会员类型 QQ注册 */
	public static final String SYS_MEMBER_TYPE_QQ="qq";
	/** 会员类型 手机注册 */
	public static final String SYS_MEMBER_TYPE_PHONE="phone";
	
	/** 会员状态-激活 */
	public static final Integer SYS_MEMBER_STATUS_1=1;
	/** 会员类型-冻结 */
	public static final Integer SYS_MEMBER_STATUS_2=2;
	
	//广告类型
	/** 广告类型 图片 */
	public static final String SYS_BANNER_TYPE_IMAGE="image";
	/** 广告类型 视频 */
	public static final String SYS_BANNER_TYPE_VIDEO="video";
	/** 广告类型 链接 */
	public static final String SYS_BANNER_TYPE_LINK="link";
	
	//菜单等级
	/** 菜单等级 1 */
	public static final int SYS_MENU_LEVEL_TYPE_1=1;
	/** 菜单等级 2 */
	public static final int SYS_MENU_LEVEL_TYPE_2=2;
	
	//服务、需求常量
	/** 服务方式（1线上）*/
	public static final int SYS_MEMBER_WORK_TYPE_1=1;
	/** 服务方式（2线下）*/
	public static final int SYS_MEMBER_WORK_TYPE_2=2;
	/** 付款方式（1线上）*/
	public static final int SYS_MEMBER_PAY_TYPE_1=1;
	/** 付款方式（2线下）*/
	public static final int SYS_MEMBER_PAY_TYPE_2=2;
	/** 定价方式（1一口价）*/
	public static final int SYS_MEMBER_PRICE_TYPE_1=1;
	/** 定价方式（2议价）*/
	public static final int SYS_MEMBER_PRICE_TYPE_2=2;
	
	//订单常量
	/**状态(1-待付款 2-已付款 3-待接单  4-已接单  5-已拒单 6-退款中 7-已退款 8-拒绝退款 9-待签收 10-已签收)*/
	
	/**买家-待付款*/
	public static final int SYS_ORDER_BUYER_STATUS_PAYING=1;
	/**买家-已付款*/
	public static final int SYS_ORDER_BUYER_STATUS_PAYED=2;
	/**买家-已接单*/
	public static final int SYS_ORDER_BUYER_STATUS_ACCEPTED=3;
	/**买家-已退款（拒单）*/
	public static final int SYS_ORDER_BUYER_STATUS_CANCEL_REFUNDED=4;
	/**买家-待签收*/
	public static final int SYS_ORDER_BUYER_STATUS_SIGNING=5;
	/**买家-已签收*/
	public static final int SYS_ORDER_BUYER_STATUS_SIGNED=6;
	/**买家-待评论*/
	public static final int SYS_ORDER_BUYER_STATUS_COMMENTING=7;
	/**买家-已关闭*/
	public static final int SYS_ORDER_BUYER_STATUS_CLOSED=8;
	/**买家-申请退款*/
	public static final int SYS_ORDER_BUYER_STATUS_APPLYING=9;
	/**买家-已退款（申请）*/
	public static final int SYS_ORDER_BUYER_STATUS_APPLY_REFUND=10;
	/**买家-待维权*/
	public static final int SYS_ORDER_BUYER_STATUS_RIGHTING=11;
	/**买家-已维权*/
	public static final int SYS_ORDER_BUYER_STATUS_RIGHTED=12;

	/**商家-待接单*/
	public static final int SYS_ORDER_SELLER_STATUS_ACCEPTING=1;
	/**商家-已接单*/
	public static final int SYS_ORDER_SELLER_STATUS_ACCEPTED=2;
	/**商家-已拒单*/
	public static final int SYS_ORDER_SELLER_STATUS_CANCEL=3;
	/**商家-已完成*/
	public static final int SYS_ORDER_SELLER_STATUS_FINISH=4;
	/**商家-拒绝退款*/
	public static final int SYS_ORDER_SELLER_STATUS_APPLY_REJECT=5;
	/**商家-已退款*/
	public static final int SYS_ORDER_SELLER_STATUS_APPLY_REFUND=6;
	/**商家-待举证*/
	public static final int SYS_ORDER_SELLER_STATUS_APPROVING=7;
	/**商家-已举证*/
	public static final int SYS_ORDER_SELLER_STATUS_APPROVED=8;
	/**商家-已关闭*/
	public static final int SYS_ORDER_SELLER_STATUS_CLOSED=9;
	
	//服务类型
	/** 服务*/
	public static final int SYS_MEMBER_SERVE_TYPE_1=1;
	/** 需求*/
	public static final int SYS_MEMBER_SERVE_TYPE_2=2;
	
	//动态文件类型
	/** 图片*/
	public static final int SYS_MEMBER_ACTIVE_FILE_TYPE_1=1;
	/** 视频*/
	public static final int SYS_MEMBER_ACTIVE_FILE_TYPE_2=2;
	
	/** 身份认证*/
	public static final int SYS_MEMBER_PERSON_AUTH_1=1;
	/** 其他*/
	public static final int SYS_MEMBER_PERSON_AUTH_2=2;
	
	/** 营业执照*/
	public static final int SYS_MEMBER_COMPANY_AUTH_1=1;
	/** 委托书*/
	public static final int SYS_MEMBER_COMPANY_AUTH_2=2;
	/** 其他*/
	public static final int SYS_MEMBER_COMPANY_AUTH_3=3;
	
	public static final String SYS_MEMBER_AUTH_PUBLIC_0="0";
	public static final String SYS_MEMBER_AUTH_PUBLIC_1="1";
	
	//热播视频来源
	/** 个人动态*/
	public static final int SYS_MEMBER_ACTIVE_RESOURCE_1=1;
	/** 品牌秀*/
	public static final int SYS_MEMBER_ACTIVE_RESOURCE_2=2;
	/** 发布*/
	public static final int SYS_MEMBER_ACTIVE_RESOURCE_3=3;
	
	//品牌秀来源
	/** 原创*/
	public static final int SYS_MEMBER_BRANDSHOW_RESOURCE_1=1;
	/** 转载*/
	public static final int SYS_MEMBER_BRANDSHOW_RESOURCE_2=2;
	
	//头条类型
	/** 注册*/
	public static final int SYS_MEMBER_NEWS_TYPE_1=1;
	/** 充值*/
	public static final int SYS_MEMBER_NEWS_TYPE_2=2;
	
	
	//账户类型
	/** 现金账户*/
	public static final int SYS_MEMBER_ACCOUNT_TYPE_1=1;
	/** 网币账户*/
	public static final int SYS_MEMBER_ACCOUNT_TYPE_2=2;
	
	//中央账户ID
	public static final String SYS_NETJOB_CENTER_ACCOUNT_ID="netjob_center_account_id";
	
	//账户充值方式
	/** 微信*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_WAY_1=1;
	/** 支付宝*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_WAY_2=2;
	/** 银行账户*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_WAY_3=3;
	/** 现金账户*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_WAY_4=4;
	/** 网币账户*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_WAY_5=5;
	//是否收入（1-是 2-否）
	public static final int SYS_MEMBER_ACCOUNT_ISIN_1=1;
	public static final int SYS_MEMBER_ACCOUNT_ISIN_2=2;
	
	//账户充值状态（1-待支付 2-已支付 3-支付超时 4-已关闭）
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1=1;
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2=2;
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_3=3;
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4=4;
	
	
	//账户充值类型
	/** 现金账户*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1=1;
	/** 网币*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2=2;
	/** 保证金*/
	public static final int SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_3=3;
	
	//现金账户记录类型
	/** 充值*/
	public static final int SYS_MEMBER_ACCOUNT_RECORD_TYPE_1=1;
	/** 支付订单*/
	public static final int SYS_MEMBER_ACCOUNT_RECORD_TYPE_2=2;
	/** 退款*/
	public static final int SYS_MEMBER_ACCOUNT_RECORD_TYPE_3=3;
	/** 账户提现*/
	public static final int SYS_MEMBER_ACCOUNT_RECORD_TYPE_4=4;
	/** 兑换网币*/
	public static final int SYS_MEMBER_ACCOUNT_RECORD_TYPE_5=5;
	/** 网币提现*/
	public static final int SYS_MEMBER_ACCOUNT_RECORD_TYPE_6=6;
	
	
	//网币账户记录类型
	/** 充值*/
	public static final int SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_1=1;
	/** 账户兑换*/
	public static final int SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_2=2;
	/** 网币提现*/
	public static final int SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_3=3;
	/** 打赏*/
	public static final int SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_4=4;
	
	
	//中央账户状态
	/** 已收入**/
	public static final int SYS_MEMBER_ACCOUNTCENTER_STATUS_1=1;
	/** 已交付**/
	public static final int SYS_MEMBER_ACCOUNTCENTER_STATUS_2=2;
	/** 已退回**/
	public static final int SYS_MEMBER_ACCOUNTCENTER_STATUS_3=3;
	
	//售后类型
	/** 退款*/
	public static final int SYS_MEMBER_ORDER_REFUND=1;
	/** 维权*/
	public static final int SYS_MEMBER_ORDER_RIGHT=2;
	/** 举证*/
	public static final int SYS_MEMBER_ORDER_APPROVE=3;
	
	//是否默认地址
	public static final int SYS_MEMBER_ADDRESS_IS_DEFAULT_YES=1;
	
	public static final int SYS_MEMBER_ADDRESS_IS_DEFAULT_NO=0;
	
	//审核类型
	public static final int SYS_AUTH_TYPE_1=1;//需求
	public static final int SYS_AUTH_TYPE_2=2;//服务
	public static final int SYS_AUTH_TYPE_3=3;//品牌秀
	public static final int SYS_AUTH_TYPE_4=4;//个人审核
	public static final int SYS_AUTH_TYPE_5=5;//企业审核
	
	//审核状态
	public static final int SYS_AUTH_STATUS_1=1;//未审核
	public static final int SYS_AUTH_STATUS_2=2;//审核通过
	public static final int SYS_AUTH_STATUS_3=3;//审核未通
	
	//用户级别
	public static final int SYS_USER_LEVEL_1=1;//系统级别
	public static final int SYS_USER_LEVEL_2=2;//代理级别
	
	//点赞类型
	public static final int SYS_AGREE_TYPE_1=1;//会员点赞
	public static final int SYS_AGREE_TYPE_2=2;//品牌秀
	public static final int SYS_AGREE_TYPE_3=3;//需求
	public static final int SYS_AGREE_TYPE_4=4;//动态
	public static final int SYS_AGREE_TYPE_5=5;//媒体（视频、图片）
	
	//收藏类型
	public static final int SYS_COLLECT_TYPE_1=1;//会员收藏
	public static final int SYS_COLLECT_TYPE_2=2;//需求收藏
	public static final int SYS_COLLECT_TYPE_3=3;//服务收藏
	
	//数据字典
	/** 年龄范围*/
	public static final String SYS_DICT_AGE_TYPE="dict.age.range";
	/** 服务类型*/
	public static final String SYS_DICT_SERVE_TYPE="dict.serve.type";
	/** 性别类型*/
	public static final String SYS_DICT_GENDER_TYPE="dict.gender.type";
	/** 用户类型*/
	public static final String SYS_DICT_MEMBER_TYPE="dict.member.type";
	/** 距离范围*/
	public static final String SYS_DICT_DISTANCE_TYPE="dict.distance.range";
	/** 技能等级*/
	public static final String SYS_DICT_SKILL_TYPE="dict.skill.degree";
	/** 诚信等级*/
	public static final String SYS_DICT_CREDIT_TYPE="dict.credit.degree";
	/** 广告类型*/
	public static final String SYS_DICT_BANNER_TYPE = "dict.banner.type";
	/** 菜单级别*/
	public static final String SYS_DICT_MENU_LEVEL = "dict.menu.level";
	/** 服务方式*/
	public static final String SYS_DICT_SERVE_WAY = "dict.serve.way";
	/** 服务付款方式*/
	public static final String SYS_DICT_SERVE_PAY_WAY = "dict.serve.pay.way";
	/** 服务定价方式*/
	public static final String SYS_DICT_SERVE_PRICE_WAY = "dict.serve.price.way";
	/** 热播视频来源*/
	public static final String SYS_DICT_VIDEO_RESOURCE = "dict.video.resource";
	/** 品牌秀来源*/
	public static final String SYS_DICT_BRAND_RESOURCE = "dict.brand.resource";
	/** 头条类型*/
	public static final String SYS_DICT_NEWS_TYPE = "dict.news.type";
	/** 账户类型*/
	public static final String SYS_DICT_ACCOUNT_TYPE = "dict.account.type";
	/** 账户充值方式*/
	public static final String SYS_DICT_ACCOUNT_PAY_WAY = "dict.account.pay.way";
	/** 收支类型*/
	public static final String SYS_DICT_ACCOUNT_BUSS_TYPE = "dict.account.buss.type";
	
	public static void initSetting(List<Setting> dbSettings) {
		if(!CollectionUtils.isEmpty(dbSettings)){
			for (Setting setting : dbSettings) {
				settings.put(setting.getKey(), setting.getValue());
			}
		}
	}
	public static String getSetting(String key){
		if(null!=settings&&settings.containsKey(key)){
			return (String) settings.get(key);
		}
		return null;
	};
	
	public static boolean isImage(String link){
		String ext = FileUtil.getFileExtension(link);
		if(!StringUtils.isEmpty(ext)&&ext.toLowerCase().equals("jpg")
				||ext.toLowerCase().equals("png")
				||ext.toLowerCase().equals("jpeg")
				||ext.toLowerCase().equals("gif")
				||ext.toLowerCase().equals("bmp")){
			return true;
		}
		return false;
	}
	
	public static JSONObject initMaps(){
			try {
				if(null!=map)return map;
				JSONObject result = new JSONObject();
				JSONObject jsonProvinces = AmapUtil.getAmapInfo(null);
				JSONArray provinces = new JSONArray();
				if(null!=jsonProvinces){
					JSONArray districts = jsonProvinces.getJSONArray("districts");
					if(null!=districts&&districts.size()>0){
							provinces = districts.getJSONObject(0).getJSONArray("districts");
					}
				}
				if(!CollectionUtils.isEmpty(provinces)){
					for (Object provinceJson : provinces) {
						JSONObject province = (JSONObject) provinceJson;
						if(province.containsKey("adcode")&&!StringUtils.isEmpty(province.get("adcode"))){
							String adcode = province.get("adcode").toString();
							JSONObject jsonCitys = AmapUtil.getAmapInfo(adcode);
							if(null!=jsonCitys){
								JSONArray districts = jsonCitys.getJSONArray("districts");
								if(null!=districts&&districts.size()>0){
									JSONArray citys = districts.getJSONObject(0).getJSONArray("districts");
									if(!CollectionUtils.isEmpty(citys)){
										for (Object cityJson : citys) {
											JSONObject city = (JSONObject) cityJson;
											if(city.containsKey("citycode")&&!StringUtils.isEmpty(city.get("citycode"))){
												String citycode = city.get("citycode").toString();
												JSONObject jsonAreas = AmapUtil.getAmapInfo(citycode);
												if(null!=jsonAreas){
													JSONArray districts2 = jsonAreas.getJSONArray("districts");
													if(null!=districts2&&districts2.size()>0){
														JSONArray areas = districts2.getJSONObject(0).getJSONArray("districts");
														city.put("areas", areas);
													}
												}
											}else{
												city.put("areas", new ArrayList<>());
											}
											city.remove("districts");
										}
										province.remove("districts");
										province.put("citys", citys);
									}
								}
							}
						}else{
							province.remove("districts");
							province.put("citys", new ArrayList<>());
						}
					}
				}
				result.put("provinces", provinces);
				map = result;
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	}
}
