package org.mc.search.datastructure

import java.io.InputStream

import scala.annotation.tailrec
import scala.collection.immutable.TreeMap
import scala.io.Source


/**
 * A SortedTrie is a Trie that ensures lexicographic order.
 * @param char The character at this trie node
 * @param kwMark Tells if the character at this trie node is the last character of a keyword
 * @param children Lexicographically ordered children SortedTrie
 * @todo Implement methods - and -- for removing a keyword and a list of keywords, respectively
 * @todo Handle/Document exceptions!
 */
class SortedTrie private[SortedTrie](val char: Option[Char] = None,
                                     val kwMark: Boolean = false,
                                     val children: TreeMap[Char,SortedTrie] = TreeMap.empty[Char,SortedTrie])
  extends Trie {


  def loadFromStream(stream: InputStream): SortedTrie = this ++ Source.fromInputStream(stream).getLines().toList


  def ++(kwList: List[String]): SortedTrie = {

    @tailrec
    def ++@(trie: SortedTrie, kws: List[String]): SortedTrie = {

      if (kws.isEmpty)
        trie
      else
        ++@(trie + kws.head, kws.tail)
    }

    ++@(this, kwList)
  }


  def +(kw: String): SortedTrie = {

    def +@(trie: SortedTrie, w: String): SortedTrie = w.length match {

      case 0 =>
        // End of keyword has been reached: sets kwMark to true
        new SortedTrie(trie.char, true, trie.children)

      case _ =>
        trie.children get w(0) match {

          case Some(child) =>
            // A subSortedTrie already exists with the first letter as a key: updates it
            new SortedTrie(
              trie.char,
              trie.kwMark,
              trie.children - w(0) insert(w(0), +@(child, w substring 1))
            )

          case None =>
            // No subSortedTrie with the first letter as a key: adds it
            new SortedTrie(
              trie.char,
              trie.kwMark,
              trie.children insert(w(0), +@(new SortedTrie(Some(w(0))), w substring 1))
            )
        }
    }

    +@(this, kw.toLowerCase)
  }


  /**
   * Traverses this SortedTrie by following the path defined by each character of the prefix
   * @param prefix the prefix
   * @return Some(subSortedTrie) if one lies at the end of the path, None otherwise
   */
  def subSortedTrie(prefix: String): Option[SortedTrie] = {

    @tailrec
    def subSortedTrieRec(trie: SortedTrie, p: String): Option[SortedTrie] = p.length match {

      case 0 => Some(trie)

      case _ =>
        trie.children get p(0).toLower match {
          case Some(child) => subSortedTrieRec(child, p substring 1)
          case None => None
        }
    }

    subSortedTrieRec(this, prefix)
  }


  def prefixSearch(prefix: String, max: Int = Int.MaxValue): List[String] = {

    subSortedTrie(prefix) match {
      case Some(trie) => kwList(trie, prefix).take(max)
      case None => List.empty[String]
    }
  }


  /**
   * Recursively gets all the keywords contained in a given SortedTrie.
   * @param trie the trie
   * @param prefix an accumulator that stores the keyword that is being built
   * @return a list of keywords
   */
  def kwList(trie: SortedTrie = this, prefix: String = ""): List[String] = {

    def kwListRec(t: SortedTrie, acc: String, kws: List[String]): Iterable[String] = {

      t.children.isEmpty match {

        case true => kws

        case false =>
          t.children.flatMap(c => {
            val (ch, childTrie) = c
            childTrie.kwMark match {
              case true => kwListRec(childTrie, acc + ch, kws :+ (acc + ch))
              case false => kwListRec(childTrie, acc + ch, kws)
            }
          })
      }
    }

    kwListRec(trie, prefix, List.empty[String]).toList
  }


  def kwNum(prefix: String): Int = {

    subSortedTrie(prefix) match {
      case Some(trie) => kwNum(trie)
      case None => 0
    }
  }


  /**
   * Gets the number of keywords that are in a given SortedTrie.
   * @param trie the SortedTrie
   * @return the number of keywords
   */
  def kwNum(trie: SortedTrie = this): Int = {

    def kwNumRec(t: SortedTrie, num: List[Int] = List[Int](0)): Iterable[Int] = {

      t.children.isEmpty match {

        case true => num

        case false =>
          t.children.flatMap(c => {
            val childTrie = c._2
            childTrie.kwMark match {
              case true => kwNumRec(childTrie, num :+ 1)
              case false => kwNumRec(childTrie, num)
            }
          })
      }
    }

    kwNumRec(trie).sum
  }


  def prettyPrint(): Unit = {

    def prettyPrintRec(trie: SortedTrie, indent: Int): Unit = {

      trie.char match {

        case Some(ch) =>
          trie.kwMark match {
            case true => println(" " * indent + ch + " *")
            case false => println(" " * indent + ch)
          }

        case None =>
      }

      trie.children.foreach(c => prettyPrintRec(c._2, indent + 2))
    }

    prettyPrintRec(this, 0)
  }


  def dictionary: List[String] = kwList()


  def cardinality: Int = kwNum()
}


/**
 * A companion object to the SortedTrie class which exposes a constructor without parameters.
 */
object SortedTrie {

  /**
   * Force instanciation of a SortedTrie with default constructor parameter values
   * @return A SortedTrie with a root node.
   */
  def apply() = new SortedTrie()
}