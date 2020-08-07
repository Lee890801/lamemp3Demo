package com.unicloud.mp3lame.inter

import android.content.Context


/**
 *  Created by fpc  2019-06-12  11:07
 */
interface IRecord {
    fun startRecord(con: Context, listener: IRecordListener)

    fun startRecord(name: String, listener: IRecordListener,isNeedVolumeCallback:Boolean=true)

    fun stopRecord()

    fun isRecording(): Boolean
}