package com.vpvn.mediaplayer.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {


    fun getMediaDirectories(): List<MediaDirectory> {
        val list = mutableListOf<MediaDirectory>()
        val folder1 = MediaDirectory("Camera")
        val folder2 = MediaDirectory("WhatzApp")
        val folder3 = MediaDirectory("Downloads")
        val folder4 = MediaDirectory("Movies")

        /*val folder5 = MediaDirectory("Camera2")
        val folder6 = MediaDirectory("WhatzApp2")
        val folder7 = MediaDirectory("Downloads2")
        val folder8 = MediaDirectory("Movies2")

        val folder9 = MediaDirectory("Camera3")
        val folder10 = MediaDirectory("WhatzApp3")
        val folder11 = MediaDirectory("Downloads3")
        val folder12 = MediaDirectory("Movies3")*/

        with(list) {
            add(folder1)
            add(folder2)
            add(folder3)
            add(folder4)

            /*add(folder5)
            add(folder6)
            add(folder7)
            add(folder8)

            add(folder9)
            add(folder10)
            add(folder11)
            add(folder12)*/
        }
        return list
    }
}