#include <SDL2/SDL.h>
#include <SDL2/SDL_mixer.h>

#include <iostream>

#include "AudioPlayer_jni.h"

JavaVM * jvm = nullptr;

jint jvm_version;
jclass callback_class;
jmethodID callback_method;

void effectCallback(int chan, void *stream, int len, void *udata) {

	JNIEnv * g_env;
	// double check it's all ok
	int getEnvStat = jvm->GetEnv((void **) &g_env, jvm_version);

	if (getEnvStat == JNI_EDETACHED) {

		if (jvm->AttachCurrentThread((void **) &g_env, nullptr) != 0) {
			std::cerr << "Failed to attach" << std::endl;
		}

	} else if (getEnvStat == JNI_OK) {

	} else if (getEnvStat == JNI_EVERSION) {

		std::cerr << "GetEnv: version not supported" << std::endl;

	}

	// g_env->CallStaticVoidMethod(callback_class, callback_method);

	jintArray buffer = g_env->NewIntArray(len/2);

	int* newBuffer = new int[len/2];

	for (int i=0; i<len/2;i+=2) {
		newBuffer[i] = ( ((int *)stream)[i] + ((int *)stream)[i+1])/2;
	}

	g_env->SetIntArrayRegion(buffer, 0, len/2, (jint*)newBuffer);

	g_env->CallIntMethod(callback_class, callback_method, buffer);

	jvm->DetachCurrentThread();

	delete[] newBuffer;

	/*
	 std::cout << std::to_string((unsigned long long)callback_method) << std::endl;

	 */

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_init(JNIEnv *env, jobject) {

	env->GetJavaVM(&jvm);
	jvm_version = env->GetVersion();

	callback_class = env->FindClass("com/libwave/desktop/service/AudioPlayer");

	if (!callback_class) {
		std::cerr << "callback_class not found" << std::endl;
		std::terminate();
	}

	std::cout << std::to_string((unsigned long long) callback_class) << std::endl;

	callback_method = env->GetStaticMethodID(callback_class, "audioDataCallback", "([I)V");

	if (!callback_method) {
		std::cerr << "callback_method not found" << std::endl;
		std::terminate();
	}

	if (SDL_Init( SDL_INIT_EVERYTHING) < 0) {
		std::cerr << "There was an error initializing SDL2: " << SDL_GetError() << std::endl;
		std::terminate();
	}

	auto flags = MIX_INIT_OGG | MIX_INIT_MOD | MIX_INIT_FLAC | MIX_INIT_MP3;

	auto initted = Mix_Init(flags);
	if (initted & flags != flags) {
		std::cerr << "Mix_Init: Failed to init required ogg, mod, flac, mp3 support!\n";
		std::cerr << "Mix_Init: " << Mix_GetError() << std::endl;
		std::terminate();
	}

	if (Mix_OpenAudio(44100, MIX_DEFAULT_FORMAT, 2, 1024) == -1) {
		std::cerr << "Mix_OpenAudio: " << Mix_GetError() << std::endl;
		std::terminate();
	}

	if (!Mix_RegisterEffect(MIX_CHANNEL_POST, effectCallback, nullptr, nullptr)) {
		std::cerr << "Mix_RegisterEffect: " << Mix_GetError() << std::endl;
	}

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_shutdown(JNIEnv *, jobject) {

	Mix_CloseAudio();

	Mix_Quit();

	SDL_Quit();

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_resume(JNIEnv *, jobject) {

	Mix_ResumeMusic();
}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_stop(JNIEnv *, jobject) {

	Mix_HaltMusic();
}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_stopFadeOut(JNIEnv *, jobject, jint ms) {

	while (!Mix_FadeOutMusic(ms) && Mix_PlayingMusic()) {
		SDL_Delay(100);
	}

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_pause(JNIEnv *, jobject) {

	Mix_PauseMusic();

}

JNIEXPORT jlong JNICALL Java_com_libwave_desktop_service_AudioPlayer_loadMusic(JNIEnv *env, jobject, jstring file) {

	char buf[4096];

	const char *str = env->GetStringUTFChars(file, 0);

	auto music = Mix_LoadMUS(str);

	if (!music) {
		std::cerr << "Mix_LoadMUS failed for " << str << ", error: " << Mix_GetError() << std::endl;
		return 0;
	}

	env->ReleaseStringUTFChars(file, str);

	return (unsigned long long) music;
}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_playMusic(JNIEnv *env, jobject, jlong id) {

	if (Mix_PlayMusic((Mix_Music*) id, 0) == -1) {
		std::cerr << "Mix_PlayMusic: " << Mix_GetError() << std::endl;
	}

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_playMusicFadeIn(JNIEnv *, jobject, jlong music, jint ms) {
	Mix_FadeInMusic((Mix_Music*) music, -1, ms);
}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_closeMusic(JNIEnv *, jobject, jlong music) {
	Mix_FreeMusic((Mix_Music*) music);
}
