package io.ipfs.kotlin

/**
 * What is it : The different Types of Immutable files
 * Definition : An Immutable is a file adressed by its content i.e. its hash or CID on Ipfs.
 * Definition : An IpfsImmutableType is parametrized by the Type of its Hash (MultiHashType)
 * Definition : IpfsImmutableBlock
 * Definition : IpfsImmutableCode
 * Definition : IpfsImmutableFriends
 * Definition : IpfsImmutableIdentity
 * Definition : IpfsImmutableLabel
 * Definition : IpfsImmutableSmartContract
 * Definition : IpfsImmutableSymbol
 * Definition : IpfsImmutableTag
 * Definition : IpfsImmutableText
 * Abbreviation : immTyp 
 * Author : Emile Achadde 23 février 2020 at 09:33:04+01:00
 * Revision : companion by Emile Achadde 28 février 2020 at 15:45:40+01:00
 * Revision : hashOf() by Emile Achadde 01 mars 2020 at 13:03:06+01:00
 */

sealed class IpfsImmutableType () {
    data class IpfsImmutableTypeBlock (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeProgram (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeFriends (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeIdentity (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeLabel (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeSmartContract (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeSymbol (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeTag (val multiHash: MultiHashType) : IpfsImmutableType()
    data class IpfsImmutableTypeText (val multiHash: MultiHashType) : IpfsImmutableType()

    fun hashOf (): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)

	val mulHas = multiHashOf()
	val result = mulHas.hashOf()

	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }

    fun multiHashOf(): MultiHashType {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)

	val result =
	    when (this) {
		is IpfsImmutableTypeBlock -> this.multiHash
		is IpfsImmutableTypeProgram -> this.multiHash
		is IpfsImmutableTypeFriends -> this.multiHash
		is IpfsImmutableTypeIdentity -> this.multiHash
		is IpfsImmutableTypeLabel -> this.multiHash 
		is IpfsImmutableTypeSmartContract -> this.multiHash
		is IpfsImmutableTypeSymbol -> this.multiHash
		is IpfsImmutableTypeTag -> this.multiHash 
		is IpfsImmutableTypeText -> this.multiHash 
	    }
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }

    fun nameOf (): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	val mulHas = multiHashOf() 
	val result =
	    when (this) {
		is IpfsImmutableTypeBlock -> "IpfsImmutableTypeBlock"
		is IpfsImmutableTypeProgram -> "IpfsImmutableTypeProgram"
		is IpfsImmutableTypeFriends -> "IpfsImmutableTypeFriends"
		is IpfsImmutableTypeIdentity -> "IpfsImmutableTypeIdentity"
		is IpfsImmutableTypeLabel -> "IpfsImmutableTypeLabel"
		is IpfsImmutableTypeSmartContract -> "IpfsImmutableTypeSmartContract"
		is IpfsImmutableTypeSymbol -> "IpfsImmutableTypeSymbol"
		is IpfsImmutableTypeTag -> "IpfsImmutableTypeTag"
		is IpfsImmutableTypeText -> "IpfsImmutableTypeText"
	    }
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }

    fun stringOf (): String {
	val (here, caller) = moduleHereAndCaller()
	entering(here, caller)
	val mulHas = multiHashOf()
	val hash = mulHas.hashOf()
	val result = 
	    when (this) {
		is IpfsImmutableTypeBlock -> "IpfsImmutableTypeBlock("+hash+")"
		is IpfsImmutableTypeProgram -> "IpfsImmutableTypeProgram("+hash+")"
		is IpfsImmutableTypeFriends -> "IpfsImmutableTypeFriends("+hash+")"
		is IpfsImmutableTypeIdentity -> "IpfsImmutableTypeIdentity("+hash+")"
		is IpfsImmutableTypeLabel -> "IpfsImmutableTypeLabel("+hash+")"
		is IpfsImmutableTypeSmartContract -> "IpfsImmutableTypeSmartContract("+hash+")"
		is IpfsImmutableTypeSymbol -> "IpfsImmutableTypeSymbol("+hash+")"
		is IpfsImmutableTypeTag -> "IpfsImmutableTypeTag("+hash+")"
		is IpfsImmutableTypeText -> "IpfsImmutableTypeText("+hash+")"
	    }

	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
    }

    companion object {
	fun make(typ: String, mulHas: MultiHashType): IpfsImmutableType {
	    val (here, caller) = moduleHereAndCaller()
	    entering(here, caller)

	    if(isTrace(here)) println ("$here: input typ '$typ'")
	    if(isTrace(here)) println ("$here: input mulHas '$mulHas'")

	    val typLow = typ.toLowerCase()
	    val result =
		when (typLow) {
		    "block" -> IpfsImmutableTypeBlock(mulHas)
		    "code" -> IpfsImmutableTypeProgram(mulHas)
		    "friends" -> IpfsImmutableTypeFriends(mulHas)
		    "identity" -> IpfsImmutableTypeIdentity(mulHas)
		    "mabel" -> IpfsImmutableTypeLabel(mulHas)
		    "smartContract" -> IpfsImmutableTypeSmartContract(mulHas)
		    "symbol" -> IpfsImmutableTypeSymbol(mulHas)
		    "tag" -> IpfsImmutableTypeTag(mulHas)
		    "text" -> IpfsImmutableTypeText(mulHas)
		    else -> { fatalErrorPrint("Type is one of 'Block' 'Code' 'Friends' 'Identity' 'Label' 'SmartContract' 'Symbol' 'Tag' 'Text'", "'$typ'", "Check", here)}
	    } // when
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
	}
    } // companion 
}
