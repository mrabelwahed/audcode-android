package com.audcode.di.module

import androidx.lifecycle.ViewModel
import com.audcode.ui.login.RegisterVM
import com.audcode.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class RegisterViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegisterVM::class)
    internal abstract fun bindRegisterViewModel(viewModel: RegisterVM): ViewModel
}