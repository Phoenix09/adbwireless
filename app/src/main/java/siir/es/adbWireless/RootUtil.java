package siir.es.adbWireless;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import eu.chainfire.libsuperuser.Shell;

public class RootUtil {
    public class Exec extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... commands) {
            if (Shell.SU.available()) {
                Shell.SU.run(commands);
            }
            return null;
        }
    }

    public class CheckRoot extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog = null;
        private Activity activity;
        private boolean suAvailable = false;

        public CheckRoot (Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(activity);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Checking for root access...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            suAvailable = Shell.SU.available();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            if (!suAvailable) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(activity.getString(R.string.no_root)).setCancelable(true)
                        .setPositiveButton(activity.getString(R.string.button_close), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activity.finish();
                            }
                        });
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.create();
                builder.setTitle(R.string.no_root_title);
                builder.show();
            }
        }
    }
}