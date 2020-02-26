package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * Remark : Register(UrlType, UrlValue)
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

data class UrlValue (val host: String, val porVal: PortValue) {

    fun isEmpty (): Boolean {
	return host.isNullOrEmpty() || (porVal.toString()).isNullOrEmpty()
    }

    override fun toString (): String {
       return host + ":" + porVal.toString()
    }

}


  
