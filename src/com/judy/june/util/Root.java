/**
 * 
 */
package com.judy.june.util;

import java.io.DataOutputStream;

import android.util.Log;

/**
 * 判读root权限
 * 
 * @author junehappylove
 *
 */
public class Root {
	public static synchronized boolean getRootAhth() {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("exit\n");
			os.flush();
			int exitValue = process.waitFor();
			if (exitValue == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.d("mywifi", "Unexpected error - Here is what I know: " + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
