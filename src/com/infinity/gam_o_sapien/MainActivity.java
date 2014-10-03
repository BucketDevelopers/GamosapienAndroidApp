package com.infinity.gam_o_sapien;

import java.util.List;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private SensorManager sMgr;
	private Boolean hasAccel;
	private Boolean hasMag;
	private TextView supportedTextView;
	private TextView accelsupport;
	private TextView magsupport;
	private View unsupported;
	private View Supported;
	private Button sendIP;
	private EditText ipAddressET;
	private EditText ipPortET;
	protected boolean iptextispresent;
	protected boolean porttextispresent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		hasAccel = false;
		hasMag = false;
		sMgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		supportedTextView = (TextView) findViewById(R.id.Supported);
		accelsupport = (TextView) findViewById(R.id.accelsupport);
		magsupport = (TextView) findViewById(R.id.magsupport);
		sendIP = (Button) findViewById(R.id.sendIP);
		Supported = findViewById(R.id.forSupported);
		unsupported = findViewById(R.id.forUnsupported);
		ipAddressET = (EditText) findViewById(R.id.IPaddress);
		ipPortET = (EditText) findViewById(R.id.Port);

		if (deviceSupported()) {
			supportedTextView.setText("Wohoo , Device is Supported ! :)");
		} else {
			supportedTextView.setText("Sorry , Device Unsupported :( ");
		}
		if (hasAccel) {
			accelsupport.setText(getResources().getText(R.string.checkmark));
			accelsupport.setTextColor(Color.GREEN);
		}
		if (hasMag) {
			magsupport.setText(getResources().getText(R.string.checkmark));
			magsupport.setTextColor(Color.GREEN);
		}

		if (deviceSupported() == true) {
			unsupported.setVisibility(View.GONE);
			Supported.setVisibility(View.VISIBLE);
		} else {

			Supported.setVisibility(View.GONE);
			unsupported.setVisibility(View.VISIBLE);
		}
		ipPortET.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.toString().trim().equals("")
						|| s.toString().trim().equals("Enter Port")) {
					porttextispresent = false;
				} else {
					porttextispresent = true;
				}

				setSendButton(iptextispresent, porttextispresent);

			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		ipAddressET.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.toString().trim().equals("")
						|| s.toString().trim().equals("Enter IP")) {
					iptextispresent = false;
				} else {
					iptextispresent = true;
				}

				setSendButton(iptextispresent, porttextispresent);

			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		sendIP.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				String ipAddr;
				String port;
				ipAddr = ipAddressET.getText().toString().trim();
				port = ipPortET.getText().toString().trim();
				Intent i = new Intent(getApplicationContext(),
						ControlScreen.class);
				// Create the bundle
				Bundle b = new Bundle();
				// Add your data to bundle
				b.putString("ip", ipAddr + ":" + port);
				i.putExtras(b);
				startActivity(i);

			}
		});

	}

	protected void setSendButton(boolean iptextispresent2,
			boolean porttextispresent2) {

		if (iptextispresent2 && porttextispresent2) {
			sendIP.setText(" Touch to Connect to PC ");
			sendIP.setClickable(true);
			sendIP.setBackgroundColor(Color.BLACK);
		} else {
			sendIP.setText(getResources().getText(R.string.IP_details_label));
			sendIP.setClickable(false);
			sendIP.setBackgroundColor(Color.DKGRAY);

		}

	}

	public Boolean deviceSupported() {
		List<Sensor> list = sMgr.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : list) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				hasAccel = true;
			} else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				hasMag = true;
			}

		}

		if (hasAccel && hasMag) {
			return true;
		}
		return false;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if(id == R.id.About){
			Intent i = new Intent(getApplicationContext(), About.class);
			startActivity(i);
		}else if(id == R.id.editKeys){
			Intent i = new Intent(getApplicationContext(), EditKeysScreen.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
}
