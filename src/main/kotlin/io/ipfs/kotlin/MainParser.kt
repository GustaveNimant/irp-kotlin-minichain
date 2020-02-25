package io.ipfs.kotlin

import io.ipfs.kotlin.parser.*
import java.io.File
import java.util.Stack
import java.lang.Character.MIN_VALUE as nullChar

fun main(args: Array<String>) {
  val (here, caller) = hereAndCaller()
  entering(here, caller)

  val ParameterMap = parameterMapOfArguments(args)

  println ("Parameters are:")
  for ( (k, v) in ParameterMap) {
       println ("$k => $v")
  }

  writeLexemeList ()

  val lex_l = provideLexemeList ()
  val tree =
      if (hasKeywordPreviousOfLexemeList(lex_l)) {
        provideBlockCurrentMutableTreeNode()
      } else {
        provideBlockGenesisMutableTreeNode()
      }

  println("Parser tree:")
  println(tree)
  
  println("\nnormal termination")
  exiting(here)
}
