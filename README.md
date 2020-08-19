# clj-diff

This is a fork of a fork clj-diff as used by lambdaisland/deep-diff2. We
switched to the fork released by user rymndhng because it added ClojureScript
support (ported the code to `cljc`).

So this repo and the lambdaisland/clj-diff artifact on Clojars basically contain
the same code as [org.clojars.rymndhng/clj-diff
1.1.1-SNAPSHOT](https://clojars.org/org.clojars.rymndhng/clj-diff/versions/1.1.1-SNAPSHOT).

However, that has been a SNAPSHOT release since 2015, we don't want to continue
to depend on a SNAPSHOT release which may still change. It's also not pleasant
for downstream users. Moreover, the commit that that jar references does not
exist on Github, so this repo pulls out the code from the jar and commits it for
reference.

Namespaces have been prefixed with `lambdaisland.` so this artifact can co-exist
with the original clj-diff (or the earlier fork) on the same classpath. We've
also taken some liberties to modernize namespace forms, and to organize this
repo in a similar way to other lambdaisland projects (deps.edn, kaocha, common
release tooling, etc.).

Having our own fork of this might also allow us to evolve this library later on,
although that is not currently a concern and we merely release the functionality
that was already there.

<!-- installation -->
<!-- /installation -->

Below is the original README

-----

Provides <code>diff</code> and <code>patch</code> functions for Clojure
sequences where <code>(diff a b) -\> x</code> and <code>(patch a x) -\>
b</code>. Also provides <code>edit-distance</code> and
<code>levenshtein-distance</code> functions for calculating the
difference between two sequences.

## Usage

```
user=> (use 'clj-diff.core)
user=> (diff "John went to the movies." "John was in the movies.")
{:+ [[5 \a \s \space \i]], :- [6 8 9 10 11]}
user=> (patch "John went to the movies." *1)
"John was in the movies."
user=> (edit-distance "John went to the movies." "John was in the movies."))
9
user=> (levenshtein-distance "John went to the movies." "John was in the movies.")
8
```

There is already a [Java
library](http://code.google.com/p/google-diff-match-patch/) which does
this well. Why create a Clojure version? So that we can do this:

```clojure
user=> (def a [{:a 1} {:a 2} {:a 3} {:a 4} {:a 5} {:a 6} {:a 7}])
user=> (def b [{:a 2} {:a 3} {:a 4} {:a 5} {:a 6} {:a 7} {:a 1}])
user=> (diff a b)
{:+ [[6 {:a 1}]], :- [0]}
user=> (patch a *1)
({:a 2} {:a 3} {:a 4} {:a 5} {:a 6} {:a 7} {:a 1})
user=> (edit-distance a b)
2
```

## Notes

The current diff algorithm comes from the paper [An O (NP) Sequence
Comparison Algorithm](http://portal.acm.org/citation.cfm?id=96223) by
Sun Wu, Udi Manber, Gene Myers and Webb Miller. It is fast and memory
efficient. It also makes use of the pre-diff optimizations mentioned in
Neil Fraser's [Diff Strategies](http://neil.fraser.name/writing/diff/).
The worst-case running time of the algorithm is dependent only on the
length of the longest sequence (N) and the number of deletions (P). This
is much better than the
[Myers](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.4.6927&rep=rep1&type=pdf)
algorithm which is an O (ND) algorithm where N is the sum of the length
of the two sequences and D is the edit distance.

I have created a [separate
project](http://github.com/brentonashworth/clj-diff-performance) for
comparing the performance of different diff algorithms. The main
performance goal of this project is to create a Clojure diff that can
outperform Fraser's Java implementation. One of the most interesting
results is show below.

<img src="http://s3.amazonaws.com/formpluslogic-public/images/clj-diff/length_7000_5.png"/>

This chart shows the most interesting range of comparison between the
three algorithms. The current algorithm being used by clj-diff is
labeled "Miller". For larger sequences, the Miller algorithm outperforms
Fraser. To summarize all of the performance test results; the Miller
algorithm is never more than 50 milliseconds slower than the Fraser
algorithm but the Fraser algorithm can be multiple seconds slower, and
will run out of memory more quickly, for large sequences. For more
information about performance see the
[clj-diff-performance](http://github.com/brentonashworth/clj-diff-performance)
project.


## References
----------

-   [An O (ND) Difference Algorithm and Its Variations by Eugene W.
    Myers](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.4.6927&rep=rep1&type=pdf)
-   [An O (NP) Sequence Comparison Algorithm by Sun Wu, Udi Manber, Gene
    Myers and Webb Miller](http://portal.acm.org/citation.cfm?id=96223)

In order to understand the above two titles, you will need to know what
N, D and P stand for. Let A and B be two sequences with lengths Q and M
where Q \>= M. Let D be the length of the minimum edit script for A and
B. In the title of the first paper N = Q + M. In the second paper N = Q
and P = (1/2)D - (1/2)(Q - M). Therefore, the O (NP) version is faster
which is visualized in the charts below.

-   [Diff Strategies by Neil
    Fraser](http://neil.fraser.name/writing/diff/)

## License

Copyright © 2010-2011 Brenton Ashworth

Distributed under the Eclipse Public License, the same as Clojure uses.
See the file LICENSE.txt.
