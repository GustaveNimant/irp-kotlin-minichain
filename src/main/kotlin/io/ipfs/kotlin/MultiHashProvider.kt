package io.ipfs.kotlin

import io.ipfs.kotlin.defaults.*

/**
 * Provide : the MultiHashValue for a 
 * Done    : LocalIpfs().add.string(str).MultiHash
 * Command : gradlew run --args="-ipfs add truc much" 
 * Command : gradlew run --args="-ipfs add /home/achadde/profile_achadde"
 * Author  : François Colonna 22 février 2020 at 10:32:18+01:00;
 */

class MultiHashProvider {

    val register = MultiHashRegister()
    
    fun build (str: String): MultiHashType {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	println("$here: input str '$str'")
	
	val strH = LocalIpfs().add.string(str).Hash
	val result = MultiHashType.make (strH)
	println("$here: output result $result")
	
	exiting(here)
	return result 
    }
    
    fun buildAndStore(str: String){
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	println("$here: input str '$str'")
	
	val mulH = build(str)
	register.store(str, mulH)
	
	exiting(here)
    }
    
    fun provide(str: String) : MultiHashType {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	println("$here: input str '$str'")
	
	if (register.isStored(str)){
	    register.retrieve(str)
	}
	else {
	    buildAndStore(str)
	}
	
	val result = register.retrieve(str)
	if (isTrace(here)) println("$here: output result '$result'")
	
	exiting(here)
	return result
    }
    
    fun print (str: String) {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	
	val hash = provide (str)
	println ("MultiHashType: $hash")
	exiting(here)
    }

    }
