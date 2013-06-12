;; Copyright (c) 2013 Dylon Edwards <dylon.edwards@gmail.com>
;;
;; Permission is hereby granted, free of charge, to any person obtaining a copy
;; of this software and associated documentation files (the "Software"), to deal
;; in the Software without restriction, including without limitation the rights
;; to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
;; copies of the Software, and to permit persons to whom the Software is
;; furnished to do so, subject to the following conditions:
;;
;; The above copyright notice and this permission notice shall be included in
;; all copies or substantial portions of the Software.
;;
;; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;; FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
;; AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;; LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
;; SOFTWARE.

(ns umlang.test.number
  (:use umlang.number
        midje.sweet))

(defn test->basic [ctor value]
  (let [number (ctor value)]
    (fact "a number constructed from another should be equivalent"
      (ctor value) => number)
    (fact "numbers with the same value should be equivalent"
      (ctor value) => (ctor value))
    (fact "hashCodes should match"
      (.hashCode (ctor value)) => (.hashCode number))
    (fact "initialized value should remain invariant"
      (value-of number) => (roughly value))))

(defn test->increment [ctor value]
  (let [number (ctor value)]
    (fact (str "1 + " value " = " (clojure.core/inc value))
      (inc number) => (ctor (clojure.core/inc value)))
    (fact (str "1 + (1 + " value ") = " (clojure.core/inc (clojure.core/inc value)))
      (inc (inc number)) => (ctor (clojure.core/inc (clojure.core/inc value))))))

(defn test->decrement [ctor value]
  (let [number (ctor value)]
    (fact (str value " - 1 = " (clojure.core/dec value))
      (dec number) => (ctor (clojure.core/dec value)))
    (fact (str "(" value " - 1) - 1 = " (clojure.core/dec (clojure.core/dec value)))
      (dec (dec number)) => (ctor (clojure.core/dec (clojure.core/dec value))))))

(defn test->comparator [ctor small medium large]
  (fact (str medium " =/= " large)
    (= (ctor medium) (ctor large)) => false)
  (fact (str medium " == " medium)
    (= (ctor medium) (ctor medium)) => true)
  (fact (str medium " =/= " small)
    (= (ctor medium) (ctor small)) => false)
  (fact (str medium " < " large)
    (< (ctor medium) (ctor large)) => true)
  (fact (str medium " </ " medium)
    (< (ctor medium) (ctor medium)) => false)
  (fact (str medium " </ " small)
    (< (ctor medium) (ctor small)) => false)
  (fact (str medium " <= " large)
    (<= (ctor medium) (ctor large)) => true)
  (fact (str medium " <= " medium)
    (<= (ctor medium) (ctor medium)) => true)
  (fact (str medium " </= " small)
    (<= (ctor medium) (ctor small)) => false)
  (fact (str medium " >/ " large)
    (> (ctor medium) (ctor large)) => false)
  (fact (str medium " >/ " medium)
    (> (ctor medium) (ctor medium)) => false)
  (fact (str medium " > " small)
    (> (ctor medium) (ctor small)) => true)
  (fact (str medium " >/= " large)
    (>= (ctor medium) (ctor large)) => false)
  (fact (str medium " >= " medium)
    (>= (ctor medium) (ctor medium)) => true)
  (fact (str medium " >= " small)
    (>= (ctor medium) (ctor small)) => true))

(defn test->identity [ctor op ident value]
  (let [number (ctor value)
        ident-number (ctor ident)]
    (fact (str value " " op " " ident " = " value)
       (op number ident-number) => number)
    (fact (str ident " " op " " value " = " value)
       (op ident-number number) => number)))

(defn test->power [ctor value]
  (let [number (ctor value)]
    (fact (str value "** 1 = " value)
      (** number (ctor 1)) => number)))

(defn test->division [ctor value]
  (let [number (ctor value)]
    (fact (str value " / 1 = " value)
      (/ number (ctor 1)) => number)))

(defn test->parity [ctor odd-value even-value]
  (let [even-number (ctor even-value)
        odd-number (ctor odd-value)]
    (fact (str even-value " is even")
      (even? even-number) => true)
    (fact (str odd-value " is not even")
      (even? odd-number) => false)
    (fact (str odd-value " is odd")
      (odd? odd-number) => true)
    (fact (str even-value " is not odd")
      (odd? even-number) => false)))

(defn test->associativity [ctor op a b c]
  (let [a' (ctor a)
        b' (ctor b)
        c' (ctor c)]
    (fact (str "("a" "op" "b") "op" "c" == "a" "op" ("b" "op" "c")")
      (op (op a' b') c') => (op a' (op b' c')))))

(defn test->inverse [ctor op a b e]
  (let [a' (ctor a)
        b' (ctor b)
        e' (ctor e)]
    (fact (str a" "op" "b" == "b" "op" "a" == "e)
      (value-of (op a' b')) => (roughly (value-of (op b' a')))
      (value-of (op a' b')) => (roughly (value-of e'))
      (value-of (op b' a')) => (roughly (value-of e')))))

(test->basic natural 1)
(test->increment natural 2)
(test->decrement natural 5)
(test->comparator natural 0 1 2)
(test->identity natural + 0 2)
(test->identity natural * 1 2)
(test->power natural 2)
(test->division natural 2)
(test->parity natural 1 2)
(test->power natural 32)
(test->associativity natural + 5 6 7)
(test->associativity natural * 5 6 7)

(test->basic integer 1)
(test->increment integer 2)
(test->decrement integer 5)
(test->comparator integer 0 1 2)
(test->identity integer + 0 2)
(test->identity integer * 1 2)
(test->power integer 2)
(test->division integer 2)
(test->parity integer 1 2)
(test->power integer 32)
(test->associativity integer + 5 6 7)
(test->associativity integer * 5 6 7)
(test->inverse integer + 5 -5 0)

(test->basic real 0.5)
(test->increment real 2.5)
(test->decrement real 5.5)
(test->comparator real 0.5 1.5 2.5)
(test->identity real + 0.0 2.5)
(test->identity real * 1.0 2.5)
(test->power real 2.5)
(test->division real 2.5)
(test->power real 32.5)
(test->associativity real + 5.5 6.5 7.5)
(test->associativity real * 5.5 6.5 7.5)
(test->inverse real + 5.5 -5.5 0.0)
(test->inverse real * 5.5 (clojure.core// 1.0 5.5) 1.0)
