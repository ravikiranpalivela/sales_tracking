package com.tekskills.st_tekskills.presentation.br

//import com.tekskills.st_tekskills.data.repository.TaskCategoryRepositoryImpl
//
//@AndroidEntryPoint
//class RebootBroadcastReceiver : BroadcastReceiver(){
//    @Inject
////    lateinit var repository: TaskCategoryRepositoryImpl
////    override fun onReceive(context: Context?, p1: Intent?) {
////        val time = Date()
////        CoroutineScope(Main).launch {
//////            val list = repository.getActiveAlarms(time)
//////            for(taskInfo in list) setAlarm(taskInfo, context)
////        }
////    }
//
////    private fun setAlarm(taskInfo: TaskInfo, context: Context?){
////        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val intent = Intent(context, AlarmReceiver::class.java)
////        intent.putExtra("task_info", taskInfo)
////        val pendingIntent = PendingIntent.getBroadcast(context, taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
////        val mainActivityIntent = Intent(context, MainActivity::class.java)
////        val basicPendingIntent = PendingIntent.getActivity(context, taskInfo.id, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
////        val clockInfo = AlarmManager.AlarmClockInfo(taskInfo.date.time, basicPendingIntent)
//////        alarmManager.setAlarmClock(clockInfo, pendingIntent)
////    }
//
//}