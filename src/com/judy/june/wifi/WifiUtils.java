/**
 * 
 */
package com.judy.june.wifi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * Wifi密码查看器
 * @author junehappylove
 *
 */
public class WifiUtils {
	public static final String NO_PASS = "not find password!";

	/**
	 * 取wifi密码信息
	 * @return
	 */
	public static List<WifiInfo> wifis(){
		return sort(wifis(getWifiConf()));
	}
	
	/**
	 * 取wifi配置配置
	 * @return
	 */
	private static StringBuffer getWifiConf(){
		Process process = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			process = Runtime.getRuntime().exec("su");//切换至root用户
			out = new DataOutputStream(process.getOutputStream());//
			in = new DataInputStream(process.getInputStream());//
			out.writeBytes("cat /data/misc/wifi/*.conf\n");//
			out.writeBytes("exit\n");
			out.flush();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");//
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine())!=null) {
				sb.append(line);
			}
			br.close();
			reader.close();
			process.waitFor();
		} catch (IOException e) {
			Log.e("wifi", "process or stream run error!", e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.e("wifi", "process close error!", e);
			e.printStackTrace();
		} finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				Log.e("wifi", "stream close error!", e);
				e.printStackTrace();
			}
			process.destroy();
		}
		return sb;
	}

	/**
	 * 获取wifi列表信息
	 * @param sb
	 * @return
	 */
	private static List<WifiInfo> wifis(StringBuffer sb){
		List<WifiInfo> wifis = new ArrayList<WifiInfo>() ;
		Pattern pattern = Pattern.compile("network=\\{([^\\}]+)\\}",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(sb.toString());
		while (matcher.find()) {
			String block = matcher.group();
			Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
			Matcher sm= ssid.matcher(block);
			if(sm.find()){
				WifiInfo wifi = new WifiInfo();
				wifi.setSsid(sm.group(1));
				Pattern pass = Pattern.compile("psk=\"([^\"]+)\"");
				Matcher pm= pass.matcher(block);
				if(pm.find()){
					wifi.setPass(pm.group(1));
				}else{
					wifi.setPass(NO_PASS);
				}
				wifis.add(wifi);
			}
		}
		return wifis;
	}
	
	private static List<WifiInfo> sort(final List<WifiInfo> list){
		Collections.sort(list);
		return list;
	}
}
