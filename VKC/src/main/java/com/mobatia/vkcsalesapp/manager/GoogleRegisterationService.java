/*
package com.mobatia.vkcsalesapp.manager;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GoogleRegisterationService extends Service {
    Context context;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        context = this;
		*/
/*GetPatientListInBackgroundApi api=new  GetPatientListInBackgroundApi();
		api.execute();*//*


        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
	*/
/*System.out.println("come on startcomand");
	patientListInBackground = new PatientListInBackground();
	patientListInBackground.getPatientList( context, PATIENT_LIST_URL, AppPreferenceManager.getEndindex(context));*//*

        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.v("LOG", "FLOW!" + "GCM SERVICE");
                GoogleRegisteration googleRegisteration = new GoogleRegisteration(context);
                googleRegisteration.registerBackground();
            }
        }.start();
	*/
/*((Activity) context).runOnUiThread(new Runnable() {
    @Override
    public void run() {
		System.out.println("come on startcomand");
		
		patientListInBackground = new PatientListInBackground();
		patientListInBackground.getPatientList( context, PATIENT_LIST_URL, AppPreferenceManager.getEndindex(context));

    }
});*//*


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
*/
