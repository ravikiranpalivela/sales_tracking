package com.tekskills.st_tekskills.utils.services;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.MediaRecorder;
//import android.os.Bundle;
//import android.os.Environment;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.text.format.Time;
//import android.util.Log;
//import java.io.File;
//import java.io.IOException;
//
//public class OutgoinCalls extends BroadcastReceiver {
//    MediaRecorder recorder;
//    Time today;
//    String phoneNumber, ccno;
//    String selected_song_name;
//    public Context ctx;
//    private boolean isPhoneCalling = false;
//
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//        // Toast.makeText(context, "Outgoing call catched!", Toast.LENGTH_LONG).show();
//        //TODO: Handle outgoing call event here
//        Bundle extras = intent.getExtras();
//        phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//        if (extras != null) {
//            // OFFHOOK
//            Configuration.writetodatafile("MYBRODCAST CALL", context);
//            ctx = context;
//            String state = extras.getString(TelephonyManager.EXTRA_STATE);
//            if(state==null) {
//                //Outgoing call
//                String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//                ccno = number;
//                Log.v("@@tag","Outgoing number : "+number);
//            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                //Incoming call
//                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                ccno = number;
//                Log.v("@@tag","Incoming number : "+number);
//            }
//            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            telephony.listen(new PhoneStateListener() {
//                @Override
//                public void onCallStateChanged(int state, String incomingNumber) {
//                    super.onCallStateChanged(state, incomingNumber);
//                    switch (state) {
//                        case TelephonyManager.CALL_STATE_RINGING:
//                            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
//                                ccno = incomingNumber;
//                                Log.v("@@tag", "Incoming number1 : " + incomingNumber);
//                            }
//                            break;
//                        case TelephonyManager.CALL_STATE_OFFHOOK:
//                            Log.v("Received State : ", "Received");
//                            Configuration.writetoerrorfile6("CALL_STATE_OFFHOOK, ", ctx);
//                            try {
//                                startRecording(createDirIfNotExists("CallRec"/*+ccno*/));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            isPhoneCalling = true;
//                            break;
//                        case TelephonyManager.CALL_STATE_IDLE:
//                            Log.v("Idle State : ", "IDLE");
//                            stopRecording();
//                           /* SharedPreferences pref = ctx.getSharedPreferences("number", ctx.MODE_PRIVATE);
//                            pref.getString("number", null);*/
//                            final Configuration myconfig = new Configuration();
//                            if (ccno != null) {
//                                //RK myconfig.getCallDetails(ctx);
//                                myconfig.getphonecalls(ctx, ccno);
//                            }
//
//                            break;
//                        default:
//                            break;
//                    }
//                }
//
//
//                /**
//                 * Call recording start
//                 */
//                private void startRecording(File file) {
//                    Configuration. writetoerrorfile7("Recoding method call".toString(), ctx);
//                    if (recorder != null) {
//                        recorder.release();
//                    }
//
//                    if(file!=null) {
//                        recorder = new MediaRecorder();
//                        recorder.reset();
//
//                        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
//                        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//                        recorder.setAudioSamplingRate(8000);
//                        recorder.setAudioEncodingBitRate(32000);
//                        recorder.setOutputFile(file.getAbsolutePath());
//
//
//                        /*recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
//                        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
//                        recorder.setAudioEncoder(MediaRecorder.getAudioSourceMax());
//                        recorder.setAudioEncodingBitRate(16);
//                        recorder.setAudioSamplingRate(44100);
//                        recorder.setOutputFile(file.getAbsolutePath());*/
//
//
//                        try {
//                            recorder.prepare();
//                        } catch (IOException e) {
//                            Configuration. writetoerrorfile7("Recoding method call"+e.toString().toString(), ctx);
//                        }
//                        recorder.start();
//                    }
//                }
//
//                /**
//                 * Call recording stop
//                 */
//                private void stopRecording() {
//
//                    if (recorder != null) {
//                        try {
//                            Thread.sleep(2000);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        recorder.release();
//                        recorder = null;
//                    }
//
//                }
//
//
//                /**
//                 * Call recording directory or folder creation
//                 */
//                public File createDirIfNotExists(String path) {
//                    selected_song_name = path;
//                    File folder = new File(Environment.getExternalStorageDirectory() + "/Insight");
//                    if (!folder.exists()) {
//                        if (!folder.mkdirs()) {
//                            Log.e("Creating Folder :: ", "folder is created");
//                        }
//                    }
//
//                    /**
//                     * Call recording saving
//                     */
//                    File file = new File(folder, path + ".mp3");
//                    try {
//                        if (!file.exists()) {
//                            if (file.createNewFile()) {
//                                Log.e("Call data :: ", "file is created");
//                            }
//                        }
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    return file;
//                }
//
//            }, PhoneStateListener.LISTEN_CALL_STATE);
//            today = new Time(Time.getCurrentTimezone());
//            today.setToNow();
//        }
//    }
//
//}