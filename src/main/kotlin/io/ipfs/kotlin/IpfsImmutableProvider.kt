package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*

/**
 * Provision : the content of an immutable file from its MultiHash
 * Done      : LocalIpfs().get.cat(immTyp)
 * Needs     : MultiHash 
 * Needed by : MultiHash 
 * Command   : gradlew run --args="-ipfs get QmbEm7hDJ9zB22UPnXRGfaWrFoEbJZbHPTEa6udMZ48riz" 
 * Author : François Colonna 22 février 2020 at 11:02:59+01:00
 */

class IpfsImmutableProvider {

    val register = IpfsImmutableRegister()
    
    fun build (immTyp: IpfsImmutableType): IpfsImmutableValue {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	println("$here: input immTyp '$immTyp'")

	val strH = immTyp.toString()
	val str = LocalIpfs().get.cat(strH)

	val result = IpfsImmutableValue(str)
	println("$here: output result $result")
	
	exiting(here)
	return result 
    }
    
    fun buildAndStore(immTyp: IpfsImmutableType){
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	println("$here: input immTyp '$immTyp'")
	
	val immCon = build(immTyp)
	register.store(immTyp, immCon)
	
	exiting(here)
    }
    
    fun provide(immTyp: IpfsImmutableType) : IpfsImmutableValue {
	val (here, caller) = hereAndCaller()
	entering(here, caller)
	
	println("$here: input immTyp '$immTyp'")
	
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
