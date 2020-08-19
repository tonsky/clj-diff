(ns lambdaisland.clj-diff.optimizations-test
  (:require [clojure.test :refer :all]
            [lambdaisland.clj-diff.optimizations :refer :all]))

(deftest common-prefix-test
  (is (= (common-prefix "abcdef" "abcxyz")
         [3 "def" "xyz"]))
  (is (= (common-prefix "xy" "ab")
         [0 "xy" "ab"]))
  (is (= (common-prefix "ab" "ab")
         [2 "" ""])))

(deftest common-suffix-test
  (is (= (common-suffix "defabc" "xyzabc")
         [3 "def" "xyz"]))
  (is (= (common-suffix "xy" "ab")
         [0 "xy" "ab"]))
  (is (= (common-suffix "ab" "ab")
         [2 "" ""])))

(deftest diff*-test
  (let [t (fn [a b] (#'lambdaisland.clj-diff.optimizations/diff* a b (constantly nil)))]
    (is (= (t "" "abc")
           {:+ [[-1 \a \b \c]]
            :- []}))
    (is (= (t "abc" "")
           {:+ []
            :- [0 1 2]}))
    (is (= (t "abc" "xyzabcmnop")
           {:+ [[-1 \x \y \z] [5 \m \n \o \p]]
            :- []}))
    (is (= (t "abc" "abcm")
           {:+ [[2 \m]]
            :- []}))
    (is (= (t "abcm" "abc")
           {:+ []
            :- [3]}))
    (is (= (t "abc" "mabc")
           {:+ [[-1 \m]]
            :- []}))
    (is (= (t "mabc" "abc")
           {:+ []
            :- [0]}))
    (is (= (t "mabc" "abc")
           {:+ []
            :- [0]}))
    (is (nil? (t "abcac" "cbab")))))

(deftest half-match-test
  (let [t #'lambdaisland.clj-diff.optimizations/half-match]
    (is (= (t "a" "b")
           nil))
    (is (= (t "bb" "bbg")
           nil))
    (is (= (t "ahgt" "bhahgtgbh")
           nil))
    (is (= (t "aaapppppb" "cpppppdddd")
           ["ppppp" "aaa" "b" "c" "dddd"]))
    (is (= (t "apppppaab" "pppppcdddd")
           ["ppppp" "a" "aab" "" "cdddd"]))
    (is (= (t "apppppaab" "cddddppppp")
           ["ppppp" "a" "aab" "cdddd" ""]))
    (is (= (t "aaappppbb" "cppppddddd")
           nil))))
