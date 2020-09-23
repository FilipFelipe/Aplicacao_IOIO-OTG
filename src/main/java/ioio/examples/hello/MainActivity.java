package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIO.VersionType;
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
import android.widget.Toast;


public class MainActivity extends IOIOActivity {
	private ImageButton button_frente;
	private ImageButton button_tras;
	private ImageButton button_esquerda;
	private ImageButton button_direita;

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

		button_frente.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					msg_tela("Andando..");
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					msg_tela("Parando..");
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

					msg_tela("Andando para tras..");
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					msg_tela("Parando..");
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

					msg_tela("Andando para direita..");
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					msg_tela("Parando..");
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

					msg_tela("Andando para esquerda..");
					return  true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					msg_tela("Parando..");
					return true;
				}else{
					return  false;
				}
			}
		});
	}
	//exibir msg na tela do APP
	public void msg_tela(String mensagem) {
		Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
	}






	class Looper extends BaseIOIOLooper {

		private DigitalOutput led_;


		@Override
		protected void setup() throws ConnectionLostException {
			showVersions(ioio_, "IOIO connected!");
			led_ = ioio_.openDigitalOutput(0, true);
			enableUi(true);
		}

		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			//led_.write(!button_.isChecked());
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

	private int numConnected_ = 0;

	private void enableUi(final boolean enable) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (enable) {
					if (numConnected_++ == 0) {
						//button_.setEnabled(true);
					}
				} else {
					if (--numConnected_ == 0) {
						//button_.setEnabled(false);
					}
				}
			}
		});
	}
}