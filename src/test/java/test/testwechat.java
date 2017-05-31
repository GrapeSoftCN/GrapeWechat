package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import httpServer.booter;
import interfaceApplication.Wechat;
import session.session;

public class testwechat {
	public static void main(String[] args) throws IOException {
//		System.out.println(new Wechat()
//				.downloadMedia("QklLbChYRaceFlg6i1CLj-6f47m_GtLH6BON-WF_qnhEVAlv07cDBvd_sWQV5R1u"));
		booter booter = new booter();
		 try {
		 System.out.println("GrapeWechat!");
		 System.setProperty("AppName", "GrapeWechat");
		 booter.start(1003);
		} catch (Exception e) {
		}
	}
}
