package io.ipfs.kotlin.commands

import io.ipfs.kotlin.IpfsConnection
import io.ipfs.kotlin.model.BandWidthInfo

class PeerId(val ipfs: IpfsConnection) {

    private val peeridAdapter = ipfs.config.moshi.adapter(BandWidthInfo::class.java)

    fun peerid(): BandWidthInfo? {
        val response = ipfs.callCmd("config/peerid")
        return response.use { peeridAdapter.fromJson(it.source()) }
    }

}
