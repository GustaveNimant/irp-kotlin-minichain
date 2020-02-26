package io.ipfs.kotlin.url

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack

/**
 * Remark : 
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

sealed class UrlType 
  sealed class UrlLocal: UrlType()
    object UrlLocalIpfsApi: UrlLocal()
    object UrlLocalServer: UrlLocal()
  sealed class UrlRemote: UrlType()
    object UrlRemoteIpfs: UrlRemote()
