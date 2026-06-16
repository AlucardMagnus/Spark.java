package com.alucard.app;

import spark.SparkActivity;

public class MainActivity extends SparkActivity {
	@Override
	public void onInit() {
		surface.addCallback(new MainRenderer());
	}
}
