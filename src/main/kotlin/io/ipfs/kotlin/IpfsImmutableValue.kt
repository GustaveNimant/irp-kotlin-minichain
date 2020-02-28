package io.ipfs.kotlin

/**
 * What   : the content of an Immutable as a string  
 * Author : François Colonna 22 février 2020 at 10:50:52+01:00
 */

class IpfsImmutableValue (val content: String) {

    override fun toString(): String {
	return content
    }
    
}
