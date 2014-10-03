package com.infinity.gam_o_sapien;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EditKeysScreen extends ActionBarActivity{

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
			
			public void onClick(View clicked) {
				Button b = (Button) clicked;
				
				b.setTextColor(Color.GREEN);
				
			}
		};
		b1.setOnClickListener(changeKeymethod );
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
