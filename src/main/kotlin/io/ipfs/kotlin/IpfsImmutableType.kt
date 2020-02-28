package io.ipfs.kotlin

/**
 * What       : The different Types of Immutable
 * Definition : An Immutable is a file adressed by its content (its hash or CID).
 * Definition : IpfsImmutableBlock
 * Definition : IpfsImmutableCode
 * Definition : IpfsImmutableFriends
 * Definition : IpfsImmutableIdentity
 * Definition : IpfsImmutableLabel
 * Definition : IpfsImmutableSmartContract
 * Definition : IpfsImmutableSymbol
 * Definition : IpfsImmutableTag
 * Definition : IpfsImmutableText
 * Author : Emile Achadde 23 février 2020 at 09:33:04+01:00
 * Revision : companion by Emile Achadde 28 février 2020 at 15:45:40+01:00
 */

sealed class IpfsImmutableType () {
    data class IpfsImmutableTypeBlock (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeCode (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeFriends (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeIdentity (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeLabel (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeSmartContract (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeSymbol (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeTag (val hash: String) : IpfsImmutableType()
    data class IpfsImmutableTypeText (val hash: String) : IpfsImmutableType()

    override fun toString (): String {
	val result =
	    when (this) {
		is IpfsImmutableTypeBlock -> "IpfsImmutableTypeBlock"
		is IpfsImmutableTypeCode -> "IpfsImmutableTypeCode"
		is IpfsImmutableTypeFriends -> "IpfsImmutableTypeFriends"
		is IpfsImmutableTypeIdentity -> "IpfsImmutableTypeIdentity"
		is IpfsImmutableTypeLabel -> "IpfsImmutableTypeLabel"
		is IpfsImmutableTypeSmartContract -> "IpfsImmutableTypeSmartContract"
		is IpfsImmutableTypeSymbol -> "IpfsImmutableTypeSymbol"
		is IpfsImmutableTypeTag -> "IpfsImmutableTypeTag"
		is IpfsImmutableTypeText -> "IpfsImmutableTypeText"
	    }
	return result
    }

    companion object {
	fun make(name: String, what: String): IpfsImmutableType {
	    val (here, caller) = hereAndCaller()
	    entering(here, caller)

	    if(isTrace(here)) println ("$here: input name '$name'")
	    if(isTrace(here)) println ("$here: input what '$what'")
	    
	    val result =
		when (name) {
		    "Block" -> IpfsImmutableTypeBlock(what)
		    "Code" -> IpfsImmutableTypeCode(what)
		    "Friends" -> IpfsImmutableTypeFriends(what)
		    "Identity" -> IpfsImmutableTypeIdentity(what)
		    "Label" -> IpfsImmutableTypeLabel(what)
		    "SmartContract" -> IpfsImmutableTypeSmartContract(what)
		    "Symbol" -> IpfsImmutableTypeSymbol(what)
		    "Tag" -> IpfsImmutableTypeTag(what)
		    "Text" -> IpfsImmutableTypeText(what)
		    else -> { fatalErrorPrint("Type is one of 'Block' 'Code' 'Friends' 'Identity' 'Label' 'SmartContract' 'Symbol' 'Tag' 'Text'", "'$name'", "Check", here)}
	    } // when
	if(isTrace(here)) println ("$here: output result '$result'")
	
	exiting(here)
	return result
	}
    } // companion 
}
