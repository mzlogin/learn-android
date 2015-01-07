package org.mazhuang.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DateOrTimeFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.date_or_time)
			.setPositiveButton(R.string.date_picker, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((CrimeFragment)getTargetFragment()).pickDate();
						}
					})
			.setNegativeButton(R.string.time_picker, 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((CrimeFragment)getTargetFragment()).pickTime();
						}
					})
			.create();
	}
}
