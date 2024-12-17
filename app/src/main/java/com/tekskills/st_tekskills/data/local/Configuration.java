package com.tekskills.st_tekskills.data.local;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.CallLog;
//import android.telephony.TelephonyManager;
//import android.text.format.Time;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Configuration implements LocationListener {
//    String encodedImage;
//    String Lat, Long, contactNo = null;
//    Time today;
//    String fileOutput;
//    static String CALLTYPE;
//    static String DATE;
//    static String TIME;
//    public static String PHONENO;
//    static String CALLDURATION;
//    public static String Lati, Longi;
//    String encode = null;
//    protected LocationManager locationManager;
//
//    //Modified 12 jan 2016
//    public void getmediafile(Context myconetxt) {
//        TelephonyManager telephonyManager = (TelephonyManager) myconetxt.getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(myconetxt, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
//
//        String date = today.year + "-" + (today.month + 1) + "-" + today.monthDay;
//        String time = today.format("%k:%M:%S");
//
//        final Calendar c = Calendar.getInstance();
//        System.out.println("Current Date:" + c.get(Calendar.DAY_OF_MONTH) + "/" +
//                String.valueOf(Integer.parseInt(String.valueOf(c.get(Calendar.MONTH))) + 1) + "/" + c.get(Calendar.YEAR));
//
//        String CurrentDate = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + "/" +
//                String.valueOf(Integer.parseInt(String.valueOf(c.get(Calendar.MONTH))) + 1) + "/" + c.get(Calendar.YEAR));
//
//        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/Insight");// "/DCIM/Camera"
//        File[] dirFiles = fileDirectory.listFiles();
//
//        if (dirFiles != null) {
//            if (dirFiles.length != 0) {
//                // loops through the array of files, outputing the name to console
//                try {
//                    for (int ii = 0; ii < dirFiles.length; ii++) {
//                        String fileOutput = dirFiles[ii].toString();
//                        Date lastModDate = new Date(dirFiles[ii].lastModified());
//                        if (CurrentDate.equals(String.valueOf(lastModDate.getDate()
//                                + "/" + String.valueOf(Integer.parseInt(String.valueOf(lastModDate.getMonth())) + 1) + "/" + c.get(Calendar.YEAR)))) {
//                            System.out.println("My Files :" + fileOutput);
//                            encode = null;
//                            String filenameArray[] = fileOutput.split("\\.");
//                            String extension = filenameArray[filenameArray.length - 1];
//                            System.out.println(extension);
//                            /*if (extension.equals("jpg")) {
//                                encode = encodeGalleryImage(fileOutput);
//                                getlatlong(myconetxt);
//                                Lati = Lat;
//                                Longi = Long;
//                                MySocket mysocket = new MySocket(myconetxt, encode, telephonyManager.getDeviceId(), date, time, "jpg", Lat, Long, contactNo, "");
//                                mysocket.connectSocket();
//                            } else*/
//                            if (extension.equals("mp3")) {
//                                encode = convertFileToString(fileOutput);
//                                getlatlong(myconetxt);
//                                Lati = Lat;
//                                Longi = Long;
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // TODO Auto-generated method stub
//                                        callDataUpdateAPI(myconetxt,date,date+time, Lat, Long,contactNo,CALLTYPE,CALLDURATION,encode,contactNo);
//                                    }
//                                }, 15000);
//                               // callDataUpdateAPI(myconetxt,date,time, Lat, Long,contactNo,CALLTYPE,CALLDURATION,encode,contactNo);
//                               // Log.v("Call Details", date + "\n" + time + "\n" + Lati + "\n" + Longi + "\n" + contactNo + "\n" + CALLTYPE + "\n" + CALLDURATION + "\n" + encode + "\n" + contactNo);
//                            } else {
//                                encode = convertFileToString(fileOutput);
////                                getlatlong(myconetxt);
//                                Lati = Lat;
//                                Longi = Long;
//                                /*MySocket mysocket = new MySocket(myconetxt, encode, telephonyManager.getDeviceId(), date, time, extension, Lati, Longi, contactNo, "");
//                                mysocket.connectSocket();*/
//                            }
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//   /* public String encodeGalleryImage(String filePath) {
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 3;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);
//        byte[] data = bos.toByteArray();
//        encodedImage = com.tekskills.insight.utils.Base64.encodeBytes(data);
//        return encodedImage;
//    }*/
//
//    public String convertFileToString(String pathOnSdCard) {
//        String strFile = null;
//        File file = new File(pathOnSdCard);
////        try {
////            strFile = com.tekskills.insight.utils.Base64.encodeFromFile(file.toString());
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        return strFile;
//    }
//
//
////    public void getlatlong(Context context) {
////        GPSService mGPSService = new GPSService(context);
////        mGPSService.getLocation();
////        if (!mGPSService.isLocationAvailable) {
////            // Here you can ask the user to try again, using return; for that
////            Toast.makeText(context, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
////        } else {
////            Lat = String.valueOf(mGPSService.getLatitude());
////            Long = String.valueOf(mGPSService.getLongitude());
////        }
////    }
//
//    public void getphonecalls(Context myconetxt, String contact) {
//        contactNo = contact;//contact
//        getCallDetails(myconetxt);
//        SharedPreferences.Editor editor = myconetxt.getSharedPreferences("number", myconetxt.MODE_PRIVATE).edit();
//        editor.clear();
//        editor.apply();
//
//        today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
//        String date = today.year + "-" + (today.month + 1) + "-" + today.monthDay;
//        String time = today.format("%k:%M:%S");
//
//        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/Insight");
//        final Calendar c = Calendar.getInstance();
//        File[] dirFiles = fileDirectory.listFiles();
//        if (dirFiles != null) {
//            if (dirFiles.length != 0) {
//                // loops through the array of files, outputing the name to console
//                for (int ii = dirFiles.length - 1; ii < dirFiles.length; ii++) {
//                    fileOutput = dirFiles[ii].toString();
//                    encode = null;
//
//                    String[] filenameArray = fileOutput.split("\\.");
//                    String extension = filenameArray[filenameArray.length - 1];
//                    if (extension.equals("mp3")) {
//                        encode = convertFileToString(fileOutput);
//                        getlatlong(myconetxt);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                callDataUpdateAPI(myconetxt,date,date+" "+time, Lat, Long,contactNo,CALLTYPE,CALLDURATION,encode,contactNo);
//                            }
//                        }, 15000);
//                        //callDataUpdateAPI(myconetxt,date,date+time, Lat, Long,contactNo,CALLTYPE,CALLDURATION,encode,contactNo);
//                       // Log.v("Call Details123", date + "\n" + time + "\n" + Lat + "\n" + Long + "\n" + contactNo + "\n" + CALLTYPE + "\n" + CALLDURATION + "\n" + encode + "\n" + contactNo);
//                    } else {
//                        encode = convertFileToString(fileOutput);
//                        getlatlong(myconetxt);
//                       /* MySocket mysocket = new MySocket(myconetxt, encode, Common.deviceID(myconetxt), date, time, extension, Lat, Long, contactNo, CALLDURATION);
//                        mysocket.connectSocket();*/
//                    }
//                }
//            }
//        }
//    }
//
//
//    //Modified RK
//    public void getCallDetails(Context context) {
//        StringBuffer sb = new StringBuffer();
//        String strOrder = CallLog.Calls.DATE + " DESC";
//        Uri contacts = CallLog.Calls.CONTENT_URI;
//        /* Query the CallLog Content Provider */
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
//                null, null, strOrder);
//        //int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
//        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
//        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
//        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
//        sb.append("Call Log :");
//        Log.d("CALL LOG11", ":" + /*number*/ +type + date);
//        String phNum = null;
//        String callType = null;
//        String callDuration = null;
//        while (managedCursor.moveToNext()) {
//            phNum = contactNo;
//            String callTypeCode = managedCursor.getString(type);
//            callDuration = managedCursor.getString(duration);
//            callType = managedCursor.getString(type);
//           /* String callDate = managedCursor.getString(date);
//            String callDayTime = new Date(Long.valueOf(callDate)).toString();*/
//            String dir = null;
//
//            int callcode = Integer.parseInt(callTypeCode);
//            switch (callcode) {
//                case CallLog.Calls.OUTGOING_TYPE:
//                    callType = "Outgoing";
//                    break;
//                case CallLog.Calls.INCOMING_TYPE:
//                    callType = "Incoming";
//                    break;
//                case CallLog.Calls.MISSED_TYPE:
//                    callType = "Missed";
//                    break;
//            }
//            today = new Time(Time.getCurrentTimezone());
//            today.setToNow();
//            String date1 = today.year + "-" + (today.month + 1) +
//                    "-" + today.monthDay;
//            String time = today.format("%k:%M:%S");
//            CALLTYPE = callType;
//            DATE = date1;
//            TIME = time;
//            PHONENO = phNum;
//            CALLDURATION = callDuration;
//            // com.tekskills.insight.data.SharedPreferences.savePreferences(context,"mob_number",phNum);
//            com.tekskills.insight.Model.SharedPreferences.savePreferences(context, "call_type", callType);
//            com.tekskills.insight.Model.SharedPreferences.savePreferences(context, "duration", callDuration);
//            com.tekskills.insight.Model.SharedPreferences.savePreferences(context, "date", date1);
//            com.tekskills.insight.Model.SharedPreferences.savePreferences(context, "time", time);
//
//           /* Log.v("@@CALL DETAILS", "\nPhone Number:--- " + phNum +
//                    " \nCall Type:--- " + callType + " \nCall Date:--- " + date1 + " : " + time +
//                    " \nCall duration in sec :--- " + callDuration);*/
//            break;
//        }
//        today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
//        managedCursor.close();
//    }
//
//
//    public void delete() {
//        File dir = new File(Environment.getExternalStorageDirectory() + "/Insight");
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                new File(dir, children[i]).delete();
//            }
//        }
//    }
//
//
//   /* //Splitting video
//    public static void split(String path) {
//        try {
//            File file = new File("/storage/emulated/0/Video/Brown Rang Full Song HD- International Villager Yo Yo Honey Singh-YouTube.mp4 ");//File read from Source folder to Split.
//            if (file.exists()) {
//
//                String videoFileName = file.getName().substring(0, file.getName().lastIndexOf(".")); // Name of the videoFile without extension
//                File splitFile = new File(Environment.getExternalStorageDirectory() + "/Insight" + videoFileName);//Destination folder to save.
//                if (!splitFile.exists()) {
//                    splitFile.mkdirs();
//                }
//
//                int i = 01;// Files count starts from 1
//                InputStream inputStream = new FileInputStream(file);
//                String videoFile = splitFile.getAbsolutePath() + "/" + String.format("%02d", i) + "_" + file.getName();// Location to save the files which are Split from the original file.
//                OutputStream outputStream = new FileOutputStream(videoFile);
//                int totalPartsToSplit = 20;// Total files to split.
//                int splitSize = inputStream.available() / totalPartsToSplit;
//                int streamSize = 0;
//                int read = 0;
//                while ((read = inputStream.read()) != -1) {
//                    if (splitSize == streamSize) {
//                        if (i != totalPartsToSplit) {
//                            i++;
//                            String fileCount = String.format("%02d", i); // output will be 1 is 01, 2 is 02
//                            videoFile = splitFile.getAbsolutePath() + "/" + fileCount + "_" + file.getName();
//                            outputStream = new FileOutputStream(videoFile);
//                            streamSize = 0;
//                        }
//                    }
//                    outputStream.write(read);
//                    streamSize++;
//                }
//                inputStream.close();
//                outputStream.close();
//            } else {
//                System.err.println(file.getAbsolutePath() + " File Not Found.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }*/
//
//
//    /**
//     * CALL DATA UPDATE API
//     **/
//     private void callDataUpdateAPI(Context myconetxt,String date,String time,String latitude, String longitude,
//                                   String phoneNumber,String callCodeType, String dutration,String media,String number) {
//        if(!Common.isNetworkAvailable(myconetxt)){
//            Toast.makeText(myconetxt, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            String bdm_name = com.tekskills.insight.utils.SharedPreferences.getPreferences(myconetxt,Common.USERNAME);
//            ApiInterface locationService = ApiClient.getClient().create(ApiInterface.class);
//            Call<CheckInModel> locationUpdateCall = locationService.callAddCallDataAPi(bdm_name,"CallLog", date,time,latitude,longitude,
//                    phoneNumber,callCodeType,dutration,media,"mp3" + "",number);
//            Log.v("Caller phone ",bdm_name+"\n"+"CallLog"+"\n"+ date+"\n"+ time+"\n"+ latitude+"\n"+ longitude+"\n"+
//                    phoneNumber+"\n"+ callCodeType+"\n"+ dutration+"\n"+ media+ "\n"+"mp3" + ""+ "\n"+number);
//            locationUpdateCall.enqueue(new Callback<CheckInModel>() {
//                @Override
//                public void onResponse(Call<CheckInModel> call, Response<CheckInModel> response) {
//                    if(response.body().getStatus().equalsIgnoreCase("success")){
//                        String msg = response.body().getMsg();
//                        Log.v("CALL DATA UPDATED : ",msg);
//                        delete();
//                    }else
//                        return;
//                }
//
//                @Override
//                public void onFailure(Call<CheckInModel> call, Throwable t) {
//                    Toast.makeText(myconetxt, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    public static void writetodatafile(String s, Context c) {
//        try {
//            File myFile = new File("/sdcard/log1.txt");
//            myFile.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(myFile);
//            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//            myOutWriter.append(s);
//            myOutWriter.close();
//            fOut.close();
//            //Toast.makeText(c, "Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(c, e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public static void writetoerrorfile6(String s, Context c) {
//        try {
//            File myFile = new File("/sdcard/log6.txt");
//            myFile.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(myFile);
//            OutputStreamWriter myOutWriter =
//                    new OutputStreamWriter(fOut);
//            myOutWriter.append(s);
//            myOutWriter.close();
//            fOut.close();
//          //  Toast.makeText(c, "Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    public static void writetoerrorfile7(String s, Context c) {
//        try {
//            File myFile = new File("/sdcard/log7.txt");
//            myFile.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(myFile);
//            OutputStreamWriter myOutWriter =
//                    new OutputStreamWriter(fOut);
//            myOutWriter.append(s);
//            myOutWriter.close();
//            fOut.close();
//          //  Toast.makeText(c, "Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        // Toast.makeText(this, "Longitude: " + location.getLongitude()+"\nLatitude: " + location.getLatitude(), Toast.LENGTH_SHORT).show();
//        Log.d("Latitude","\nLongitude: " + location.getLongitude()+"\nLatitude: " + location.getLatitude());
//        String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
//        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//        Lat = String.valueOf(location.getLatitude());
//        Long = String.valueOf(location.getLongitude());
//
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//        Log.d("Latitude","status");
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//        Log.d("Latitude","enable");
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//        Log.d("Latitude","disable");
//        /*Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivity(intent1);*/
//    }
//}
