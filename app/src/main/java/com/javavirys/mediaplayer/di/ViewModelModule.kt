/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.presentation.viewmodel.MainViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.SplashViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { TrackListViewModel(get(), get()) }
    viewModel { TrackViewModel(get()) }
}