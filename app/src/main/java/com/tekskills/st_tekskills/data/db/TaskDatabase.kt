package com.tekskills.st_tekskills.data.db

import androidx.room.*
import com.tekskills.st_tekskills.data.model.CategoryInfo
import com.tekskills.st_tekskills.data.model.TaskInfo

@Database(entities = [TaskInfo::class, CategoryInfo::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskCategoryDao() : TaskCategoryDao
}