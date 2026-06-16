package spark;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import java.util.Arrays;
import android.graphics.Canvas;

public class SparkSurface extends SurfaceView {
	private Runnable run = new Runnable() {
		@Override
		public void run() {
			long prev = System.nanoTime();
			long curr;
			float dt;

			for (SparkCallback callback : callbacks)
				callback.onStart();

			while (running) {
				if (holder.getSurface().isValid()) {
					Canvas c = holder.lockCanvas();
					for (SparkCallback callback : callbacks)
						callback.onDraw(c);
					holder.unlockCanvasAndPost(c);
				}

				curr = System.nanoTime();
				dt = (curr - prev) / 1_000_000_000f;
				for (SparkCallback callback : callbacks)
					callback.onUpdate(dt);
				prev = curr;

				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			for (SparkCallback callback : callbacks)
				callback.onEnd();
		}
	};

	private int width, height;
	private SurfaceHolder holder;
	private volatile SparkCallback[] callbacks;

	private Thread mainThread;
	private boolean running;

	public SparkSurface(Context context) {
		super(context);
		init();
	}

	public SparkSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		DisplayMetrics dm = new DisplayMetrics();
		getContext().getDisplay().getRealMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;

		holder = getHolder();
		callbacks = new SparkCallback[0];
	}

	public void addCallback(SparkCallback callback) {
		callbacks = Arrays.copyOf(callbacks, callbacks.length + 1);
		callbacks[callbacks.length - 1] = callback;

		callback.width = width;
		callback.height = height;
		callback.context = getContext();

		if (running)
			callback.onStart();
	}

	public boolean removeCallback(SparkCallback callback) {
		int index = -1;
		int n = callbacks.length;

		for (int i = 0; i < n; i++)
			if (callbacks[i] == callback) {
				index = i;
				break;
			}

		if (index == -1) return false;
		SparkCallback[] next = new SparkCallback[n - 1];

		for (int i = 0, j = 0; i < n; i++) {
			if (i != index)
				next[j++] = callbacks[i];
		}

		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		for (SparkCallback callback : callbacks)
			callback.onResize(w, h);

		width = w;
		height = h;
	}

	public void onResume() {
		running = true;
		mainThread = new Thread(run);
		mainThread.start();
	}

	public void onPause() {
		running = false;
		try {
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
