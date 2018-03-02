package org.mc.search.app

import org.mc.search.datastructure.SortedTrie

/**
 * A very basic autocompletion app that loads /dictionary.txt then:
 * <ul>
 * <li>args.length = 0, pretty and raw prints the dictionary; prints the cardinality</li>
 * <li>args.length = 1, prints all the words that have prefix args(0) in common</li>
 * <li>args;length = 2, prints the args(1) first words, in lexicographic order, that have prefix args(0) in common</li>
 * </ul>
 */
object Autocompletion extends App {

  var trie = SortedTrie().loadFromStream(getClass.getResourceAsStream("/dictionary.txt"))

  args.length match {

    case 0 =>
      trie.prettyPrint()
      println(trie.dictionary)
      println(trie.cardinality)

    case 1 =>
      val words = trie.prefixSearch(args(0))
      println(words)
      println(words.length + " results out of " + trie.kwNum(args(0)))

    case 2 =>
      val words = trie.prefixSearch(args(0), args(1).toInt)
      println(words)
      println(words.length + " results out of " + trie.kwNum(args(0)))

    case _ => println(
      s"""
         |Usage:
         |sbt "run prefix max"
         |  prefix The prefix
         |  max The maximum number of words (optional)
       """.stripMargin)
      sys.exit(1)
  }
}
