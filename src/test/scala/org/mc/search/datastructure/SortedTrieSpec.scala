package org.mc.search.datastructure

import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source

class SortedTrieSpec extends FlatSpec with Matchers {

  val numLines = Source.fromInputStream(getClass.getResourceAsStream("/dictionary.txt")).getLines().length
  val trie = SortedTrie().loadFromStream(getClass.getResourceAsStream("/dictionary.txt"))

  "A SortedTrie" should "contain no keyword when it is empty" in {
    val emptyTrie = SortedTrie()
    emptyTrie.dictionary should be (List.empty[String])
    emptyTrie.cardinality should be (0)
  }

  it should "contain the keyword that has been added to it and nothing else" in {
    val oneTrie = SortedTrie() + "owl"
    oneTrie.dictionary should be (List[String]("owl"))
    oneTrie.cardinality should be (1)
  }

  it should "contain every keyword of a list that has been added to it and nothing else" in {
    val listTrie = SortedTrie() ++ List[String]("owl", "phone", "bowl")
    listTrie.dictionary should be (List[String]("bowl","owl","phone"))
    listTrie.cardinality should be (3)
  }

  it should "be able to load every keyword from a stream" in {
    trie.cardinality should be (numLines)
  }

  it should "be able to retrieve every possible keyword, given a prefix" in {
    trie.kwNum("p") should be (19)
    trie.kwNum("pr") should be (14)
    trie.kwNum("o") should be (1)
  }

  it should "not give more than the maximum specified number of keywords" in {
    trie.prefixSearch("p", 4).size should be (4)
    trie.prefixSearch("pr", 4).size should be (4)
    trie.prefixSearch("o", 4).size should be (1)
  }

  it should "order the keywords in ascending lexicographic order" in {
    trie.prefixSearch("p", 4) should be (List[String]("pandora", "paypal", "pg&e", "phone"))
    trie.prefixSearch("pr", 4) should be (List[String]("prank", "press democrat", "print", "proactive"))
    trie.prefixSearch("pro", 4) should be (List[String]("proactive", "processor", "procurable", "progenex"))
    trie.prefixSearch("prog", 4) should be (List[String]("progenex", "progeria", "progesterone", "programming"))
  }

  it should "give an empty list of strings when the prefix does not exist in the SortedTrie" in {
    trie.prefixSearch("bm") should be (List.empty[String])
  }
}
