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

(ns umlang.test.parser
  (:use midje.sweet)
  (:require [umlang.parser :as parser]))

(let [ast (parser/parse "3")]
  (fact "3 is an integer literal"
    ast => {:type :literal
            :class :integer
            :value 3}))

(let [ast (parser/parse "3 + 4")]
  (fact "3 + 4 is a binary expression under addition"
    ast => {:type :operation
            :class :integer
            ;:arity "binary" ;;<-- NOTE: This is redundant on the size of the
            ;parameters array.
            :operation :addition
            ;:precedence "left" ;;<-- NOTE: To reverse the precedence, reverse
            ;the order of the parameters before supplying them to the map. Then,
            ;just consume them from left-to-right, in order.
            :parameters [{:type :literal
                          :class :integer
                          :value 3}
                         {:type :literal
                          :class :integer
                          :value 4}]}))

(let [ast (parser/parse "1 + (2 + 3)")]
  (fact "1 + (2 + 3) should parse to the sum of 1 and the sum of 2 and 3"
    ast => {:type :operation
            :class :integer
            :operation :addition
            :parameters [{:type :literal
                          :class :integer
                          :value 1}
                         {:type :operation
                          :class :integer
                          :operation :addition
                          :parameters [{:type :literal
                                        :class :integer
                                        :value 2}
                                       {:type :literal
                                        :class :integer
                                        :value 3}]}]}))

(let [ast (parser/parse "(1 + 2) + 3")]
  (fact "(1 + 2) + 3 should parse to the sum of the sum of 1 and 2 and 3"
    ast => {:type :operation
            :class :integer
            :operation :addition
            :parameters [{:type :operation
                          :class :integer
                          :operation :addition
                          :parameters [{:type :literal
                                        :class :integer
                                        :value 1}
                                       {:type :literal
                                        :class :integer
                                        :value 2}]}
                         {:type :literal
                          :class :integer
                          :value 3}]}))

