package com.infinity.gam_o_sapien;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class EditKeysScreen extends ActionBarActivity {

	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
	private KeysStorage kStorageengine;
	private char[] savedKeys;
	private static String keySelectedfromlist;
	private static String[] paths = { "-- Select --", "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
			"s", "t", "u", "v", "w", "x", "y", "z",
			"1", "2", "3", "4", "5", "6", "7", "8", "9",
			"Left Arrow","Right Arrow", "Up Arrow", "Down Arrow", 
			"Space", "Ctrl", "Shift","Alt" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlscreenlayout);

		kStorageengine = new KeysStorage(getApplicationContext());
		kStorageengine.initSaveKeys();
		savedKeys = kStorageengine.readallKeys();

		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
		b6 = (Button) findViewById(R.id.b6);
		b7 = (Button) findViewById(R.id.b7);
		b8 = (Button) findViewById(R.id.b8);
		b9 = (Button) findViewById(R.id.b9);

		updateKeyPadUI();

		OnClickListener changeKeymethod = new OnClickListener() {

			private Dialog dialog;
			private Button btn;

			public void onClick(View clicked) {
				// Initialise the variable every click
				keySelectedfromlist = "";

				btn = (Button) clicked;
				// custom dialog
				dialog = new Dialog(EditKeysScreen.this);
				dialog.setContentView(R.layout.customdialog);
				dialog.setTitle("Select Key to Emulate");

				Spinner droplistofkeys = (Spinner) dialog
						.findViewById(R.id.droplistofkeys);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						EditKeysScreen.this, R.layout.customspinner, paths);

				// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				droplistofkeys.setAdapter(adapter);
				droplistofkeys
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View v, int position, long id) {

								Boolean trivialKey = false;

								switch (position) {
//								!->LEFT,@->RIGHT,_->SPACE,*->MOUSERIGHT,$->MOUSELEFT,
//								%->UP,^->DOWN,&->CTRL,(->SHIFT,)->ALT,

								case 0:
									keySelectedfromlist = " ";
									break;
								case 36:
									keySelectedfromlist = "!";//"left";
									break;
								case 37:
									keySelectedfromlist = "@";//"right";
									break;
								case 38:
									keySelectedfromlist = "%";//"up";
									break;
								case 39:
									keySelectedfromlist = "^";//down";
									break;
								case 40:
									keySelectedfromlist = "_";//"space";
									break;
								case 41:
									keySelectedfromlist = "&";//"ctrl";
									break;
								case 42:
									keySelectedfromlist = "(";//"shift";
									break;
								case 43:
									keySelectedfromlist = ")";//"alt";
									break;
								default:
									keySelectedfromlist = " ";
									trivialKey = true;
									break;
								}
								
								//if its none of the above keys we can directly transmit whatever we selected
								//that is it is a,b,c,d,e,f...... or 1,2,3,4,5..........
								//and not ctrl ,alt ,etc
								//basically its a trivial key
								
								if (trivialKey) {
									
								if(position<27)
								{
									//Then these are a,b,c,d,e,........
									
									keySelectedfromlist=(char)('a'+(position-1))+"";
								
									
								}
								else if(position<36){
									//these are 1,2,3,4,...
									keySelectedfromlist=(((char)((position-27)+'1'))+"");
									
								}

								}
								
								Log.d("INF"," SELECTED : "+keySelectedfromlist);

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// Do Same thing as selecting 0th item
								keySelectedfromlist = " ";
							}
						});

				Button ok = (Button) dialog.findViewById(R.id.dialogButtonOK);

				Button cancel = (Button) dialog
						.findViewById(R.id.dialogButtonCancel);
				// if button is clicked, close the custom dialog
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String enteredCharacter = keySelectedfromlist.trim();
						if (enteredCharacter.equals("")) {
							dialog.setTitle("Select Again !");
						} else {
							Log.d("INF", "char " + enteredCharacter);
							btn.setTextColor(Color.GREEN);
							switch (btn.getId()) {
							case R.id.b1:
								kStorageengine.saveKey('0',
										enteredCharacter.charAt(0));
								break;
							case R.id.b2:
								kStorageengine.saveKey('1',
										enteredCharacter.charAt(0));
								break;
							case R.id.b3:
								kStorageengine.saveKey('2',
										enteredCharacter.charAt(0));
								break;
							case R.id.b4:
								kStorageengine.saveKey('3',
										enteredCharacter.charAt(0));
								break;
							case R.id.b5:
								kStorageengine.saveKey('4',
										enteredCharacter.charAt(0));
								break;
							case R.id.b6:
								kStorageengine.saveKey('5',
										enteredCharacter.charAt(0));
								break;
							case R.id.b7:
								kStorageengine.saveKey('6',
										enteredCharacter.charAt(0));
								break;
							case R.id.b8:
								kStorageengine.saveKey('7',
										enteredCharacter.charAt(0));
								break;
							case R.id.b9:
								kStorageengine.saveKey('8',
										enteredCharacter.charAt(0));
								break;

							}

							kStorageengine.readallKeys();
							updateKeyPadUI();
							dialog.dismiss();

						}

					}
				});

				dialog.show();

			}
		};
		b1.setOnClickListener(changeKeymethod);
		b2.setOnClickListener(changeKeymethod);
		b3.setOnClickListener(changeKeymethod);
		b4.setOnClickListener(changeKeymethod);
		b5.setClickable(false);//To be used as Calibrate Button only
		//b5.setOnClickListener(changeKeymethod);
		b6.setOnClickListener(changeKeymethod);
		b7.setOnClickListener(changeKeymethod);
		b8.setOnClickListener(changeKeymethod);
		b9.setOnClickListener(changeKeymethod);

	}

	private void updateKeyPadUI() {
		Log.d("INF","UPDATING KEYPAD");
		
		b1.setText(getProperText(savedKeys[0]));
		b2.setText(getProperText(savedKeys[1]));
		b3.setText(getProperText(savedKeys[2]));
		b4.setText(getProperText(savedKeys[3]));
		b5.setText("CLBRTE");
		b5.setTextSize(12f);
		// b5.setText(savedKeys[4]+"");
		b6.setText(getProperText(savedKeys[5]));
		b7.setText(getProperText(savedKeys[6]));
		b8.setText(getProperText(savedKeys[7]));
		b9.setText(getProperText(savedKeys[8]));

	}
	private String getProperText(char keyCharacter)
	{
//		!->LEFT,@->RIGHT,_->SPACE,*->MOUSERIGHT,$->MOUSELEFT,
//		%->UP,^->DOWN,&->CTRL,(->SHIFT,)->ALT,
		
		
		String ret;
		switch(keyCharacter)
		{
		case '!':ret="LEFT";break;
		case '@':ret="RIGHT";break;
		case '_':ret="SPACE";break;
		case '%':ret="UP";break;
		case '^':ret="DOWN";break;
		case '&':ret="CTRL";break;
		case '(':ret="SHIFT";break;
		case ')':ret="ALT";break;
		default :ret=keyCharacter+"";
		}
		Log.d("INF","PROPER "+ret);
		
		return ret;
		
	}

}
