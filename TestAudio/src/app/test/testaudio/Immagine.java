package app.test.testaudio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Immagine extends ActionBarActivity implements SensorEventListener{

	String filepath = "/sdcard/TestAudio";
	LinearLayout mLinearLayout;
	
	private SensorManager manager; 
	private Sensor accelerometro; 
	private FileWriter writer;
	int i = 0;
	int x,y,z, primo, secondo, terzo, quarto, quinto, sesto;

	
	int[] mappa = new int[400];
	Bitmap temp1;
	Bitmap bitmap=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_immagine);
		
		

		manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); 
		accelerometro = (Sensor) manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 
		manager.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_NORMAL); 

		
		mLinearLayout = new LinearLayout(this);
		File folder = new File(filepath);
		boolean success = true;
		if (!folder.exists()) {
		Toast.makeText(Immagine.this, "Directory Does Not Exist, I Create It", Toast.LENGTH_SHORT).show();
		success = folder.mkdir();
		}
		
		
		try {
		writer = new FileWriter(new File(filepath, "AranciaGranate.txt"), true);
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		
		

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	
	
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) { 
		//questo non ci serve quindi lo lasciamo vuoto
		}
	//ecco il nostro bad boy


	public void onSensorChanged(SensorEvent event) { 
		
		x = (int)Math.abs(event.values[0]);
		y = (int)Math.abs(event.values[1]);
		z = (int)Math.abs(event.values[2]);
		x= (int)((x*100)%255);
		y= (int)((y*100)%255);
		z= (int)((z*100)%255);
		
		
		
		try {
		writer.write(x+", "+y+", "+z+"\n");
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}		
		this.onPause();				
	}	//fine onSensorChanged	
			

	
	
	
	protected void onPause() {
	super.onPause();
	manager.unregisterListener(this);
	
	if(writer != null) {
	try {
	writer.close();
	}
	catch (IOException e) { // TODO Auto-generated catch block
	e.printStackTrace();
	}
	}//fine if
	
	Color col1 = new Color();
	primo = col1.argb(255, x, y, z);
	Color col2 = new Color();
	secondo = col2.argb(255, y, x, z);
	Color col3 = new Color();
	terzo = col3.argb(255, z, x, y);
	Color col4 = new Color();
	quarto = col4.argb(255, z, y, x);
	Color col5 = new Color();
	quinto = col5.argb(255, x, z, y);
	Color col6 = new Color();
	sesto = col6.argb(255, y, z, x);
	
	
    // read a Bitmap from Assets
		//per gli dei non cambiare ne rimuovere assolutamente
		//Bitmap bitmap = null;
	
	    try {
//	    	BitmapFactory.Options o = new BitmapFactory.Options();
//	        o.inJustDecodeBounds = true;
	      bitmap = BitmapFactory.decodeResource(getResources(),R.raw.basefortp);
	     
	      Log.d("onSensorChange()", ""+bitmap.getHeight()+ " " + bitmap.getWidth());
	      // Assign the bitmap to an ImageView in this layout
	      if (bitmap == null)
		    	Log.d("onSensorChanged", "failed opening image file");
	      //storeImage(bitmap);
	    
	    
	    } catch (Exception e) {
	    	Log.d("onSensorChanged", "failed opening image file");
	      e.printStackTrace();
	    }

	
	
	
	temp1 = bitmap.copy(Bitmap.Config.RGB_565, true);
	
	
	for (int y = 0; y < bitmap.getWidth(); y++) {
		for (int x = 0; x < bitmap.getHeight(); x++) {
				if(temp1.getPixel(x, y)==Color.RED){
						temp1.setPixel(x, y, primo);
					}
				else if(temp1.getPixel(x, y)==Color.BLUE){
						temp1.setPixel(x, y, secondo);
					}
				else if(temp1.getPixel(x, y)==Color.GREEN){
						temp1.setPixel(x, y, terzo);
					}
				else if(temp1.getPixel(x, y)==Color.YELLOW){
						temp1.setPixel(x, y, quarto);
					}
				else if(temp1.getPixel(x, y)==Color.BLACK){
						temp1.setPixel(x, y, quinto);
				}
				else temp1.setPixel(x, y, sesto);
		}//fine for
	}//fine for
	
	storeImage(temp1);
	}
	
	
	
	private void storeImage(Bitmap image) {
		File pictureFile = new File(filepath, "Immy.jpg");
		
		try {
		FileOutputStream fos = new FileOutputStream(pictureFile);
		if (!image.compress(Bitmap.CompressFormat.JPEG, 100, fos))
			Log.d("storeImage", "error compressing file");
		fos.close();
		}
		
		catch (IOException e) {
		
		}
		}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.immagine, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_immagine,
					container, false);
			return rootView;
		}
	}

}



