package org.mc.search.datastructure

import java.io.InputStream

/**
 * A Trie is a prefix tree.
 * A common application is storing a dictionary for autocompletion.
 *
 * Todo:
 * <ul>
 *   <li>Add methods - and -- for removing a keyword and a list of keywords, respectively</li>
 *   <li>Add way more documentaton about how a Trie is being built, how it compresses prefixes and so forth</li>
 *   <li>Also give some examples</li>
 * </ul>
 */
trait Trie extends {

  /**
   * Adds every keyword from a stream to this SortedTrie.
   * @param stream the stream that transmits the keywords to add to this SortedTrie
   * @return this trie with the addition of the new keywords
   */
  def loadFromStream(stream: InputStream): Trie

  /**
   * Adds a keyword to this SortedTrie.
   * @param kw the keyword to add to this SortedTrie
   * @return this try with the addition of the keyword
   */
  def +(kw: String): Trie

  /**
   * Adds every keyword from a list of strings to this SortedTrie.
   * @param kwList the list of keywords to be added to this SortedTrie
   * @return this trie with the addition of the new keywords
   */
  def ++(kwList: List[String]): Trie

  /**
   * Gets every keyword in this SortedTrie that starts by the prefix
   * @param prefix the prefix
   * @param max optional, maximum of keywords to be returned
   * @return a list of every keyword that starts with the prefix
   */
  def prefixSearch(prefix: String, max: Int = Int.MaxValue): List[String]

  /**
   * Gets the number of keywords that share the prefix in common.
   * @param prefix the prefix
   * @return the number of keywords
   */
  def kwNum(prefix: String): Int

  /**
   * Beautifully prints this SortedTrie.
   * An '*' is printed at the end of each keyword.
   */
  def prettyPrint(): Unit

  /**
   * Gets every keywords in this trie.
   * @return a list of every keywords
   */
  def dictionary: List[String]

  /**
   * Get the number of keywords in this trie.
   * @return the number of keywords
   */
  def cardinality: Int
}
