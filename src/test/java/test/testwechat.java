package test;

import java.io.IOException;

import httpServer.booter;

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
