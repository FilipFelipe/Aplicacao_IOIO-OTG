package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIO.VersionType;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends IOIOActivity {
	//Variáveis
	private ImageButton button_frente;
	private ImageButton button_tras;
	private ImageButton button_esquerda;
	private ImageButton button_direita;
	private SeekBar barra ;
	private TextView texto_barra;
	//Valores do estado
	private Boolean bool_esquerda=false;
	private Boolean bool_direita=false;
	private Boolean bool_frente=false;
	private Boolean bool_tras=false;
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//botoes
		button_frente = (ImageButton) findViewById(R.id.botao_frente);
		button_tras = (ImageButton) findViewById(R.id.botao_tras);
		button_esquerda = (ImageButton) findViewById(R.id.botao_esquerda);
		button_direita = (ImageButton) findViewById(R.id.botao_direita);
		barra = (SeekBar) findViewById(R.id.barra);
		texto_barra= (TextView) findViewById(R.id.texto_barra);

		//Carrega valor do SeekBar
		texto_barra.setText(barra.getProgress() + "%");

		button_frente.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					toast("Andando.."+ barra.getProgress() + "%");
					bool_frente=true;
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					toast("Parando..");
					bool_frente=false;
					return true;
				}
				else{
					return  false;
				}
			}
		});
		button_tras.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					toast("Andando para tras.."+ barra.getProgress() + "%");
					bool_tras=true;
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					toast("Parando..");
					bool_tras=false;
					return true;
				}else{
					return  false;
				}
			}
		});
		button_direita.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					toast("Andando para direita.."+ barra.getProgress() + "%");
					bool_tras=true;
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					toast("Parando..");
					bool_tras=false;
					return true;
				}else{
					return  false;
				}
			}
		});
		button_esquerda.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					toast("Andando para esquerda.."+ barra.getProgress() + "%");
					bool_esquerda=true;
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					toast("Parando.." );
					bool_esquerda=false;
					return true;
				}else{
					return  false;
				}
			}
		});
		barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				texto_barra.setText(String.valueOf(barra.getProgress()+"%"));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				texto_barra.setText(String.valueOf(barra.getProgress()+"%"));
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				texto_barra.setText(String.valueOf(barra.getProgress()+"%"));
			}
		});
	}

	class Looper extends BaseIOIOLooper {
		private DigitalOutput led_;
		//Velocidade PWM
		private PwmOutput motor_1;
		private PwmOutput motor_2;
		//Seletor
		private DigitalOutput s1_motor1;
		private DigitalOutput s2_motor1;
		private DigitalOutput s1_motor2;
		private DigitalOutput s2_motor2;
		
		@Override
		protected void setup() throws ConnectionLostException {
			showVersions(ioio_, "IOIO connected!");
			enableUi(true);
			// Ponte H L298N
			s1_motor1 = ioio_.openDigitalOutput(10, false);
			s2_motor1 = ioio_.openDigitalOutput(11, false);
			s1_motor2 = ioio_.openDigitalOutput(12, false);
			s2_motor2 = ioio_.openDigitalOutput(13, false);

			motor_1 = ioio_.openPwmOutput(1, 100); // direita
			motor_2 = ioio_.openPwmOutput(2, 100); // esquerda

		}

		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			if(bool_frente) {
				s1_motor1.write(true);
				s2_motor1.write(false);
				s1_motor2.write(true);
				s2_motor2.write(false);
				motor_1.setDutyCycle(barra.getProgress()/100);
				motor_2.setDutyCycle(barra.getProgress()/100);
			} else if (bool_tras) {
				s1_motor1.write(false);
				s2_motor1.write(true);
				s1_motor2.write(false);
				s2_motor2.write(true);
				motor_1.setDutyCycle(barra.getProgress()/100);
				motor_2.setDutyCycle(barra.getProgress()/100);
			} else if (bool_esquerda) {
				s1_motor1.write(true);
				s2_motor1.write(false);
				s1_motor2.write(true);
				s2_motor2.write(false);
				motor_1.setDutyCycle(barra.getProgress()/100);
				motor_2.setDutyCycle(barra.getProgress()/200);
			} else if (bool_direita) {
				s1_motor1.write(true);
				s2_motor1.write(false);
				s1_motor2.write(true);
				s2_motor2.write(false);
				motor_2.setDutyCycle(barra.getProgress()/100);
				motor_1.setDutyCycle(barra.getProgress()/200);
			} else {
				s1_motor1.write(false);
				s2_motor1.write(false);
				s1_motor2.write(false);
				s2_motor2.write(false);
				motor_2.setDutyCycle(0);
				motor_1.setDutyCycle(0);
			}
			Thread.sleep(100);
		}
		@Override
		public void disconnected() {
			enableUi(false);
			toast("IOIO disconnected");
		}

		@Override
		public void incompatible() {
			showVersions(ioio_, "Incompatible firmware version!");
		}
}
	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}

	private void showVersions(IOIO ioio, String title) {
		toast(String.format("%s\n" +
				"IOIOLib: %s\n" +
				"Application firmware: %s\n" +
				"Bootloader firmware: %s\n" +
				"Hardware: %s",
				title,
				ioio.getImplVersion(VersionType.IOIOLIB_VER),
				ioio.getImplVersion(VersionType.APP_FIRMWARE_VER),
				ioio.getImplVersion(VersionType.BOOTLOADER_VER),
				ioio.getImplVersion(VersionType.HARDWARE_VER)));
	}

	private void toast(final String message) {
		final Context context = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void enableUi(final boolean enable) {
	}

}