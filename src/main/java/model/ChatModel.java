package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import apps.appsProxy;
import esayhelper.DBHelper;
import esayhelper.JSONHelper;
import esayhelper.TimeHelper;
import esayhelper.fileHelper;
import esayhelper.jGrapeFW_Message;
import httpClient.request;
import httpServer.reqSession;
import nlogger.nlogger;
import security.codec;
import session.session;
import subscribe.sdkServer;
import thirdsdk.wechatHelper;
import thirdsdk.wechatModel;

public class ChatModel {
	private DBHelper sdkRoute;
	private DBHelper sdkUser;
	private JSONObject _obj = new JSONObject();
	private String APPID = "wxd4ed724da52799cb";
	private String APPSECRET = "6b45df4cd58422eff5a3a707500cb8ca";
	private wechatHelper helper = new wechatHelper(APPID, APPSECRET);

	private DBHelper getRoute() {
		if (sdkRoute == null) {
			sdkRoute = new DBHelper(appsProxy.configValue().get("db").toString(), "sdkrouter", "id");
			// sdkRoute = new DBHelper("localdb", "sdkrouter", "id");
		}
		return sdkRoute;
	}

	private DBHelper getUser() {
		if (sdkUser == null) {
			sdkUser = new DBHelper(appsProxy.configValue().get("db").toString(), "sdkuser", "id");
			// sdkUser = new DBHelper("localdb", "sdkuser", "id");
		}
		return sdkUser;
	}

	// 第三方平台
	public String addRoute(JSONObject object) {
		String code = sdkServer.addListen(object.get("sdkname").toString(), object.get("invoke").toString());
		return resultMessage(findRoute(code));
	}

	public int EditRoute(String id, JSONObject object) {
		boolean code = sdkServer.editListen(id, object.get("invoke").toString());
		return code == true ? 0 : 99;
	}

	public int RemoveRoute(String id) {
		boolean code = sdkServer.removeListen(id);
		return code == true ? 0 : 99;
	}

	public int DeleteRoute(String[] id) {
		getRoute().or();
		int len = id.length;
		for (String string : id) {
			getRoute().eq("id", string);
		}
		return getRoute().deleteAll() == len ? 0 : 99;
	}

	public JSONObject findRoute(String id) {
		return getRoute().eq("id", Integer.parseInt(id)).find();
	}

	@SuppressWarnings("unchecked")
	public String PageRoute(int ids, int pageSize) {
		JSONArray array = getRoute().page(ids, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize", (int) Math.ceil((double) getRoute().count() / pageSize));
		object.put("currentPage", ids);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String PageRoute(int ids, int pageSize, JSONObject info) {
		for (Object object2 : info.keySet()) {
			getRoute().eq(object2.toString(), info.get(object2.toString()));
		}
		JSONArray array = getRoute().dirty().page(ids, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize", (int) Math.ceil((double) getRoute().count() / pageSize));
		object.put("currentPage", ids);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	// 微信平台用户
	public String addPUser(JSONObject object) {
		String code = getUser().data(object).insertOnce().toString();
		return code;
	}

	public int EditPUser(String id, JSONObject object) {
		// boolean code = sdkServer.editPaltUser(Integer.parseInt(id),
		// object.get("configstring").toString());
		JSONObject obj = getUser().eq("id", id).data(object).update();
		return obj != null ? 0 : 99;
	}

	public int RemovePUser(String id) {
		String code = sdkServer.removePaltUser(Integer.parseInt(id));
		return code.equals("true") ? 0 : 99;
	}

	public int DeletePUser(String[] id) {
		getUser().or();
		int len = id.length;
		for (String string : id) {
			getUser().eq("id", string);
		}
		return getUser().deleteAll() == len ? 0 : 99;
	}

	public JSONObject findPUser(String id) {
		return getUser().eq("id", Integer.parseInt(id)).find();
	}

	@SuppressWarnings("unchecked")
	public String pagePUser(int idx, int pageSize) {
		JSONArray array = getUser().page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize", (int) Math.ceil((double) getUser().count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String pagePUser(int idx, int pageSize, JSONObject fileInfo) {
		for (Object object2 : fileInfo.keySet()) {
			getUser().eq(object2.toString(), fileInfo.get(object2.toString()));
		}
		JSONArray array = getUser().dirty().page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize", (int) Math.ceil((double) getUser().count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	// 微信发送消息
	public String send(JSONObject object) {
		if (object == null) {
			return resultMessage(99, "");
		}
		JSONObject param = JSONHelper.string2json(object.get("configstring").toString());
		String tip = wechatHelper.checkSignature(param);
		if (tip.equals("putao520:error")) {
			return resultMessage(2, "");
		}
		// wechatHelper wechatHelper = new wechatHelper(
		// param.get("appid").toString(),
		// param.get("appsecret").toString());
		// String openid = object.get("openid").toString();
		JSONObject content = wechatModel.toall.text(object.get("content").toString());
		// 根据openid，获取用户id
		// String userid = wechatHelper.getUserInfo(openid).toString();
		JSONObject object2 = helper.send2all(0, content, wechatModel.MSGTYPE_TEXT);
		return resultMessage(object2);
	}

	// 微信发送消息
	@SuppressWarnings("unchecked")
	public String send(String id, String content) {
		JSONObject object = findPUser(id);
		object.put("content", content);
		return send(object);
	}

	public String getopenid(String code) {
		String openid = "";
		session session = new session();
		if (session.get(APPID) != null) {
			session.delete(APPID);
		}
		if (session.get(APPID + "_webtoken") != null) {
			session.delete(APPID + "_webtoken");
		}
		openid = helper.getOpenID(code);
		return openid;
	}

	// 获取微信签名
	@SuppressWarnings("unchecked")
	public String getSign(String url) {
		// url = url.replaceAll("@t", "/");
		// url = url.replaceAll("@q", "&");
		url = codec.DecodeHtmlTag(url);
		String message = helper.signature(url);
		JSONObject object = JSONHelper.string2json(message);
		object.put("appid", APPID);
		return resultMessage(0, object.toString());
	}

	// 获取微信用户信息
	public String getUserInfo(String openid) {
		return resultMessage(helper.getUserInfo(openid));
	}

	// 上传媒体文件
	public void MediaUpload(String fileurl) {
		File file = new File(fileurl);
		helper.uploadmedia(file);
	}

	// 下载媒体文件
	
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

	@SuppressWarnings("unchecked")
	private String resultMessage(JSONObject object) {
		_obj.put("records", object);
		return resultMessage(0, _obj.toString());
	}

	public String resultMessage(int num, String message) {
		String msg = "";
		switch (num) {
		case 0:
			msg = message;
			break;
		case 1:
			msg = "必填项没有填";
			break;
		case 2:
			msg = "微信配置项验证失败";
			break;
		default:
			msg = "其它异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
