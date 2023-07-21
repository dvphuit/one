package dvp.data.youtube.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dvp.data.youtube.domain.AuthenticatedUseCase
import dvp.data.youtube.domain.GetRelativeUseCase
import dvp.data.youtube.domain.GetVideoDetailUseCase
import dvp.data.youtube.domain.LoadVideoListUseCase
import dvp.data.youtube.extension.fromVideoDetailDto
import dvp.data.youtube.models.VideoEntity
import dvp.lib.core.viewmodel.MviViewModel


class UTubeViewModel(
    auth: AuthenticatedUseCase,
    private val loadVideos: LoadVideoListUseCase,
    private val getVideo: GetVideoDetailUseCase,
    private val getRelative: GetRelativeUseCase
) : MviViewModel<YoutubeState, YoutubeEvent>() {

    init {
        val cookie = "SID=TwhMu3L-rBNe9SrkM8vA7GZxd3msRsDLxiKnxe3aNa06maak8eku7OqIk54-Qc3PimZnjA.; __Secure-1PSID=TwhMu3L-rBNe9SrkM8vA7GZxd3msRsDLxiKnxe3aNa06maakN46YM7OUKIPKdRCtRGXYpQ.; __Secure-3PSID=TwhMu3L-rBNe9SrkM8vA7GZxd3msRsDLxiKnxe3aNa06maakfKaQasPRoUJNGmikAmAx6A.; HSID=Ao-8pnEE9DqvaDoKA; SSID=ASpLtRbGXcDYQ_fv_; APISID=PnjYpDxZ8A7qUGxE/ACbyLv6lb9SsuOuru; SAPISID=DW_4sjFVZ6qc1LlC/AzFBJ2tGBZZnsKgjE; __Secure-1PAPISID=DW_4sjFVZ6qc1LlC/AzFBJ2tGBZZnsKgjE; __Secure-3PAPISID=DW_4sjFVZ6qc1LlC/AzFBJ2tGBZZnsKgjE; LOGIN_INFO=AFmmF2swRQIhAMyjG4LmkNH5_uWlY6eNocar240GcKLSyDBHOMl17aMsAiBFqVmT5__TP_VY2CPzrXjdNQNXc738jeSjQhAwlWN-Hg:QUQ3MjNmd1JkZ0UyaE9RV1czaHA0V0ppMDVZN3NrenpieTNKeEFqaklYc19FY0FkTE5Ranh6Z0tDMUNiTmt2M09tN3VhZXBXXzVFZV9LY2p0bXFteURZNG11OXQtRlFHS1VLbGh4R2hWZzRpSExva0tmSjlrNzNuYmxkVURiaC1Ic1U3XzZoQXU4WW45MHlnRnB0NUswOV9wZVlZYXdheS1R; YSC=EnaM7b8mCa4; VISITOR_INFO1_LIVE=AKZgyPgq4Aw; DEVICE_INFO=ChxOekl3TXpVME1Ua3hPRFUyTkRreE1ETTROQT09ELW+4J8GGLW+4J8G; PREF=tz=Asia.Saigon; SIDCC=AFvIBn93R2KEj77Svoeeg-z49-Cg7M6AVQrKL0GlnKhIHhLPMm6O7hvM8qm_L4devQgR29IM; __Secure-1PSIDCC=AFvIBn-bM5TUaM_Y_mb24J5tRCmv2pLF9afG33_jy3cbzNOL8my9-3vM2HhO--NwD0m3oBqaTw; __Secure-3PSIDCC=AFvIBn_a4teHfsrro2kDo7kuM7hjfomppq6fY5jvH-PBz97Wbzv6_dyYXtfVx3oemxO6qXVC; ST-132oqjv=csn=MC4yMTk3NTM3NzQzMzEyNTYx&itct=CGEQh_YEGAEiEwi8p9HRjK39AhXGumMGHWXNBEE%3D"
        auth.invoke(AuthenticatedUseCase.Params.create(cookie))
    }

    override fun submit(event: YoutubeEvent) {
        println("TEST: YoutubeMainViewModel event = $event")
        when (event) {
            is YoutubeEvent.LoadVideos -> getVideos()
            is YoutubeEvent.SetVideo -> setVideo(event.video)
            is YoutubeEvent.GetVideoDetail -> getVideoDetail(event.videoId)
            is YoutubeEvent.GetRelative -> getRelativeVidieos(event.videoId)
        }
    }

    private fun getRelativeVidieos(videoId: String) = safeLaunch {
        TODO("Not yet implemented")
    }

    private fun getVideos() = safeLaunch {
        setLoading()
        val data = loadVideos().cachedIn(scope = viewModelScope)
        setData {
            this?.copy(videos = data) ?: YoutubeState(videos = data)
        }
    }

    private fun setVideo(video: VideoEntity?) = safeLaunch {
        updateData { copy(openingVideo = video) }

        if (video != null){
            val detail = getVideo.invoke(GetVideoDetailUseCase.Params(video.id))
            updateData {
                this.copy(openingVideo = this.openingVideo?.fromVideoDetailDto(detail))
            }
        } else {
            updateData { copy(openingVideo = null) }
        }
    }

    private fun getVideoDetail(id: String) = safeLaunch {
        val detail = getVideo.invoke(GetVideoDetailUseCase.Params(id))
        updateData {
            this.copy(openingVideo = this.openingVideo?.fromVideoDetailDto(detail))
        }
    }
}