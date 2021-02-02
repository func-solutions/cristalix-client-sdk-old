package ru.cristalix.uiengine.utility

import dev.xdark.clientapi.ClientApi

data class ScreenState(
    val scaleFactor: Int,
    val screenWidth: Int,
    val screenHeight: Int,
    val unicodeFlag: Boolean,
) {
    companion object {
        fun getScreenState(clientApi: ClientApi): ScreenState {
            val resolution = clientApi.resolution()
            val scaleFactor = resolution.scaleFactor
            val screenWidth = resolution.scaledWidth
            val screenHeight = resolution.scaledHeight
            val unicodeFlag = clientApi.fontRenderer().unicodeFlag

            return ScreenState(
                scaleFactor,
                screenWidth,
                screenHeight,
                unicodeFlag
            )
        }
    }

}
