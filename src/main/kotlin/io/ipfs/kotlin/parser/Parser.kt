package io.ipfs.kotlin.parser
// The Parser : List of Lexemes => Tree of Domain Entities

import io.ipfs.kotlin.*
import java.io.File
import java.util.Stack
import java.lang.Character.MIN_VALUE as nullChar

fun blockKindOfMetaLexemeList (met_l: List<Lexeme>): String {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    var result = "genesis"
    for (lex in met_l) {
      	if (lex is Lexeme.KeywordWithQmHash && lex.name == "previous") {	
       	   result = "current"
	   break
      }
    }	

    exiting(here)
    return result
}

fun leafedNodeAndStackOfLexemeMetaStack (lex_met_s: Stack<Lexeme>): Pair<MutableTreeNode<String>, Stack<Lexeme>> {
// Set up a Leafed Node (ex.: qm / z2....)
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    var nodMut = MutableTreeNode<String>("")
    var Done = false
    
    while (! Done) {
      try {
        var lex = lex_met_s.pop()
        if (isDebug(here)) println ("$here: while lex '$lex'")

      	when (lex) {
      	  is Lexeme.TokenEndOfLine -> {
	     Done=true
	     if (isDebug(here)) println ("$here: while EndOfLine reached")
	  }
     	  is Lexeme.KeywordWithDate -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
     	  is Lexeme.KeywordWithFile -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
	  is Lexeme.KeywordWithQmHash -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
	  is Lexeme.KeywordWithString -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
	  is Lexeme.KeywordWithZ2Hash -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
     	  is Lexeme.KeywordWithInteger -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
	  is Lexeme.KeywordWithPersonName -> {
	    var nod_nam = lex.name
	    nodMut = MutableTreeNode<String>(nod_nam)
	  }
	  is Lexeme.AuthorName -> {
	    var lea_val = lex.name
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.NextName -> {
	    var lea_val = lex.name
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.FilePath -> {
	    var lea_val = lex.name
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
          }
	  is Lexeme.DateValue -> {
	    var lea_val = lex.value
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.QmHash -> {
	    var lea_val = lex.hash
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.Z2Hash -> {
	    var lea_val = lex.hash
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.Signature -> {
	    var lea_val = lex.value
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.Spot -> {
	    var lea_val = lex.value
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.Tic  -> {
	    var lea_val = lex.value
	    var leaf = MutableTreeNode<String>(lea_val)
	    nodMut.addChild(leaf)
	  }
	  is Lexeme.TokenDollar, is Lexeme.TokenVee, is Lexeme.TokenSharp, is Lexeme.TokenSpace -> {
	    if(isVerbose(here)) println ("$here: lexeme skipped '$lex'")
	    MutableTreeNode<String>("skipped")
	  }
	  else -> {
	    if(isVerbose(here)) println ("$here: lexeme skipped '$lex'")
	    MutableTreeNode<String>("skipped")
	  }
       }
       }
      catch (e:java.util.EmptyStackException) {Done = true }
    }

    if (isTrace(here)) println ("$here: output nodMut $nodMut")
    if (isTrace(here)) println ("$here: output lex_met_s '$lex_met_s'")
	
    exiting(here)
    return Pair (nodMut, lex_met_s)
}

fun provideBlockCurrentMutableTreeNode () : MutableTreeNode<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// <BlockCurrent> ::= <TreeMeta> <TreeText>

    val tree = MutableTreeNode<String> ("block-current")
    val treeMeta = provideBlockMetaMutableTreeNode ()
    val treeText = provideBlockTextMutableTreeNode ()

    tree.addChild (treeMeta)
    tree.addChild (treeText)

    if (isTrace(here)) println ("$here: output tree '$tree'")	
    exiting(here)
    return tree
}

fun provideBlockGenesisMutableTreeNode () : MutableTreeNode<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// <BlockGenesis> ::= <TreeMeta> <TreeText>

    val tree = MutableTreeNode<String> ("block-genesis")
    val treeMeta = provideBlockMetaMutableTreeNode ()
    val treeText = provideBlockTextMutableTreeNode ()

// Building 
    tree.addChild(treeMeta)
    tree.addChild(treeText)

    exiting(here)
    return tree
}

fun provideBlockMetaMutableTreeNode () : MutableTreeNode<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// <TreeMeta> ::= TreeMetaRecordList ::= { TreeMetaRecord }

    val tree = MutableTreeNode<String> ("block-meta")
    val nod_l = provideTreeMetaRecordList ()

    for (nod in nod_l) {
    	tree.addChild (nod)
    }

    if (isTrace(here)) println ("$here: output tree '$tree'")	
    exiting(here)
    return tree
}

fun provideBlockTextMutableTreeNode () : MutableTreeNode<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// <TreeText> ::= TreeTextRecordList

    val tree = MutableTreeNode<String> ("block-text")
    val nod_l = provideTreeTextRecordList ()

    for (nod in nod_l) {
    	tree.addChild (nod)
    }

    if (isTrace(here)) println ("$here: output tree '$tree'")	
    exiting(here)
    return tree
}

fun provideMetaLexemeList () : List<Lexeme> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val lex_l = provideLexemeList ()

    var metaList = mutableListOf<Lexeme>()
    var is_meta = false

    for (lex in lex_l) {
    	if (isDebug(here)) if (isDebug(here)) println ("$here: for lex '$lex'")

	if (lex is Lexeme.TokenSharp) {
	   is_meta = true
	}
	
	if (is_meta) {
	   metaList.add (lex)
	   if (isDebug(here)) println ("$here: added lex '$lex'")
	}
	
	if (is_meta && (lex is Lexeme.TokenEndOfLine)){
	   is_meta = false
	   if (isDebug(here)) println ("$here: meta set to false")
	}
    }
    
    if (isTrace(here)) println ("$here: output metaList "+ fullnameListOfLexemeList(metaList))
    
    exiting(here)
    return metaList
}

fun provideRecordTextList () : List<String> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// A record is enclosed between two TokenEndOfLine
// Record are rebuilt from Lexemes and Not Parsed Yet
// Need to interpolate variables

    val lex_l = provideTextLexemeList ()

    var rec = ""
    var rec_l = mutableListOf<String>()
	
    for (lex in lex_l) {
        if (isDebug(here)) println ("$here: for lex '$lex'")	
        when (lex) {
       	   is Lexeme.TokenEndOfLine -> {
	      rec_l.add (rec)
	      rec = ""
	   }
	   else -> {
	        var str = stringValueOfLexeme (lex) 
	   	rec = rec + str
		if (isDebug(here)) println ("$here: for str '$str'")	
		if (isDebug(here)) println ("$here: for rec '$rec'")	
		}
      }
    }

    val result = rec_l.toList()
    if (isTrace(here)) println ("$here: output result '$result'")	
    exiting(here)
    return result
}

fun provideTextLexemeList () : List<Lexeme> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

    val lex_l = provideLexemeList ()

    var textList = mutableListOf<Lexeme>()
    var is_meta = false

    for (lex in lex_l) {
    	if (isDebug(here)) if (isDebug(here)) println ("$here: for lex '$lex'")

	if (lex is Lexeme.TokenSharp) {
	   is_meta = true
	}
	
	if (is_meta && (lex is Lexeme.TokenEndOfLine)){
	   is_meta = false
	   if (isDebug(here)) println ("$here: meta set to false")
	}

	if (! is_meta) {
	   textList.add (lex)
	   if (isDebug(here)) println ("$here: added lex '$lex'")
	}
	
    }
    
    if (isTrace(here)) println ("$here: output textList "+ fullnameListOfLexemeList(textList))
    
    exiting(here)
    return textList
}

