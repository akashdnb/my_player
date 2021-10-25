package com.a9934527599.myplayer;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;
import android.view.WindowManager;

import com.a9934527599.myplayer.VideoPlayerActivity;

public class rotationListenerHelper {
    private int lastRotation;
    private WindowManager windowManager;
    private OrientationEventListener orientationEventListener;

    private VideoPlayerActivity.rotationCallbackFn callback;

    public rotationListenerHelper() {
    }

    public void listen(Context context, VideoPlayerActivity.rotationCallbackFn callback){
        stop();
        context= context.getApplicationContext();
         this.callback=callback;
         this.windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
         this.orientationEventListener= new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
             @Override
             public void onOrientationChanged(int orientation) {
                 WindowManager windowManager1=windowManager;
                 VideoPlayerActivity.rotationCallbackFn rotationCallbackFn1= callback;
                 if (windowManager1!=null&& rotationCallbackFn1!=null){
                     int newRtn=windowManager1.getDefaultDisplay().getRotation();
                    if (newRtn != lastRotation){
                        rotationCallbackFn1.onRtationChanged(lastRotation,newRtn);
                        lastRotation= newRtn;
                    }
                 }
             }
         };
         this.orientationEventListener.enable();
         lastRotation= windowManager.getDefaultDisplay().getRotation();

    }

    private void stop() {
        if (this.orientationEventListener!= null){
            this.orientationEventListener.disable();
        }
        this.orientationEventListener=null;
         this.windowManager=null;
         this.callback=null;
    }
}
