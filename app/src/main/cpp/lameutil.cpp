#include <jni.h>
#include <string>
#include "lame.h"

static lame_global_flags *lame = NULL;

extern "C" JNIEXPORT void JNICALL Java_com_unicloud_mp3lame_LameUtil_init(JNIEnv *env, jclass cls,
        jint inSamplerate, jint inChannel, jint outSamplerate, jint outBitrate, jint quality){
    if (lame != NULL) {
        lame_close(lame);
        lame = NULL;
    }
    lame = lame_init();
    lame_set_in_samplerate(lame, inSamplerate);
    lame_set_num_channels(lame, inChannel);//输入流的声道
    lame_set_out_samplerate(lame, outSamplerate);
    lame_set_brate(lame, outBitrate);
    lame_set_quality(lame, quality);
    lame_init_params(lame);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_unicloud_mp3lame_LameUtil_encode(JNIEnv *env, jclass clazz, jshortArray buffer_l,
                                          jshortArray buffer_r, jint samples,
                                          jbyteArray mp3buf) {

    jshort* j_buffer_l = env->GetShortArrayElements(buffer_l, NULL);

    jshort* j_buffer_r = env->GetShortArrayElements(buffer_r, NULL);

    const jsize mp3buf_size = env->GetArrayLength(mp3buf);
    jbyte* j_mp3buf = env->GetByteArrayElements(mp3buf, NULL);

    int result = lame_encode_buffer(lame, j_buffer_l, j_buffer_r,
                                    samples, reinterpret_cast<unsigned char *>(j_mp3buf), mp3buf_size);

    env->ReleaseShortArrayElements(buffer_l, j_buffer_l, 0);
    env->ReleaseShortArrayElements(buffer_r, j_buffer_r, 0);
    env->ReleaseByteArrayElements(mp3buf, j_mp3buf, 0);

    return result;

}

extern "C"
JNIEXPORT jint JNICALL
Java_com_unicloud_mp3lame_LameUtil_flush(JNIEnv *env, jclass clazz, jbyteArray mp3buf) {
    const jsize mp3buf_size = env->GetArrayLength( mp3buf);
    jbyte* j_mp3buf = env->GetByteArrayElements( mp3buf, NULL);

    int result = lame_encode_flush(lame, reinterpret_cast<unsigned char *>(j_mp3buf), mp3buf_size);

    env->ReleaseByteArrayElements(mp3buf, j_mp3buf, 0);

    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_unicloud_mp3lame_LameUtil_close(JNIEnv *env, jclass clazz) {
    lame_close(lame);
    lame = NULL;
}