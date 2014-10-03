package com.infinity.gam_o_sapien;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class KeysStorage {

	private Context appContext;
	private char[] keys;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private boolean initflag;

	public KeysStorage(Context cntxt) {
		appContext = cntxt;
		keys = new char[9];
		initflag = false;
		pref = PreferenceManager.getDefaultSharedPreferences(appContext);
		editor = pref.edit();
	}

	public void saveKey(char keyID, char saveValue) {
		editor.putInt(keyID + "", saveValue);
		if (!editor.commit()) {
			Log.d("INF", "SAVING THE KEY FAILED");
		}
	}

	public char readKey(char keyID) {

		int readval = pref.getInt(keyID + "", -1);
		if (readval == -1) {
			Log.d("INF", "READ FOR KEY FAILED");
			readval = '~';
		}
		char ch = (char) readval;

		Log.d("INF", (ch + ""));
		return ch;

	}

	public char[] readallKeys() {
		for (int i = 0; i < 9; ++i) {
			keys[i] = readKey((i + "").charAt(0));
			if (keys[i] == '~') {
				initflag = true;
				break;
			}
		}
		return keys;
	}

	public void saveallKeys(char k[]) {
		for (int i = 0; i < 9; ++i) {
			saveKey((i + "").charAt(0), k[i]);
		}

	}

	public void initSaveKeys() {
		readallKeys();
		if (!initflag) {
			return;
		}
		char defkeys[] = new char[9];
		defkeys[0] = 'q';
		defkeys[1] = 'w';
		defkeys[2] = 'r';
		defkeys[3] = 'a';
		defkeys[4] = '0';
		defkeys[5] = 'd';
		defkeys[6] = 'c';
		defkeys[7] = 's';
		defkeys[8] = ' ';
		saveallKeys(defkeys);
	}

}
