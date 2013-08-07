package com.cm.clamztidalprediction;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.TextView;

public class TidalPrediction extends GridLayout{

		
		TextView _title;
		TextView _tide;
		TextView _time;
		TextView _text;
		Context _context;
		
		
		public TidalPrediction(Context context){
			super(context);
			
			//assigning a value
			_context = context;
			
			//columns/rows  1 for labels and one for values
			this.setColumnCount(2);
			
			TextView locLabel = new TextView(_context);
			locLabel.setText("Location: ");
			_title = new TextView(_context);
			
			TextView timeLabel = new TextView(_context);
			timeLabel.setText("Time:");
			_time = new TextView(_context);
			
			TextView preLabel = new TextView(_context);
			preLabel.setText("Prediction: ");
			_tide = new TextView(_context);
			
			TextView textLabel = new TextView(_context);
			textLabel.setText("Clamming: ");
			_text = new TextView(_context);
			
			
			//add views to display
			this.addView(locLabel);
			this.addView(_title);
			this.addView(timeLabel);
			this.addView(_time);
			this.addView(preLabel);
			this.addView(_tide);
			this.addView(textLabel);
			this.addView(_text);
			
			
			
			
		}
	}

