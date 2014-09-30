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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

public class ControlScreen extends Activity implements SensorEventListener {

	float Rot[] = null; // for gravity rotational data
	// don't use R because android uses that for other stuff
	float I[] = null; // for magnetic rotational data
	float accels[] = new float[3];
	float mags[] = new float[3];
	float[] values = new float[3];
	private double YawValue;
	private double PitchValue;
	private double RollValue;

	/*************************/
	private static DatagramSocket clientSocket;
	private int keyValues[]=new int[11]; // Supports 11 Keys
	private static String ipAdr;
	private static int IpPort = 9001;
	private int yawCorrection = 0;
	private int lastYaw;
	private int yawBase;
	protected boolean YawSample;
	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;

	private SensorManager mSensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlscreenlayout);

		// get sensorManager and initialise sensor listeners
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		initListeners();

		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
		b6 = (Button) findViewById(R.id.b6);
		b7 = (Button) findViewById(R.id.b7);
		b8 = (Button) findViewById(R.id.b8);
		b9 = (Button) findViewById(R.id.b9);
		yawBase = 0;
		// key_string = "0";

		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		OnTouchListener oclBtn = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				switch (v.getId()) {
				case R.id.b1:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[0] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[0] = 2;
					}

					break;
				case R.id.b2:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[1] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[1] = 2;
					}

					break;
				case R.id.b3:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[2] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[2] = 2;
					}
					break;
				case R.id.b4:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[3] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[3] = 2;
					}

					break;

				case R.id.b5:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[4] = 1;
						
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[4] = 2;
					}
					yawBase = lastYaw;
					break;

				case R.id.b6:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[5] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[5] = 2;
					}

					break;
				case R.id.b7:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[6] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[6] = 2;
					}

					break;
				case R.id.b8:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[7] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[7] = 2;
					}

					break;
				case R.id.b9:
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						keyValues[8] = 1;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						keyValues[8] = 2;
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
//		b5.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				// YawSample = true;
//				yawBase = lastYaw;
//			}
//		});

		// To keep App Screen ON
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}

	// Volume Rocker Key Control
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			keyValues[10] = 1;
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			keyValues[9] = 1;
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			keyValues[10] = 2;
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			keyValues[9] = 2;
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

		// below commented code - junk - unreliable is never populated
		// if sensor is unreliable, return void
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
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

				sendData.append("," + getKeyNameandState(0,keyValues));
				sendData.append("," + getKeyNameandState(1,keyValues));
				sendData.append("," + getKeyNameandState(2,keyValues));
				sendData.append("," + getKeyNameandState(3,keyValues));
				sendData.append("," + getKeyNameandState(4,keyValues));
				sendData.append("," + getKeyNameandState(5,keyValues));
				sendData.append("," + getKeyNameandState(6,keyValues));
				sendData.append("," + getKeyNameandState(7,keyValues));
				sendData.append("," + getKeyNameandState(8,keyValues));
				sendData.append("," + getKeyNameandState(9,keyValues));
				sendData.append("," + getKeyNameandState(10,keyValues));

				sendSensors(sendData.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getKeyNameandState(int keyno,int keyVal[]) {
		
		char key;
		
		
		switch(keyno)
		{
			case 0:key='Q';break;
			case 1:key='W';break;
			case 2:key='R';break;
			case 3:key='A';break;
			case 4:key='0';break;
			case 5:key='D';break;
			case 6:key='0';break;
			case 7:key='S';break;
			case 8:key=' ';break;
			case 9:key='$';break;
			case 10:key='*';break;
			default:key='0';break;	
		
		}
		String ret=key+""+keyVal[keyno];
		if(keyVal[keyno]==2)
		{
			keyVal[keyno]=0;
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