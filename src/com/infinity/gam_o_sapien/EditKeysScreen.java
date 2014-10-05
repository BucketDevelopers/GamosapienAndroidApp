package com.infinity.gam_o_sapien;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditKeysScreen extends ActionBarActivity {

	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
	private KeysStorage kStorageengine;
	private char[] savedKeys;

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

				btn = (Button) clicked;
				// custom dialog
				dialog = new Dialog(EditKeysScreen.this);
				dialog.setContentView(R.layout.custom);
				dialog.setTitle("Select Key");

				final EditText keyEntered = (EditText) dialog
						.findViewById(R.id.enteredKey);

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
						String enteredCharacter = keyEntered.getText()
								.toString().trim();
						if (enteredCharacter.equals("")) {
							dialog.setTitle("Renter !");
						} else {
							Log.d("INF", "char "+enteredCharacter);
							btn.setTextColor(Color.GREEN);
							switch(btn.getId())
							{
							case R.id.b1:kStorageengine.saveKey('0', enteredCharacter.charAt(0));break;
							case R.id.b2:kStorageengine.saveKey('1', enteredCharacter.charAt(0));break;
							case R.id.b3:kStorageengine.saveKey('2', enteredCharacter.charAt(0));break;
							case R.id.b4:kStorageengine.saveKey('3', enteredCharacter.charAt(0));break;
							case R.id.b5:kStorageengine.saveKey('4', enteredCharacter.charAt(0));break;
							case R.id.b6:kStorageengine.saveKey('5', enteredCharacter.charAt(0));break;
							case R.id.b7:kStorageengine.saveKey('6', enteredCharacter.charAt(0));break;
							case R.id.b8:kStorageengine.saveKey('7', enteredCharacter.charAt(0));break;
							case R.id.b9:kStorageengine.saveKey('8', enteredCharacter.charAt(0));break;
							
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
		b5.setOnClickListener(changeKeymethod);
		b6.setOnClickListener(changeKeymethod);
		b7.setOnClickListener(changeKeymethod);
		b8.setOnClickListener(changeKeymethod);
		b9.setOnClickListener(changeKeymethod);

	}

	private void updateKeyPadUI() {
		b1.setText(savedKeys[0] + "");
		b2.setText(savedKeys[1] + "");
		b3.setText(savedKeys[2] + "");
		b4.setText(savedKeys[3] + "");
		b5.setText("CLBRTE");
		b5.setTextSize(12f);
		// b5.setText(savedKeys[4]+"");
		b6.setText(savedKeys[5] + "");
		b7.setText(savedKeys[6] + "");
		b8.setText(savedKeys[7] + "");
		b9.setText(savedKeys[8] + "");

	}

}
