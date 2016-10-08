package com.edu.basicaccountingforguangzhou.view;

import android.widget.VideoView;

public interface OnQuitFullscreenListener {
	void onQuitFullscreen(VideoView videoView, Boolean isPlay,
			int currentPosition, int duration);

	void onFullscreen(VideoView videoView, Boolean isPlay, int currentPosition,
			int duration);
}
