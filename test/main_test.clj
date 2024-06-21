(ns main-test
  (:require [clojure.test :refer [are deftest is testing]]))


(deftest ensuring-test-works
  (testing "addition"
    (is (= (+ 3 3) 6))))
