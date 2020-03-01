package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*

/**
 * Provision : IpfsImmutableValue (content of an Immutable file) from its ImmutableType
 * Needs     : IpfsImmutableType (MultiHashType (strHas)) where strHas is the hash string
 * Needed by : 
 * Done by   : LocalIpfs().get.cat(strHas)
 * Command   : gradlew run --args="-ipfs cat QmbEm7hDJ9zB22UPnXRGfaWrFoEbJZbHPTEa6udMZ48riz" 
 * Author : Emile Achadde 01 mars 2020 at 10:29:03+01:00
 */

class IpfsImmutableProvider {

    val register = IpfsImmutableRegister()
    
    fun build (immTyp: IpfsImmutableType): IpfsImmutableValue {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	val strHas = immTyp.hashOf()
	if(isDebug(here)) println("$here: input strHas '$strHas'")

	val str = LocalIpfs().get.cat(strHas)
	
	if(isDebug(here)) println("$here: input str:")
	if(isDebug(here)) println(str)
	val result = IpfsImmutableValue(str)
	if(isTrace(here)) println("$here: output result:")
	if(isTrace(here)) println(result.contentOf())
	
	exiting(here)
	return result 
    }
    
    fun buildAndStore(immTyp: IpfsImmutableType){
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	
	val immCon = build(immTyp)
	register.store(immTyp, immCon)
	
	exiting(here)
    }
    
    fun provide(immTyp: IpfsImmutableType) : IpfsImmutableValue {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	
	if (register.isStored(immTyp)){
	    register.retrieve(immTyp)
	}
	else {
	    buildAndStore(immTyp)
	}
	
	val result = register.retrieve(immTyp)
	if (isTrace(here)) println("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
}
