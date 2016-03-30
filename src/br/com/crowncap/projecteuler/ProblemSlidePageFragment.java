package br.com.crowncap.projecteuler;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProblemSlidePageFragment extends Fragment {

	public static final String PROB_NUMBER = "problem";
	private static final String USER_AGENT = "Mozilla/5.0";
	private GetProblemTask task;
	private ProblemDatabaseHandler dbHandler;
	private Problem problem;

	private int mProblemNumber;

	public static ProblemSlidePageFragment create(int pageNumber) {
		ProblemSlidePageFragment fragment = new ProblemSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(PROB_NUMBER, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ProblemSlidePageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mProblemNumber = getArguments().getInt(PROB_NUMBER);
		dbHandler = new ProblemDatabaseHandler(getActivity().getApplicationContext(), null, null, 1);
		problem = new Problem();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return false;
		case R.id.action_refresh:
			task = new GetProblemTask("https://projecteuler.net/problem=" + mProblemNumber);
			task.execute();
			return true;
		default:
			break;
		}

		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (task != null) {
			task.cancel(true);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		findProblem(mProblemNumber);
		if (problem == null || "".equals(problem.getInfo())) {
			task = new GetProblemTask("https://projecteuler.net/problem=" + mProblemNumber);
			task.execute();
		} else {
			updateView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_problem_slide_page, container, false);

		return rootView;
	}

	private void updateView() {
		View view = getView();
		if (view != null) {
			((TextView)view.findViewById(R.id.problem_title)).setText(problem.getTitle());
			((TextView)view.findViewById(R.id.problem_info)).setText(problem.getInfo());
			((TextView)view.findViewById(R.id.problem_text)).setText(Html.fromHtml(problem.getText()));
		}
	}

	class GetProblemTask extends AsyncTask<Void, Void, ArrayList<String>> {

		String url;
		ProgressDialog progDailog = new ProgressDialog(getActivity());

		public GetProblemTask(String url) {
			super();
			this.url = url;
		}

		@Override
		protected ArrayList<String> doInBackground(Void... nothing) {

			String title, info, text;
			ArrayList<String> content = new ArrayList<String>();

			try {
				Document doc = Jsoup.connect(url)
						.userAgent(USER_AGENT)
						.validateTLSCertificates(false)
						.timeout(10000)
						.get();
				title = doc.getElementsByTag("h2").first().text();
				info = doc.getElementById("problem_info").child(1).text().split(";", 2)[1].trim();
				text = doc.getElementsByClass("problem_content").first().toString();
				content.add(title);
				content.add(info);
				content.add(text);
				
			} catch (Exception e) {
				content.add("Could not load problem");
				content.add("");
				content.add("Refresh to try again.");
			}
			return content;

		}

		@Override
		protected void onPostExecute(ArrayList<String> content) {
			if (content.size() == 3) {

				String title = content.get(0);
				String info = content.get(1);
				String text = content.get(2);

				if (problem == null) {
					addProblem(mProblemNumber, title, info, text);
				} else {
					if (!"".equals(info)) {
						updateProblem(mProblemNumber, title, info, text);
						Toast.makeText(getActivity(), "Problem updated.", Toast.LENGTH_SHORT).show();
					}
				}
				updateView();
			}
		}
	}

	public void addProblem(int id, String title, String info, String text) {

		problem = new Problem(id, title, info, text);
		dbHandler.addProblem(problem);

	}

	public void updateProblem(int id, String title, String info, String text) {

		problem = new Problem(id, title, info, text);
		dbHandler.updateProblem(problem);

	}

	public void findProblem(int id) {

		problem = dbHandler.findProblem(id);

	}

	public boolean deleteProblem(int id) {

		boolean result = dbHandler.deleteProblem(id); 

		return result;
	}

}
