package com.infinity.gam_o_sapien;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

public class ControlScreen extends ActionBarActivity implements
		SensorEventListener {

	private SensorManager mSensorManager;
	float Rot[] = null; // for gravity rotational data
	float I[] = null; // for magnetic rotational data
	float accels[] = new float[3];
	float mags[] = new float[3];
	float[] values = new float[3];
	private double YawValue;
	private double PitchValue;
	private double RollValue;

	/*************************/
	private static DatagramSocket clientSocket;
	private int keycurrStates[] = new int[11]; // Supports 11 HW Keys
	private static String ipAdr;
	private static int IpPort = 9001;
	private int yawCorrection = 0;
	private int lastYaw;
	private int yawBase;
	protected boolean YawSample;
	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
	private KeysStorage kStorageengine;
	private char[] savedKeys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlscreenlayout);
		//To make it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSupportActionBar().hide();

		// get sensorManager and initialise sensor listeners
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		initListeners();
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

		yawBase = 0;

		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		OnTouchListener oclBtn = new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				switch (v.getId()) {
				case R.id.b1:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[0] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[0] = 2;
					}

					break;
				case R.id.b2:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[1] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[1] = 2;
					}

					break;
				case R.id.b3:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[2] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[2] = 2;
					}
					break;
				case R.id.b4:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[3] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[3] = 2;
					}

					break;

				case R.id.b5:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[4] = 1;

					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[4] = 2;
					}
					yawBase = lastYaw;
					break;

				case R.id.b6:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[5] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[5] = 2;
					}

					break;
				case R.id.b7:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[6] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[6] = 2;
					}

					break;
				case R.id.b8:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[7] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[7] = 2;
					}

					break;
				case R.id.b9:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keycurrStates[8] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keycurrStates[8] = 2;
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
		b5.setOnTouchListener(oclBtn);
		// b5.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// // YawSample = true;
		// yawBase = lastYaw;
		// }
		// });

		// To keep App Screen ON
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}

	// Volume Rocker Key Control
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			keycurrStates[10] = 1;
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			keycurrStates[9] = 1;
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			keycurrStates[10] = 2;
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			keycurrStates[9] = 2;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bundle bundle = getIntent().getExtras();
		String msg = (String) bundle.get("ip");
		String[] ipParts = msg.split(":");
		ipAdr = ipParts[0];
		IpPort = Integer.parseInt(ipParts[1]);

		// restore the sensor listeners when user resumes the application.
		initListeners();

	}

	// This function registers sensor listeners for the accelerometer,
	// magnetometer and gyroscope.
	public void initListeners() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	public void onSensorChanged(SensorEvent event) {

		// if sensor is unreliable, return void
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			Log.d("INF", "Sensor Data Unreliable");
			return;
		}

		switch (event.sensor.getType()) {
		case Sensor.TYPE_MAGNETIC_FIELD:
			mags = event.values.clone();
			break;
		case Sensor.TYPE_ACCELEROMETER:
			accels = event.values.clone();
			break;
		}

		if (mags != null && accels != null) {
			Rot = new float[9];
			I = new float[9];
			SensorManager.getRotationMatrix(Rot, I, accels, mags);
			// Correct if screen is in Landscape

			float[] outR = new float[9];
			SensorManager.remapCoordinateSystem(Rot, SensorManager.AXIS_X,
					SensorManager.AXIS_Z, outR);
			SensorManager.getOrientation(outR, values);

			YawValue = values[0] * 57.2957795f; // looks like we don't need this
												// one
			PitchValue = values[1] * 57.2957795f;
			RollValue = values[2] * 57.2957795f;
			mags = null; // retrigger the loop when things are repopulated
			accels = null; // //retrigger the loop when things are repopulated
		}

		updateandSendOreintationData();

	}

	public void updateandSendOreintationData() {
		String yawText = ""
				+ correctYaw((int) Math.round(YawValue), yawCorrection);
		String pitchText = "" + Math.round(PitchValue);
		String rollText = "" + Math.round(RollValue);
		if (YawSample) {
			YawSample = false;
			yawCorrection = (int) Math.round(YawValue);
		}
		try {
			StringBuilder sendData = new StringBuilder("DEVICE," + rollText
					+ "," + pitchText + "," + yawText);

			sendData.append("," + getKeyNameandState(0, keycurrStates));
			sendData.append("," + getKeyNameandState(1, keycurrStates));
			sendData.append("," + getKeyNameandState(2, keycurrStates));
			sendData.append("," + getKeyNameandState(3, keycurrStates));
			sendData.append("," + getKeyNameandState(4, keycurrStates));
			sendData.append("," + getKeyNameandState(5, keycurrStates));
			sendData.append("," + getKeyNameandState(6, keycurrStates));
			sendData.append("," + getKeyNameandState(7, keycurrStates));
			sendData.append("," + getKeyNameandState(8, keycurrStates));
			sendData.append("," + getKeyNameandState(9, keycurrStates));
			sendData.append("," + getKeyNameandState(10, keycurrStates));

			sendSensors(sendData.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void updateKeyPadUI() {
		Log.d("INF", "UPDATING KEYPAD");
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

	private String getProperText(char keyCharacter) {
		// !->LEFT,@->RIGHT,_->SPACE,*->MOUSERIGHT,$->MOUSELEFT,
		// %->UP,^->DOWN,&->CTRL,(->SHIFT,)->ALT,
		String ret;
		Log.d("INF", "KEYCHAR : " + keyCharacter);
		switch (keyCharacter) {
		case '!':
			ret = "LEFT";
			break;
		case '@':
			ret = "RIGHT";
			break;
		case '_':
			ret = "SPACE";
			break;
		case '%':
			ret = "UP";
			break;
		case '^':
			ret = "DOWN";
			break;
		case '&':
			ret = "CTRL";
			break;
		case '(':
			ret = "SHIFT";
			break;
		case ')':
			ret = "ALT";
			break;
		default:
			ret = keyCharacter + "";
		}
		return ret;
	}

	private String getKeyNameandState(int keyno, int keyVal[]) {

		char key;

		switch (keyno) {
		case 0:
			key = savedKeys[0];
			break;
		case 1:
			key = savedKeys[1];
			break;
		case 2:
			key = savedKeys[2];
			break;
		case 3:
			key = savedKeys[3];
			break;
		case 4:
			key = savedKeys[4];
			break;
		case 5:
			key = savedKeys[5];
			break;
		case 6:
			key = savedKeys[6];
			break;
		case 7:
			key = savedKeys[7];
			break;
		case 8:
			key = savedKeys[8];
			break;
		case 9:
			key = '$';
			break;
		case 10:
			key = '*';
			break;
		default:
			key = '0';
			break;

		}
		String ret = key + "" + keyVal[keyno];
		if (keyVal[keyno] == 2) {
			keyVal[keyno] = 0;
		}
		return ret;
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