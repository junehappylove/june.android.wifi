/**
 * 
 */
package com.judy.june.wifi;

import android.annotation.SuppressLint;

/**
 * Wifi信息
 * @author junehappylove
 *
 */
public class WifiInfo implements Comparable<WifiInfo>{
	private String ssid;
	private String pass;
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public WifiInfo() {
		super();
	}
	public WifiInfo(String ssid, String pass) {
		super();
		this.ssid = ssid;
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "\n{ssid:"+this.ssid+",pass:"+this.pass+"}";
	}
	@SuppressLint("DefaultLocale")
	@Override
	public int compareTo(WifiInfo another) {
		if (this.ssid.toUpperCase().compareTo(another.getSsid().toUpperCase()) > 0) {
			return 1;
		}
		if (this.ssid.toUpperCase().compareTo(another.getSsid().toUpperCase()) < 0) {
			return -1;
		}
		if(this.pass.toUpperCase().compareTo(another.getPass().toUpperCase()) > 0) {
			return 1;
		}
		if(this.pass.toUpperCase().compareTo(another.getPass().toUpperCase()) < 0) {
			return -1;
		}
		
		return 0;
	}
	
}
