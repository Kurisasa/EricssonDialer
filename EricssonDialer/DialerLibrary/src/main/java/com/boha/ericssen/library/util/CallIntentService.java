package com.boha.ericssen.library.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CallIntentService extends IntentService {

    public static final String EXTRA_NUMBER = "com.boha.ericssen.library.util.extra.NUMBER";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startCallService(Context context, String number) {
        Intent intent = new Intent(context, CallIntentService.class);
        intent.putExtra(EXTRA_NUMBER, number);
        context.startService(intent);
    }

    public CallIntentService() {
        super("CallIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String number = intent.getStringExtra(EXTRA_NUMBER);
            String uri = "tel:" + number;
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.w("CallIntentService", "###### calling: " + uri);
            startActivity(callIntent);
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
