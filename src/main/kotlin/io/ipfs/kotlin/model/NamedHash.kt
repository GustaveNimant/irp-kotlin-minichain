package io.ipfs.kotlin.model

data class NamedHash(val Name: String,
                     val Hash: String) {

    override fun toString(): String {
	return Name + ":" + Hash
	}
    }
