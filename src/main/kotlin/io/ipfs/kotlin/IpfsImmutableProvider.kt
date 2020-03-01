package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*

/**
 * Provision : the content of an immutable file from its MultiHash
 * Done      : LocalIpfs().get.cat(immTyp)
 * Needs     : MultiHash 
 * Needed by : MultiHash 
 * Command   : gradlew run --args="-ipfs get block QmbEm7hDJ9zB22UPnXRGfaWrFoEbJZbHPTEa6udMZ48riz" 
 * Author : Emile Achadde 22 février 2020 at 11:02:59+01:00
 */

class IpfsImmutableProvider {

    val register = IpfsImmutableRegister()
    
    fun build (immTyp: IpfsImmutableType): IpfsImmutableValue {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	val mulH = immTyp.multiHashOf()
	println("$here: input mulH '$mulH'")
	val strH = mulH.hashOf()
	println("$here: input strH '$strH'")
	val str = LocalIpfs().get.cat(strH)
	println("$here: input str '$str'")
	val result = IpfsImmutableValue(str)
	if(isTrace(here)) println("$here: output result $result")
	
	exiting(here)
	return result 
    }
    
    fun buildAndStore(immTyp: IpfsImmutableType){
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	
	val immCon = build(immTyp)
	register.store(immTyp, immCon)
	
	exiting(here)
    }
    
    fun provide(immTyp: IpfsImmutableType) : IpfsImmutableValue {
	val (here, caller) = hereAndCaller()
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
