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
package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.presentation.viewmodel.TrackInformationViewModel
import com.javavirys.mediaplayer.util.extension.findView
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackInformationFragment :
    BaseFragment<TrackInformationViewModel>(R.layout.fragment_track_information) {

    private val args: TrackInformationFragmentArgs by navArgs()

    override val model: TrackInformationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filesTextView = view.findView<TextView>(R.id.filesTextView)
        val fileSizeTextView = view.findView<TextView>(R.id.fileSizeTextView)

        model.trackInformationLiveData.observe(viewLifecycleOwner) {
            var count = 0L
            it.forEach { item ->
                filesTextView.text = "${filesTextView.text}${item.filePath}\n"
                count += item.fileSize
            }
            fileSizeTextView.text = "${(count / 1024)} kb"
        }

        model.loadTrackInformation(args.tracks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        model.trackInformationLiveData.removeObservers(viewLifecycleOwner)
    }

    override fun getScreenName() = "TrackInformationScreen"
}