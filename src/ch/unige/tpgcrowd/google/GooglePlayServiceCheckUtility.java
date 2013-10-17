package ch.unige.tpgcrowd.google;

import android.app.Dialog;
import android.content.IntentSender.SendIntentException;
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
    public final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    private static FragmentActivity mainActivity;
    
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
    
    public static boolean servicesConnected(final FragmentActivity activity) {
        // Check that Google Play services is available
    	mainActivity = activity;
        final int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(mainActivity);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Geofence Detection",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            generateErrorDialog(resultCode);
            return false;
        }
    }
    
    public static void generateErrorDialog(final int errorCode) {
    	// Get the error dialog from Google Play services
        final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                mainActivity,
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
            		mainActivity.getSupportFragmentManager(),
                    "Location/Activity Service");
        }
    }
    
    public static void tryResolutionOnConnectionError(final ConnectionResult connectionResult) {
    	try {
            connectionResult.startResolutionForResult(
            		mainActivity,
                    GooglePlayServiceCheckUtility.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        } catch (final SendIntentException e) {
            // Log the error
            e.printStackTrace();
        }
    }
}
