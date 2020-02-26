package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * What is it : the Value of a Host
 * Example : 127.0.0.1 
 * What to do : provide host and port by asking if stored in ParameterMap 
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

data class HostValue (val host: String) {

    fun isEmpty (): Boolean {
	return host.isNullOrEmpty()
    }

}


  