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

(defn run->suite [ctor small medium large]
  (let [number (ctor medium)]
    (fact "a number constructed from another should be equivalent"
      (ctor number) => number))

    (fact "numbers with the same value should be equivalent"
      (ctor medium) => (ctor medium))

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
      (>= (ctor medium) (ctor small)) => true)

    (fact (str "1 + " medium " = " (clojure.core/inc medium))
      (inc (ctor medium)) => (ctor (clojure.core/inc medium)))
    (fact (str "1 + (1 + " medium ") = " (clojure.core/inc (clojure.core/inc medium)))
      (inc (inc (ctor medium))) => (ctor (clojure.core/inc (clojure.core/inc medium))))

    (fact (str large " - 1 = " (clojure.core/dec large))
      (dec (ctor large)) => (ctor (clojure.core/dec large)))
    (fact (str "(" large " - 1) - 1 = " (clojure.core/dec (clojure.core/dec large)))
      (dec (dec (ctor large))) => (ctor (clojure.core/dec (clojure.core/dec large))))

    (fact "hashCodes should match"
      (.hashCode (ctor medium)) => (.hashCode (ctor medium)))

    (fact "initialized value should remain invariant"
      (value-of (ctor medium)) => medium)

    (fact "one is odd"
      (odd? (ctor medium)) => true)
    (fact "one is not even"
      (even? (ctor medium)) => false)
    (fact "zero is not odd"
      (odd? (ctor small)) => false)
    (fact "zero is the most-even number"
      (even? (ctor small)) => true))

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

(run->suite natural 0 1 2)
(run->identity natural + 0 2)
(run->identity natural * 1 2)
(run->power natural 2)
(run->division natural 2)

(run->suite integer 0 1 2)
(run->identity integer + 0 2)
(run->identity integer * 1 2)
(run->power natural 2)
(run->division natural 2)

;; NOTE: Be careful of set closure under various operations.

(run->suite integer+ 2 3 4)
;(run->identity integer+ + 0 2) ;;-> 0 is not a member of integer+
(run->identity integer+ * 1 2)
(run->power natural 2)
(run->division natural 2)

(run->suite integer- -4 -3 -2)
;(run->identity integer- + 0 2) ;;-> 0 is not a member of integer-
;(run->identity integer- * 1 2) ;;-> 1 is not a member of integer-
