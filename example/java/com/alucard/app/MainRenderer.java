package com.alucard.app;

import spark.*;
import android.graphics.*;

public class MainRenderer extends SparkCallback {
	@Override
	public void onStart() {
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFFFFFFF);
	}
}
