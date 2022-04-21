package studio.iskaldvind.democlasses.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import studio.iskaldvind.democlasses.repository.IRepository
import studio.iskaldvind.democlasses.repository.Repository
import studio.iskaldvind.democlasses.viewmodel.ClassesViewModel
import studio.iskaldvind.democlasses.viewmodel.ReviewViewModel

val application = module {
    single<IRepository> { Repository() }
}

val reviewScreen = module {
    viewModel { ReviewViewModel(repository = get()) }
}

val classesScreen = module {
    viewModel { ClassesViewModel(repository = get()) }
}