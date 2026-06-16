package spark;

import android.content.Context;
import android.graphics.Canvas;

public abstract class SparkCallback {
	protected int width, height;
	protected Context context;

	public void onStart() {}
	public void onResize(int w, int h) {}
	public void onUpdate(float dt) {}
	public void onDraw(Canvas canvas) {}
	public void onEnd() {}
}
