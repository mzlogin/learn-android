package org.mazhuang.android.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	public static final String EXTRA_CRIME_ID = "org.mazhuang.android.criminalintent.crime_id";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final String DIALOG_DATE_OR_TIME = "date_or_time";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private static final int REQUEST_DATE_OR_TIME = 2;
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
		
		setHasOptionsMenu(true);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start,
					int before, int count) {
				mCrime.setTitle(c.toString());
			}
			
			public void beforeTextChanged(CharSequence c, int start,
					int before, int count) {
				
			}
			
			public void afterTextChanged(Editable c) {
				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DateOrTimeFragment dialog = new DateOrTimeFragment();
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE_OR_TIME);
				dialog.show(fm, DIALOG_DATE_OR_TIME);
			}
		});
		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
			}
		});
		
		return v;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK){
			return;
		}
		
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		} else if (requestCode == REQUEST_TIME) {
			Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}
	}
	
	public void pickDate() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
		dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
		dialog.show(fm, DIALOG_DATE);
	}
	
	public void pickTime() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
		dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
		dialog.show(fm, DIALOG_TIME);
	}
	
	private void updateDate() {
		String format = new String("EEEE, MMM dd, yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		mDateButton.setText(sdf.format(mCrime.getDate()));
	}
	
	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
}
