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

(defn run->basic [ctor value]
  (let [number (ctor value)]
    (fact "a number constructed from another should be equivalent"
      (ctor value) => number)
    (fact "numbers with the same value should be equivalent"
      (ctor value) => (ctor value))
    (fact "hashCodes should match"
      (.hashCode (ctor value)) => (.hashCode number))
    (fact "initialized value should remain invariant"
      (value-of number) => (roughly value))))

(defn run->increment [ctor value]
  (let [number (ctor value)]
    (fact (str "1 + " value " = " (clojure.core/inc value))
      (inc number) => (ctor (clojure.core/inc value)))
    (fact (str "1 + (1 + " value ") = " (clojure.core/inc (clojure.core/inc value)))
      (inc (inc number)) => (ctor (clojure.core/inc (clojure.core/inc value))))))

(defn run->decrement [ctor value]
  (let [number (ctor value)]
    (fact (str value " - 1 = " (clojure.core/dec value))
      (dec number) => (ctor (clojure.core/dec value)))
    (fact (str "(" value " - 1) - 1 = " (clojure.core/dec (clojure.core/dec value)))
      (dec (dec number)) => (ctor (clojure.core/dec (clojure.core/dec value))))))

(defn run->comparator [ctor small medium large]
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

(defn run->identity [ctor op ident value]
  (let [number (ctor value)
        ident-number (ctor ident)]
    (fact (str value " " op " " ident " = " value)
       (op number ident-number) => number)
    (fact (str ident " " op " " value " = " value)
       (op ident-number number) => number)))

(defn run->power [ctor value]
  (let [number (ctor value)]
    (fact (str value "** 1 = " value)
      (** number (ctor 1)) => number)))

(defn run->division [ctor value]
  (let [number (ctor value)]
    (fact (str value " / 1 = " value)
      (/ number (ctor 1)) => number)))

(defn run->parity [ctor odd-value even-value]
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

(run->basic natural 1)
(run->increment natural 2)
(run->decrement natural 5)
(run->comparator natural 0 1 2)
(run->identity natural + 0 2)
(run->identity natural * 1 2)
(run->power natural 2)
(run->division natural 2)
(run->parity natural 1 2)

(run->basic integer 1)
(run->increment integer 2)
(run->decrement integer 5)
(run->comparator integer 0 1 2)
(run->identity integer + 0 2)
(run->identity integer * 1 2)
(run->power integer 2)
(run->division integer 2)
(run->parity integer 1 2)

(run->basic real 0.5)
(run->increment real 2.5)
(run->decrement real 5.5)
(run->comparator real 0.5 1.5 2.5)
(run->identity real + 0.0 2.5)
(run->identity real * 1.0 2.5)
(run->power real 2.5)
(run->division real 2.5)
