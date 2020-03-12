package io.ipfs.kotlin.defaults

import io.ipfs.kotlin.*
import io.ipfs.kotlin.url.*

/**
 * Remark : Url is provided by IRP
 * Author : Emile Achadde 25 février 2020 at 19:03:02+01:00
 */

val localIpfsConfig by lazy {
    val here = "localIpfsConfig"
    val caller = callerName()
    entering (here, caller)

    val provider = UrlProvider()
    val UrlVal = provider.provideOfUrlType(UrlType.UrlLocal.UrlLocalIpfsApi) 
    val urlStr = UrlVal.toString()
    if(isVerbose(here)) println("$here: urlStr '$urlStr'")

    val result = IpfsConfiguration("http://$urlStr/api/v0/", createOKHTTP(), createMoshi())
    if(isVerbose(here)) println("$here : output result $result")

    exiting (here)
    result
}

open class LocalIpfs : Ipfs(localIpfsConfig)
