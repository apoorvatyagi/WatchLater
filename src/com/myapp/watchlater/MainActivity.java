package com.myapp.watchlater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private ProgressBar pbMovieSearch;
	private Button searchButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pbMovieSearch = (ProgressBar) findViewById(R.id.progressBarMovieList);
		searchButton = (Button) findViewById(R.id.buttonSearch);
		
		final EditText movieName = (EditText) findViewById(R.id.textSearch);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startSecondPage();
				String movieNameSearch = movieName.getText().toString();
				new LoadMovieList().execute(movieNameSearch);
			}
		});
		
	}
	
	public void startSecondPage(ArrayList<String> movieList)
	{
		Intent nextPage = new Intent(MainActivity.this, DisplayMovies.class);
		nextPage.putStringArrayListExtra("movieSearchList", movieList);
		MainActivity.this.startActivity(nextPage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	class LoadMovieList extends AsyncTask<String, Integer, ArrayList<String>> {
		 

		@Override
		protected void onPreExecute() {
			pbMovieSearch.setVisibility(ProgressBar.VISIBLE);
		}

		
		@Override
		protected void onProgressUpdate(Integer... values) {
			pbMovieSearch.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			pbMovieSearch.setVisibility(ProgressBar.INVISIBLE);
			startSecondPage(result);
		}

		

		@Override
		protected ArrayList<String> doInBackground(String... movieName) {
			// TODO Auto-generated method stub
			
			String rottenAPIkey = "zefw49tuactgfvuggxwj552m";
			String movieTitle = movieName[0];
			String responseRottenJson="";
			String tokenisedMovie = "";
			
			StringTokenizer st = new StringTokenizer(movieTitle);
			tokenisedMovie=st.nextToken();
		     while (st.hasMoreTokens()) {
		         tokenisedMovie+="+"+st.nextToken();
		     }
		    System.out.println("Search query:"+tokenisedMovie); 
		     
			ArrayList<String> searchMovies = new ArrayList<String>();
			String stringUrl="http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="+rottenAPIkey+"&q="+tokenisedMovie;
			try {
				URL movieURL = new URL(stringUrl);
				
			    BufferedReader in = new BufferedReader(new InputStreamReader(movieURL.openStream()));

		        String inputLine;
		        while ((inputLine = in.readLine()) != null)
		            responseRottenJson+=inputLine;
		        in.close();
		        
		        getFieldString(responseRottenJson, "title", searchMovies);
			      
		        //Print list of movies returned by rotten tomatoes
		        for(int i=0;i<searchMovies.size();i++)
		        	System.out.println(searchMovies.get(i));
		        
		        
		        
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        return searchMovies;

		}
		
		public void getFieldString(String jsonString, String field, ArrayList<String> searchMovies)
		{
			int startIndex = 0;
			int occurance=0;
			String movieTitle="";
			
			while(true)
			{
				occurance = jsonString.indexOf(field, startIndex);

				if(occurance!=-1)
				{
					//System.out.println(occurance);
			
					int i=occurance+8;
					while(jsonString.charAt(i)!='"')
						movieTitle += jsonString.charAt(i++);
					startIndex=i;
					searchMovies.add(movieTitle);
					//System.out.println(movieTitle);

					movieTitle="";
					
				}
				else break;
			}
			
			
		}
	}

}