fun provideTreeMetaRecordList () : List<MutableTreeNode<String>> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// <TreeMetaRecordList> ::= { <TreeMetaRecord> }
// <TreeMetaRecord>     ::= Node(Record)-Leaf(value)
//         Source        Date
//           |            |
//        file_path   dd/mm/yyyy

    val lex_met_l = provideMetaLexemeList ()
    var lex_met_s = teeStackOfTeeList (lex_met_l)
    
    var tree_l = mutableListOf<MutableTreeNode<String>>()
    var Done = false
    
    while (! Done) {
      try {	  
      	var lex = lex_met_s.pop()
      	if (isDebug(here)) println ("$here: while lex '$lex'")
      	if (lex is Lexeme.TokenSharp) {
	  var (tree, lex_s) = leafedNodeAndStackOfLexemeMetaStack (lex_met_s)
	  tree_l.add(tree)
	  lex_met_s = lex_s
	  if (isDebug(here)) println ("$here: while added tree '$tree")	
	  if (isDebug(here)) println ("$here: while lex_met_s '$lex_met_s")	
        }
      }
      catch (e:java.util.EmptyStackException) {
        println ("$here: end of Stack reached")	
        Done = true
      }
    }
    if (isTrace(here)) println ("$here: output tree_l '$tree_l'")	

    exiting(here)
    return tree_l
}

fun provideTreeTextRecordList () : List<MutableTreeNode<String>> {
    val (here, caller) = hereAndCaller()
    entering(here, caller)

// <TreeTextRecordList> ::= { <TreeTextRecord> }

    val nam_l = provideRecordTextList ()

    var rec_tl = mutableListOf<MutableTreeNode<String>>()   
    for (nam in nam_l) {
        var nod = MutableTreeNode<String>(nam)
    	rec_tl.add (nod)
    }

    val tree_l = rec_tl.toList()
    if (isTrace(here)) println ("$here: output tree_l '$tree_l'")

    exiting(here)
    return tree_l
}

