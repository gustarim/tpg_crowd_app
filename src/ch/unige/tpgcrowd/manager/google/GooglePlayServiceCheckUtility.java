package ch.unige.tpgcrowd.manager.google;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public final class GooglePlayServiceCheckUtility {
	// Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        
        // Set the dialog to display
        public void setDialog(final Dialog dialog) {
            mDialog = dialog;
        }
        
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    public static void toCallInActivityOnActivityResult(final int requestCode, 
    		final int resultCode, final Intent data, final FragmentActivity activity) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    servicesConnected(activity);
                    break;
                }
        }
    }
    
    public static boolean servicesConnected(final FragmentActivity activity) {
        // Check that Google Play services is available
        final int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(activity);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Geofence Detection",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error code
            //final int errorCode = ConnectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                   // errorCode,
            		resultCode,
                    activity,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                final ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(
                        activity.getSupportFragmentManager(),
                        "Location/Activity Service");
            }
            return false;
        }
    }
}
