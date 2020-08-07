package com.unicloud.mp3lame.engine

import android.content.Context
import android.os.Handler
import com.unicloud.mp3lame.core.MP3Recorder
import com.unicloud.mp3lame.engine.FileUtils
import com.unicloud.mp3lame.inter.IRecord
import com.unicloud.mp3lame.inter.IRecordListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * record class
 *  Created by fpc  2019-06-12  11:02
 */
class RecordEngine : IRecord {

    private var mp3Recorder: MP3Recorder? = null
    private var listener: IRecordListener? = null
    private var fileName: String? = null
    private var handler = Handler()
    private var context:Context? = null

    override fun startRecord(name: String, listener: IRecordListener, isNeedVolumeCallback: Boolean) {
        this.listener = listener

        if (isRecording()) {
            listener?.onRecordError("上一次录音未结束")
            return
        }
        try {
            makeFile( name) {
                mp3Recorder = MP3Recorder(it, isNeedVolumeCallback)
                mp3Recorder?.start()
                listener?.onRecordStart()
                if (isNeedVolumeCallback)
                    getVolume()
            }

        } catch (e: Exception) {
            listener?.onRecordError("录音启动失败：${e.message}")
        }
    }

    override fun startRecord(con: Context, listener: IRecordListener) {
        context = con
        startRecord(System.currentTimeMillis().toString(), listener, true)
    }

    fun getVolume() {
        listener?.calculateVolume(mp3Recorder?.realVolume)
        handler.postDelayed({
            getVolume()
        }, 200)
    }

    private fun makeFile(name: String, u: (file: File) -> Unit) {
        Observable.just(name)
                .map<File> {
                    var path = FileUtils.getRootPath(context,context?.packageName.toString(), true) + "audio"+ File.separator + "$name.mp3"
                    this.fileName = path
                    FileUtils.createFile(path)
                    return@map File(path)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    u.invoke(it)
                }
    }

    override fun stopRecord() {
        try {
            handler.removeCallbacksAndMessages(null)
            mp3Recorder?.stop()
            mp3Recorder = null
            listener?.onRecordComplete(fileName)
        } catch (e: Exception) {
            listener?.onRecordError("stopError:${e.message}")
        }
    }

    override fun isRecording() = mp3Recorder != null && mp3Recorder!!.isRecording

    companion object {
        const val TAG = "RecordEngine"
        const val NAME_VOICE_DEFAULT = "name_voice_default"
        @JvmStatic
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = RecordEngine()
    }

    private constructor()
}