package com.tekskills.st_tekskills.utils

import com.tekskills.st_tekskills.utils.Common.Companion.INITIALIZE_CONFIG


object ConfigProvider {

    private var configuration: CTConfig? = null

    fun setConfiguration(config: CTConfig) {
        configuration = config
    }

    fun getConfiguration() = configuration ?:
    throw IllegalStateException(INITIALIZE_CONFIG)
}