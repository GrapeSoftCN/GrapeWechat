package interfaceApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.CacheHelper;
import esayhelper.JSONHelper;
import httpClient.request;
import model.ChatModel;
import nlogger.nlogger;
import thirdsdk.wechatDef;
import thirdsdk.wechatHelper;
import thirdsdk.wechatModel;

public class Wechat {
	private wechatHelper helpers;
	private ChatModel model = new ChatModel();

	/* 操作第三方平台 */
	// 新增
	public String AddRoute(String Info) {
		return model.addRoute(JSONHelper.string2json(Info));
	}

	public String UpdateRoute(String id, String Info) {
		return model.resultMessage(model.EditRoute(id, JSONHelper.string2json(Info)), "修改成功");
	}

	public String DeleteRoute(String id) {
		return model.resultMessage(model.RemoveRoute(id), "删除成功");
	}

	public String DeleteBatchRoute(String ids) {
		return model.resultMessage(model.DeleteRoute(ids.split(",")), "删除成功");
	}

	public String PageRoute(int ids, int pageSize) {
		return model.PageRoute(ids, pageSize);
	}

	public String PageByRoute(int ids, int pageSize, String RouteInfo) {
		return model.PageRoute(ids, pageSize, JSONHelper.string2json(RouteInfo));
	}

	/* 操作第三方平台用户 */
	// 新增
	public String AddPUser(String Info) {
		return model.addPUser(JSONHelper.string2json(Info));
	}

	public String UpdatePUser(String id, String Info) {
		return model.resultMessage(model.EditPUser(id, JSONHelper.string2json(Info)), "修改成功");
	}

	public String DeletePUser(String id) {
		return model.resultMessage(model.RemovePUser(id), "删除成功");
	}

	public String DeleteBatchPUser(String ids) {
		return model.resultMessage(model.DeletePUser(ids.split(",")), "删除成功");
	}

	public String PagePUser(int ids, int pageSize) {
		return model.pagePUser(ids, pageSize);
	}

	public String PageByPUser(int ids, int pageSize, String PUserInfo) {
		return model.pagePUser(ids, pageSize, JSONHelper.string2json(PUserInfo));
	}

	public String getPUser(String uid) {
		return model.resultMessage(0, model.GetConfigString(uid).toString());
		// return model.pagePUser(ids, pageSize,
		// JSONHelper.string2json(PUserInfo));
	}

	// 微信发送消息
	public String sendMsg(String parameter) {
		JSONObject object = JSONHelper.string2json(parameter);
		return model.send(object);
	}

	// 微信发送消息
	public String ToMsg(String id, String content) {
		return model.send(id, content);
	}

	// 微信授权,获取openid
	public String BindWechat(String code) {
		return model.getopenid(code);
	}

	// 微信上传素材
	public String uploadMedia() {

		return null;
	}

	// 微信下载素材
	public String downloadMedia(String mediaid) {
		String message = "";
		try {
			String hoString = "http://" + getAppIp("file").split("/")[0];
			System.out.println(hoString);
			message = request.Get(hoString + "/File/WechatDownload?mediaid=" + mediaid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	// 获取签名
	public String getSignature(String url) {
		return model.getSign(url);
	}

	// 获取用户信息
	public String getUserInfo(String openid) {
		CacheHelper helper = new CacheHelper();
		String userinfo = "";
		if (helper.get(openid + "_userinfo") != null) {
			userinfo = helper.get(openid + "_userinfo");
		}
		userinfo = model.getUserInfo(openid);
		helper.setget(openid + "_userinfo", userinfo);
		return userinfo;
	}

	public JSONObject uploadmedia(String id) {
		helpers = getWechatHelper(id);
		JSONObject object = null;
		if (helpers != null) {
			object = new JSONObject();
			object = helpers.uploadmedia(
					new File("F:\\tomcat8.0\\webapps\\File\\upload\2017-07-01\\tlszf_bgPic_20161104-01.jpg"));
		}
		nlogger.logout(object);
		return object;
	}

	/**
	 * 推送图文消息
	 * 
	 * @project GrapeWechat
	 * @package interfaceApplication
	 * @file Wechat.java
	 * 
	 * @param id
	 * @return
	 *
	 */
	public String UploadMpNew(String id, String content) {
		JSONObject object = new JSONObject();
		wechatModel.uploadArticle upload = new wechatModel.uploadArticle();
		JSONObject media = uploadmedia(id);
		JSONArray array = upload.newArticle(media.getString("media_id"), "", "测试", "测试描述",
				"http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg", "这是个测试", false).toArticleArray();
		nlogger.logout(array);
		helpers = getWechatHelper(id);
		if (helpers != null && array != null) {
			object = helpers.uploadArticles(array);
			nlogger.logout(object);
			object = wechatModel.toall.mpNews(object.getString("media_id"));
			object = helpers.send2all(0, object, wechatModel.MSGTYPE_NEWS);
		}
		nlogger.logout("array: " + array + ",obj: " + object);
		return object.toJSONString();
	}

	private wechatHelper getWechatHelper(String id) {
		CacheHelper cache = new CacheHelper("redis");
		// redis cache = new redis();
		JSONObject object = getConfig(id);
		nlogger.logout(object);
		String appid;
		String appsecret;
		if (object != null) {
			try {
				appid = object.getString("appid");
				appsecret = object.getString("appsecret");
				if (cache.get(appid) != null) {
					cache.delete(appid);
				}
				if (cache.get(appid + "_webtoken") != null) {
					cache.delete(appid + "_webtoken");
				}
				helpers = new wechatHelper(appid, appsecret);
			} catch (Exception e) {
				nlogger.logout(e);
				helpers = null;
			}
		}
		return helpers;
	}

	private JSONObject getConfig(String id) {
		JSONObject rObject = JSONHelper.string2json(getPUser(id));
		rObject = JSONHelper.string2json(rObject.getString("message"));
		return rObject == null ? null : JSONHelper.string2json(rObject.getString("configstring"));
	}

	private String getAppIp(String key) {
		String value = "";
		try {
			Properties pro = new Properties();
			pro.load(new FileInputStream("URLConfig.properties"));
			value = pro.getProperty(key);
		} catch (Exception e) {
			value = "";
		}
		return value;
	}
}
