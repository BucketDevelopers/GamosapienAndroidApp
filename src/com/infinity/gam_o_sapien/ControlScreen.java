package com.infinity.gam_o_sapien;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class ControlScreen extends Activity implements SensorEventListener {

	private static DatagramSocket clientSocket;
	private String key_string;
	private SensorManager mSensorManager;
	private static String ipAdr;
	private static int IpPort = 9001;
	private int yawCorrection = 0;
	private int lastYaw;
	private int yawBase;
	protected boolean YawSample;
	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlscreenlayout);

		yawBase = 0;
		key_string = "0";
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
		b6 = (Button) findViewById(R.id.b6);
		b7 = (Button) findViewById(R.id.b7);
		b8 = (Button) findViewById(R.id.b8);
		b9 = (Button) findViewById(R.id.b9);
		OnTouchListener oclBtn = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.b1:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "1";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;
				case R.id.b2:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "2";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;
				case R.id.b3:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "3";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}
					break;
				case R.id.b4:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "4";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;
				case R.id.b6:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "6";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;
				case R.id.b7:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "7";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;
				case R.id.b8:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "8";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;
				case R.id.b9:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						key_string = "9";
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						key_string = "";
					}

					break;

				}
				return false;
			}
		};
		b1.setOnTouchListener(oclBtn);
		b2.setOnTouchListener(oclBtn);
		b3.setOnTouchListener(oclBtn);
		b4.setOnTouchListener(oclBtn);
		b6.setOnTouchListener(oclBtn);
		b7.setOnTouchListener(oclBtn);
		b8.setOnTouchListener(oclBtn);
		b9.setOnTouchListener(oclBtn);

		b5.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// YawSample = true;
				yawBase = lastYaw;
			}
		});

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// To keep App Screen ON
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			key_string = "11";
			Log.d("vol", "pressed");

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			// do nothing
			key_string = "10";

			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("This", "msgm");

		Bundle bundle = getIntent().getExtras();

		String msg = (String) bundle.get("ip");

		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		String[] ipParts = msg.split(":");
		ipAdr = ipParts[0];
		Log.d("This", ipAdr);
		// IpPort = Integer.parseInt(ipParts[1]);

		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	public void onSensorChanged(SensorEvent event) {
		String yawText = ""
				+ correctYaw(Math.round(event.values[0]), yawCorrection);
		String pitchText = "" + Math.round(event.values[1]);
		String rollText = "" + Math.round(event.values[2]);
		if (YawSample) {
			YawSample = false;
			yawCorrection = Math.round(event.values[0]);
		}
		try {
			if (key_string == "" || key_string == "0")
				sendSensors("Head," + rollText + "," + pitchText + ","
						+ yawText + ",0");
			else {
				sendSensors("Head," + rollText + "," + pitchText + ","
						+ yawText + "," + key_string);
				key_string = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public static void sendSensors(String sensorsdata) throws IOException {

		InetAddress IPAddress = InetAddress.getByName(ipAdr);
		byte[] sendData = new byte[1024];

		sendData = sensorsdata.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, IpPort);
		clientSocket.send(sendPacket);
	}

	public int correctYaw(int actualValue, int Correction) {
		int CorrectedValue = 0;

		lastYaw = actualValue;
		actualValue = (actualValue - yawBase) < 0 ? actualValue - yawBase + 360
				: actualValue - yawBase;

		if (actualValue > 180) {
			CorrectedValue = actualValue - 360;
		} else {
			CorrectedValue = actualValue;
		}
		return (CorrectedValue);
	}

}