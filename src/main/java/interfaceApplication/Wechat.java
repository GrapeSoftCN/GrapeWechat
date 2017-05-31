package interfaceApplication;


import java.io.FileInputStream;
import java.util.Properties;

import org.json.simple.JSONObject;

import apps.appsProxy;
import esayhelper.JSONHelper;
import httpClient.request;
import model.ChatModel;
import rpc.execRequest;

public class Wechat {
	private ChatModel model = new ChatModel();

	/* 操作第三方平台 */
	// 新增
	public String AddRoute(String Info) {
		return model.addRoute(JSONHelper.string2json(Info));
	}

	public String UpdateRoute(String id, String Info) {
		return model.resultMessage(
				model.EditRoute(id, JSONHelper.string2json(Info)), "修改成功");
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
		return model.PageRoute(ids, pageSize,
				JSONHelper.string2json(RouteInfo));
	}

	/* 操作第三方平台用户 */
	// 新增
	public String AddPUser(String Info) {
		return model.addPUser(JSONHelper.string2json(Info));
	}

	public String UpdatePUser(String id, String Info) {
		return model.resultMessage(
				model.EditPUser(id, JSONHelper.string2json(Info)), "修改成功");
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
		return model.pagePUser(ids, pageSize,
				JSONHelper.string2json(PUserInfo));
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
	public String downloadMedia(String mediaid){
		String message = "";
		try{
			String hoString = "http://"+getAppIp("file").split("/")[0];
			System.out.println(hoString);
			message = request.Get(hoString+"/File/WechatDownload?mediaid="+mediaid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return message;
	}

	// 获取签名
	public String getSignature(String url) {
		return model.getSign(url);
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
	// 获取用户信息
	public String getUserInfo(String openid) {
		return model.getUserInfo(openid);
	}
}
