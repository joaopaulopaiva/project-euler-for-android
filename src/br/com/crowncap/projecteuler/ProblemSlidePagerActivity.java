package br.com.crowncap.projecteuler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ProblemSlidePagerActivity extends ActionBarActivity {

    private int numPages;
    private int currentPage;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_slide_pager);
        
        numPages = getIntent().getIntExtra("NUMBER_OF_PROBLEMS", 1);
        currentPage = getIntent().getIntExtra("PROBLEM_ID", 0);
        
        setTitle("Problem " + (currentPage + 1));
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        
//        this.deleteDatabase("problems.db");
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ProblemSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(5);
        mPager.setCurrentItem(currentPage);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	setTitle("Problem " + (mPager.getCurrentItem() + 1));
            	supportInvalidateOptionsMenu();
            	if (mPager.getCurrentItem() + 6 == numPages) {
            		numPages += 5;
            		mPagerAdapter.notifyDataSetChanged();
            	}
            }
        });
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello");
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.problem_slide_pager, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
	    switch (id) {
	    case R.id.action_settings:
	        return true;
	    case R.id.action_refresh:
	        return false;
	    default:
	        break;
	    }
		
		return super.onOptionsItemSelected(item);
	}

//    mPager.setCurrentItem(mPager.getCurrentItem() - 1);

    private class ProblemSlidePagerAdapter extends FragmentStatePagerAdapter {
    	
    	
        public ProblemSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	return ProblemSlidePageFragment.create(position + 1);

        }

        @Override
        public int getCount() {
            return numPages;
        }

    }
}