package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

sealed class UrlType () {
    sealed class UrlLocal (): UrlType() {
	object UrlLocalIpfsApi: UrlLocal()
	object UrlLocalServer: UrlLocal()
    }
    sealed class UrlRemote (): UrlType() {
	object UrlRemoteIpfs: UrlRemote()
    }
    
    override fun toString (): String {
	val result =
	    when (this) {
		is UrlType.UrlLocal.UrlLocalIpfsApi -> "UrlLocalIpfsApi"
		is UrlType.UrlLocal.UrlLocalServer -> "UrlLocalServer"
		is UrlType.UrlRemote.UrlRemoteIpfs -> "UrlRemoteIpfs"
	    }
	return result
    }

  companion object {
      fun make (wor: String): UrlType {
	  val (here, caller) = moduleHereAndCaller()
	  entering(here, caller)
	  
	  if(isTrace(here)) println ("$here: input wor '$wor'")
	  
	  val result =
	      when (wor) {
		  "localIpfsApi" -> UrlType.UrlLocal.UrlLocalIpfsApi
		  "localServer" -> UrlType.UrlLocal.UrlLocalServer 
		  "remote" -> UrlType.UrlRemote.UrlRemoteIpfs
		  else -> {
		      fatalErrorPrint("url Type were localIpfsApi|localServer|remote", "'$wor'", "Check", here)
		      }
	      }
	  if(isTrace(here)) println ("$here: output result $result")
	  
	  exiting(here)
	  return result
      }
  }
}
