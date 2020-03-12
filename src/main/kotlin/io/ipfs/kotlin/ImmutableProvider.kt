package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*

/**
 * Provision : ImmutableValue (content of an Immutable file) from its ImmutableType
 * Needs     : ImmutableType (MultiHashType (strHas)) where strHas is the hash string
 * Needed by : 
 * Done by   : LocalIpfs().get.cat(strHas)
 * Command   : gradlew run --args="-ipfs cat QmbEm7hDJ9zB22UPnXRGfaWrFoEbJZbHPTEa6udMZ48riz" 
 * Author : Emile Achadde 01 mars 2020 at 10:29:03+01:00
 * Revision : class => object by Emile Achadde 12 mars 2020 at 10:42:28+01:00
 */

class ImmutableProvider {

    val register = ImmutableRegister
    
    private fun build (immTyp: ImmutableType): ImmutableValue {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	val strHas = immTyp.hashOf()
	if(isDebug(here)) println("$here: input strHas '$strHas'")

	val str = LocalIpfs().get.cat(strHas)
	
	if(isDebug(here)) println("$here: str from LocalIpfs:")
	if(isDebug(here)) println(str)
	val result = ImmutableValue(str)
	if(isTrace(here)) println("$here: output result:")
	if(isTrace(here)) println(result.content)
	
	exiting(here)
	return result 
    }
    
    private fun buildAndStore(immTyp: ImmutableType){
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	if(isTrace(here)) println("$here: input immTyp '$immTyp'")
	
	val immCon = build(immTyp)
	register.store(immTyp, immCon)
	
	exiting(here)
    }
    
    fun provideOfImmutableType(immTyp: ImmutableType) : ImmutableValue {
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
	println()
	println("$immTyp => '$result'")
	println()
	
	exiting(here)
	return result
    }
}
