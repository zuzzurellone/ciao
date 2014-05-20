package app.test.testaudio;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
	
	private SensorManager manager; 
	private Sensor accelerometro; 
	//fare un test cambiando tutti i valori in protected 
	//variabili per accelerometro 
	public boolean interruttore; 
	public float x, y, z; 
	//variabili che c'erano nel programma che "fa il fischio" alcune le ho usate, altre no 
	/* 
	 * private final int duration = 3; // seconds 
	 * private final int sampleRate = 8000; 
	 * private final int numSamples = duration * sampleRate; 
	 * private final double sample[] = new double[numSamples]; 
	 * private final double freqOfTone = 440; // hz 
	 * private final byte generatedSnd[] = new byte[2 * numSamples]; 
	 * Handler handler = new Handler(); */ 
	
	public int numSamples=100; 
	private double sample[] = new double[numSamples]; 
	public double freqOfTone; 
	//udibili da 20 a 20K, riproducibili tramite cassa stimiamo un 20 - 1000 
	private byte generatedSnd[] = new byte[2 * numSamples]; 
	Handler handler = new Handler(); 
	
//	aggiorna textview
//	
//			public TextView prima= (TextView) findViewById(R.id.printx);
//			public TextView seconda= (TextView) findViewById(R.id.printy);
//			public TextView terza = (TextView) findViewById(R.id.printz);
				
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); 
		accelerometro = (Sensor) manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 
		manager.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_NORMAL); 

		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
		/*
		 * 
		 * 
		 * 
		 */
	public void onorevole() { 
		
		//attenzione provo a buttare dentro codice di stack 
		
		final Thread thread = new Thread(new Runnable() { 
			public void run() { 
				
				genTone(); 
				
				handler.post(new Runnable() {
		

	public void run() { 
		playSound(); 
		} 
	}); 
	} 
}); 

		thread.start(); 
	
		//fine inserimento
	}

protected void onPause() { 
	super.onPause(); 
	manager.unregisterListener(this); 

	} //fine 
/* 
 * 
 * 
 * 
 */
//metodi che mi generano la musica


void genTone(){ 
	// fill out the array 
	for (int i = 0; i < numSamples; ++i) { 
		
		sample[i] = Math.sin(2 * Math.PI * i / (8000/freqOfTone)); 
		}

//convert to 16 bit pcm sound array
// assumes the sample buffer is normalised.

int idx = 0; 
for (final double dVal : sample) { 
	// scale to maximum amplitude 
	final short val = (short) ((dVal * 32767)); 
	// in 16 bit wav PCM, first byte is the low order byte 
	generatedSnd[idx++] = (byte) (val & 0x00ff); 
	generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

	} 
}//fine gentone


void playSound(){ 
	
	
	final AudioTrack audioTrack;
	audioTrack= new AudioTrack(AudioManager.STREAM_MUSIC, 
			8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length, AudioTrack.MODE_STATIC); 
audioTrack.write(generatedSnd, 0, generatedSnd.length); 


if(audioTrack.getState()==AudioTrack.STATE_INITIALIZED){ 
	audioTrack.play(); 
	
	} 
else{ 	
	Intent intent = new Intent(getApplicationContext(), 
	MainActivity.class); 
	startActivity(intent); 
	finish(); 
}
audioTrack.flush();



}

/*
 * 
 * 
 * 
 * 
 * 
 */
	//metodi dell'accelerometro

//metodi di SensorEventListenerche devono essere implementati 

public void onAccuracyChanged(Sensor sensor, int accuracy) { 
	//questo non ci serve quindi lo lasciamo vuoto
	}
//ecco il nostro bad boy


public void onSensorChanged(SensorEvent event) { 
	int temp;
	
	
	if (interruttore==true){//ricorda:assolutamente no usare ciclo while 
		
		x = (int)((event.values[0])%30); 
		y = (int)((event.values[1])%30); 
		z = (int)((event.values[2])%30); 
		
		
						
		//la funzione di trasformazione dovrà essere raffinata 
		
		temp = (int)((20*x)+(50*y)+(80*z)); 
		freqOfTone = (double)(((temp))+20); 
		onorevole();
		
		
			
	}
}

//metodi associati ai bottoni
//non ho messo il metodo che mi aggiorna le textview mostrando i dati dell'accelerometro perchè in fondo è solo di bellezza 
public void fallo(View view){ 
	interruttore = true; 
	
	} 

public void ferma(View view){ 
	
	interruttore = false; 
	//resetta tutto
	byte temp[] = new byte[2 * numSamples]; 
	generatedSnd = temp;
	
	double temp2[] = new double[numSamples]; 
	sample = temp2;
	
	Handler franco = new Handler();
	handler=franco;


}

//public void aggiorna(){
//	
//	prima.setText(""+x);
//	seconda.setText(""+y);
//	terza.setText(""+z);
//
//}



//probabilmente devo anche azzerare le variabili in alto perchè mi fa crashare tutto quando lo premo 

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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
