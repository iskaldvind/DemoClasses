package studio.iskaldvind.democlasses

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import studio.iskaldvind.democlasses.di.application
import studio.iskaldvind.democlasses.di.classesScreen
import studio.iskaldvind.democlasses.di.reviewScreen

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(
                application,
                reviewScreen,
                classesScreen
            ))
        }
    }
}