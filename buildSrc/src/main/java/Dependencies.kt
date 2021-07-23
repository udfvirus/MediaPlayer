/*
 * Copyright 2021 Vitaliy Sychov
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

object Versions {

    const val gradle = "4.2.2"

    const val kotlinGradlePlugin = "1.5.20"

    const val kotlin = "1.5.20"

    const val coreKtx = "1.5.0"

    const val activityKtx = "1.2.3"

    const val fragmentKtx = "1.3.2"

    const val appcompat = "1.2.0"

    const val material = "1.4.0"

    const val constraintLayout = "2.0.4"

    const val lottie = "3.7.0"

    const val timber = "4.7.1"

    const val navigation = "2.3.5"

    const val kotlinxCoroutinesAndroid = "1.4.3"

    const val glide = "4.12.0"

    const val media = "1.3.1"

    const val room = "2.3.0"

    const val koin = "3.0.2"

    const val exoplayer = "2.14.1"
}

object Dependencies {

    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"

    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinGradlePlugin}"

    const val navigationSafeArgsGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"

    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"

    const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val kotlinxCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCoroutinesAndroid}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val media = "androidx.media:media:${Versions.media}"

    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"

    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    const val koin = "io.insert-koin:koin-android:${Versions.koin}"

    const val koinExt = "io.insert-koin:koin-android-ext:${Versions.koin}"

    const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}"

    const val exoplayerUi = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}"

    const val exoplayerExtensionMediasession =
        "com.google.android.exoplayer:extension-mediasession:${Versions.exoplayer}"

    const val exoplayerExtensionCast =
        "com.google.android.exoplayer:extension-cast:${Versions.exoplayer}"
}