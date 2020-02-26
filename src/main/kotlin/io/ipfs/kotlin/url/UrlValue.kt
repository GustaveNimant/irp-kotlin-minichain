package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * Remark : Register(UrlType, UrlValue)
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

data class UrlValue (val host: String, val port: Int) {

    fun isEmpty (): Boolean {
	return host.isNullOrEmpty() || (port.toString()).isNullOrEmpty()
    }

    override fun toString (): String {
       return host + ":" + port.toString()
    }

    fun printOfUrlType (UrlTyp: UrlType) {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	val url = UrlProvider.provideUrl (UrlTyp)
	val str = url.toString()
	println ("UrlType $UrlTyp => $str")
	exiting(here)
    }
}


  
