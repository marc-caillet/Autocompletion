## What is it?

This is a very basic autocomplete app. It comes together with the implementation of a sorted trie that stores
the autocomplete dictionary.



## Requirements

This app has been developped, built and tested using the following:
* Java 8
* Scala 2.11.7
* sbt 0.13.8


## How to?

#### Run the app

```
sbt run 
```
Pretty and raw prints the autocomplete dictionary; prints the total number of entries.

```
sbt "run <prefix>"
```
Prints a lexicographically sorted list of every word that share <prefix> in common.

__Example__
* `sbt "run pro"`

  List(proactive, processor, procurable, progenex, progeria, progesterone, programming,
  progressive, project free tv priceline, project runway, proud) 11 results out of 11

```
sbt "run <prefix> <max>"
```   
Prints a lexicographically sorted list of at most <max> words that share <prefix>
in common. 

__Example__
* sbt "run pro 4"`
  
  List(proactive, processor, procurable, progenex)
  4 results out of 11

#### Run the unit tests
    sbt test


# Known issues

* The app does not allow one to use her own dictionary, nor to modify it.
* The app reloads the dictionary every time it is run. It could be implemented as a web service (using Akka HTTP,
for example) that would load the dictionary at start time.
* No exception is currently handled.
* The SortedTrie class does not implement some basic functions like remove or contains. They were deemed to be beyond
the scope of this exercise.
* The SortedTrie class makes use of recursive functions, some of which are not tail recursive. Generally speaking,
it is not a good idea as it might eventually lead to a stack overflow.
* Some of these recursive functions, e.g. kwNum and kwList, traverse the entire trie. So, another and more optimal way
to code them would be to code the traversing only once then pass to it a function that would process the trie nodes.
* What differs from a sorted trie to a regular trie is that, in a sorted trie, a tree map is used to implement children
tries. Some of functions of the SortedTrie class would work the same for a regular trie. So, the current design is far
from being optimal.
* The only operation that is performed on keywords and prefix queries is lowercasing. Switching from accented to non
accented characters might also be considered.


# Q&A

* What would you change if the list of keywords was large (several millions)?   
Well, I think that a trie is pretty well suited to handling several millions of keywords, given the RAM capacity of
modern computers. Plus, the cardinality of the dictionary does not weight in the overall insert and search performance
(O(k), where k is the length of the keyword to insert or the length of the prefix). That being said, if I were to reduce
the memory footprint, I would switch the trie for a data structure with a better compression ratio. A radix tree first
comes to mind. It is basically a trie in which each single child is merged with its parent.

* What would you change if the requirements were to match any portion of the keywords (example: for string "pro", code
would possibly return "reprobe")?   
I would first write the query as a regular expression query, e.g. "*pro*". I would then model both the autocomplete
dictionary and the query as deterministic acyclic finite state automata (DAFSA). The keywords that match the query are
all the words that can be generated by the DAFSA that results from the intersection of the dictionary DAFSA and the
query DAFSA.

* At the end of the day, how would you set up an autocomplete service?  
I would definitely go with either Solr or Elasticsearch:
    * scalable web service that is able to process a very large number of queries as well as insert/delete operations
      while still processing queries
    * easy preprocessing of keywords and queries
    * regex queries are based on the Lucene automaton package