package ch.unige.tpgcrowd.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ch.unige.tpgcrowd.R;

public class AboutDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.about_text);
		builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                dismiss();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
