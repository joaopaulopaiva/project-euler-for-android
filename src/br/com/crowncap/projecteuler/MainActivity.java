package br.com.crowncap.projecteuler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void signInAction(View view) throws Exception {
		
	}
	
	public void showProblemsAction(View view) throws Exception {
		Intent intent = new Intent(getApplicationContext(), ShowProblemsActivity.class);
		startActivity(intent);
	}
	
}
