package br.com.crowncap.projecteuler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ShowProblemsActivity extends ActionBarActivity {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private GridView gridView;
	private GetNumberOfProblemsTask task;
	private String numberOfProblems;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_problems);
	    
		sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	    numberOfProblems = sharedPreferences.getString("number_of_problems", "");
	    
	    if ("".equals(numberOfProblems)) {
			new GetNumberOfProblemsTask("https://projecteuler.net/recent").execute();
	    } else {
	    	createGrid();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.show_problems, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
	    switch (id) {
	    case R.id.action_settings:
	        return true;
	    case R.id.action_refresh:
			task = new GetNumberOfProblemsTask("https://projecteuler.net/recent");
			task.execute();
	        return true;
	    case R.id.action_scroll_to_top:
			gridView.setSelection(0);
	        return true;
	    case R.id.action_scroll_to_bottom:
			gridView.setSelection(Integer.parseInt(numberOfProblems) - 1);
	        return true;
	    default:
	        break;
	    }
		
		return super.onOptionsItemSelected(item);
	}
	
	class GetNumberOfProblemsTask extends AsyncTask<Void, Void, String> {

		String url;

		public GetNumberOfProblemsTask(String url) {
			super();
			this.url = url;
		}
		
		@Override
		protected String doInBackground(Void... nothing) {

			try {
				Document doc = Jsoup.connect(url)
						.userAgent(USER_AGENT)
						.validateTLSCertificates(false)
						.timeout(10000)
						.get();
				numberOfProblems = doc.getElementsByClass("id_column").get(1).text();
			} catch (Exception e) {
				numberOfProblems = "";
			}
			return numberOfProblems;

		}

		@Override
		protected void onPostExecute(String numberOfProblemns) {
			if ("".equals(numberOfProblemns)) {
				Toast.makeText(getApplicationContext(), "Could not load problems. Refresh to try again.", Toast.LENGTH_LONG).show();
			} else {
				sharedPreferences = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("number_of_problems", numberOfProblemns);
				editor.commit();
				createGrid();
			}
		}

	}
	
	private void createGrid() {
		
		int n = Integer.parseInt(numberOfProblems);
		
		gridView = (GridView) findViewById(R.id.problems_grid);
		
        String[] numbers = new String[n];

        for(int i = 0; i < n; i++){
            numbers[i] = String.valueOf(i + 1);
        }

        gridView.setAdapter(new TextViewAdapter(this, numbers));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ProblemSlidePagerActivity.class);
				intent.putExtra("NUMBER_OF_PROBLEMS", Integer.parseInt(numberOfProblems));
				intent.putExtra("PROBLEM_ID", position);
				startActivity(intent);
			}
		});
		
	}

}