///* importato da casa
// private SensorManager manager;
//private Sensor accelerometro;
//private FileWriter writer;
//
//String filepath = "/sdcard/AranciaGranate";
//
//int a = 255;
//Bitmap temp1;
//int x,y,z, primo, secondo, terzo;
//Bitmap bmp =BitmapFactory.decodeResource(getResources(), R.drawable.basefortp);
//private TextView testo;
//
//
//public void onAccuracyChanged(Sensor sensor, int accuracy) {
////questo non ci serve quindi lo lasciamo vuoto
//}
//
//
////ecco il nostro bad boy
//public void onSensorChanged(SensorEvent event) {
//
//
//
//testo = (TextView)findViewById(R.id.bitmap);
//
//x = (int)Math.abs(event.values[0]);
//y = (int)Math.abs(event.values[1]);
//z = (int)Math.abs(event.values[2]);
//x= (int)((x*100)%255);
//y= (int)((y*100)%255);
//z= (int)((z*100)%255);
//
//
//
//try {
//writer.write(x+", "+y+", "+z+"\n");
//}
//catch (IOException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//}
//
//testo.setText("Campioni presi 1");
//
//this.onPause();
//
//
//}
//
//
//protected void onPause() {
//super.onPause();
//manager.unregisterListener(this);
//testo = (TextView)findViewById(R.id.bitmap);
//testo.setText("Fatto");
//
//if(writer != null) {
//try {
//writer.close();
//}
//catch (IOException e) { // TODO Auto-generated catch block
//e.printStackTrace();
//}
//}//fine if
//
//Color col1 = new Color();
//primo = col1.argb(255, x, y, z);
//Color col2 = new Color();
//secondo = col2.argb(255, y, x, z);
//Color col3 = new Color();
//terzo = col3.argb(255, z, x, y);
//
//
//
//
//
//
//// temp1 = bmp.copy(Bitmap.Config.RGB_565, true);
////bmp.copyPixelsToBuffer(pixels);
////temp1.copyPixelsFromBuffer(pixels);
///*
//for (int y = 0; y < bmp.getWidth(); y++) {
//for (int x = 0; x < bmp.getHeight(); x++) {
//if(temp1.getPixel(x, y)==Color.RED){
//temp1.setPixel(x, y, primo);
//}
//else if(temp1.getPixel(x, y)==Color.BLUE){
//temp1.setPixel(x, y, secondo);
//}
//else if(temp1.getPixel(x, y)==Color.GREEN){
//temp1.setPixel(x, y, terzo);
//}
//}//fine for
//}//fine for
//*/
//
//
//
//
//}
//
//

//
//
//
//
//protected void onResume() {
//super.onResume();
//manager.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_NORMAL);
//}
//
//
//
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//super.onCreate(savedInstanceState);
//setContentView(R.layout.activity_crea_bitmap);
//
//manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//accelerometro = (Sensor) manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//File folder = new File(filepath);
//boolean success = true;
//if (!folder.exists()) {
//Toast.makeText(CreaBitmap.this, "Directory Does Not Exist, I Create It", Toast.LENGTH_SHORT).show();
//success = folder.mkdir();
//}
//
//
//try {
//writer = new FileWriter(new File(filepath, "AranciaGranate.txt"), true);
//} catch (IOException e) {
//// TODO Auto-generated catch block
//e.printStackTrace();
//}
//
//
//
//
//
//if (savedInstanceState == null) {
//getSupportFragmentManager().beginTransaction()
//.add(R.id.container, new PlaceholderFragment()).commit();
//}
//}
//
//
///*
//int[] pixels = new int[myBitmap.getHeight()*myBitmap.getWidth()];
//myBitmap.getPixels(pixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
//
//for(int i =0; i<500;i++){
////Log.e(TAG, "pixel"+i +pixels[i]);
//*
//*
//*
//*
//import android.graphics.Color;
//
//int[] pixels = new int[myBitmap.getHeight()*myBitmap.getWidth()];
//myBitmap.getPixels(pixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
//for (int i=0; i<myBitmap.getWidth()*5; i++)
//pixels[i] = Color.BLUE;
//myBitmap.setPixels(pixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
//*/
//
//
//
//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//
//// Inflate the menu; this adds items to the action bar if it is present.
//getMenuInflater().inflate(R.menu.crea_bitmap, menu);
//return true;
//}
//
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
//// Handle action bar item clicks here. The action bar will
//// automatically handle clicks on the Home/Up button, so long
//// as you specify a parent activity in AndroidManifest.xml.
//int id = item.getItemId();
//if (id == R.id.action_settings) {
//return true;
//}
//return super.onOptionsItemSelected(item);
//}
//
///**
//* A placeholder fragment containing a simple view.
//*/
//public static class PlaceholderFragment extends Fragment {
//
//public PlaceholderFragment() {
//}
//
//@Override
//public View onCreateView(LayoutInflater inflater, ViewGroup container,
//Bundle savedInstanceState) {
//View rootView = inflater.inflate(R.layout.fragment_crea_bitmap,
//container, false);
//return rootView;
//}
//}
//
//} 
// 
// */
//
//
