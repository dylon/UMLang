(defproject umlang "0.1.0-alpha"
  :description "Symbolic/Numeric CAS (Computer Algebra System) with UML-like Syntax"
  :url "https://github.com/dylon/umlang"
  :license {:name "The MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.antlr/antlr4 "4.0"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]
                 [org.clojure/tools.logging "0.2.6"]]
  :main umlang.core)
