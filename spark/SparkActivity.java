package spark;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public abstract class SparkActivity extends Activity {
	protected SparkSurface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		surface = new SparkSurface(this);
		setContentView(surface);
		onInit();
    }

	public abstract void onInit();

	@Override
	protected void onResume() {
		super.onResume();

		getWindow().getDecorView().setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_FULLSCREEN |
			View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
			View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
		);

		surface.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		surface.onPause();
	}
}
