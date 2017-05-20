package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import httpServer.booter;
import interfaceApplication.Wechat;

public class testwechat {
	public static void main(String[] args) {
		// System.out.println(new Wechat().PageRoute(1, 2));
		// String string =
		// "{\"configstring\":{\"appid\":\"123\",\"appsecret\":\"123\",\"token\":\"123\"},\"platid\":\"1\",\"name\":\"test\"}";
		// System.out.println(new Wechat().AddPUser(string));

		// String string =
		// "{\"platid\":\"1\",\"configstring\":{\"appid\":\"123\",\"appsecret\":\"123\",\"token\":\"123\"}}";
		// String string =
		// "{\"platid\":\"1\",\"configstring\":\"123\",\"name\":\"test\"}";
//		 System.out.println(new Wechat().AddPUser(string));
		 booter booter = new booter();
		 System.out.println("Grapewe!");
		 try {
		 System.setProperty("AppName", "Grapewe");
		 booter.start(106);
		 } catch (Exception e) {
		
		 }
	}
}
