#include <SDL2/SDL.h>
#include <SDL2/SDL_mixer.h>

#include <iostream>

#include "AudioPlayer_jni.h"

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_init(JNIEnv *, jobject) {

	if (SDL_Init( SDL_INIT_EVERYTHING) < 0) {
		std::cerr << "There was an error initializing SDL2: " << SDL_GetError() << std::endl;
		std::terminate();
	}

	// load support for the OGG and MOD sample/music formats
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

}

/*
 * Class:     com_libwave_desktop_service_AudioPlayer
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_shutdown(JNIEnv *, jobject) {

	Mix_CloseAudio();

	Mix_Quit();

	SDL_Quit();

}

/*
 * Class:     com_libwave_desktop_service_AudioPlayer
 * Method:    resume
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_resume(JNIEnv *, jobject) {

}

/*
 * Class:     com_libwave_desktop_service_AudioPlayer
 * Method:    stop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_stop(JNIEnv *, jobject) {

	Mix_HaltMusic();
}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_stopFadeOut(JNIEnv *, jobject, jint ms) {

	// fade out music to finish 3 seconds from now
	while (!Mix_FadeOutMusic(ms) && Mix_PlayingMusic()) {
		SDL_Delay(100);
	}

}

/*
 * Class:     com_libwave_desktop_service_AudioPlayer
 * Method:    pause
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_pause(JNIEnv *, jobject) {

	Mix_PauseMusic();

}

/*
 * Class:     com_libwave_desktop_service_AudioPlayer
 * Method:    loadMusic
 * Signature: (Ljava/lang/String;)J
 */
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

/*
 * Class:     com_libwave_desktop_service_AudioPlayer
 * Method:    playMusic
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_playMusic(JNIEnv *env, jobject, jlong id) {

	Mix_PlayMusic((Mix_Music*) id, 1);

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_playMusicFadeIn(JNIEnv *, jobject, jlong music, jint ms) {

	Mix_FadeInMusic((Mix_Music*) music, -1, ms);

}

JNIEXPORT void JNICALL Java_com_libwave_desktop_service_AudioPlayer_closeMusic(JNIEnv *, jobject, jlong music) {

	Mix_FreeMusic((Mix_Music*) music);

}
