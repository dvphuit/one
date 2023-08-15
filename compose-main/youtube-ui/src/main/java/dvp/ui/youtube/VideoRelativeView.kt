package dvp.ui.youtube

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dvp.data.youtube.models.VideoEntity
import dvp.ui.youtube.common.LoadingView


@Composable
fun Modifier.VideoRelatedView(video: VideoEntity?) {
    video ?: return

    if (video.relatedVideos == null) {
        LoadingView()
        return
    }

    if (video.relatedVideos?.isEmpty() == true) {
        Box(
            modifier = this
                .fillMaxSize()
                .background(Color.Green)
        )
        return
    }

    Box(
        modifier = this
            .fillMaxSize()
            .background(Color.White)
    )
}