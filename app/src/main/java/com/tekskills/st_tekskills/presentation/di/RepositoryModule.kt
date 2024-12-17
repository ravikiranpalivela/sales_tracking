package com.tekskills.st_tekskills.presentation.di

import com.tekskills.st_tekskills.data.db.TaskCategoryDao
import com.tekskills.st_tekskills.domain.TaskCategoryRepository
import com.tekskills.st_tekskills.data.repository.TaskCategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskCategoryRepository(taskCategoryDao: TaskCategoryDao) : TaskCategoryRepository {
        return TaskCategoryRepositoryImpl(taskCategoryDao)
    }
}