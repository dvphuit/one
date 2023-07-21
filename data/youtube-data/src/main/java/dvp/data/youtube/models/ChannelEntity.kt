package dvp.data.youtube.models

import dvp.data.youtube.extension.toThumbnail
import dvp.lib.ytube.dto.init.InitResponse

data class ChannelEntity(
    val id: String,
    val name: String,
    val thumbnail: ThumbnailEntity?,
) {
    companion object {
        fun fromDto(
            owner: InitResponse.Text?,
            thumbnail: InitResponse.Thumbnail?
        ): ChannelEntity? {
            if (owner == null && thumbnail == null) return null

            return ChannelEntity(
                id = owner!!.runs?.firstOrNull()?.navigationEndpoint?.browseEndpoint?.browseId
                    ?: "",
                name = owner.runs?.firstOrNull()?.text ?: "",
                thumbnail = thumbnail?.toThumbnail()
            )
        }
    }
}