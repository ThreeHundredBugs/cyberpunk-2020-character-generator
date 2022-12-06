(ns cbrpnk-char-gen.db
  (:require [re-frame.core :as re-frame]))

(def default-db
  {:role    "Рокербой"
   :stats   {:intellect      {:mod 0
                              :val 0}
             :reflex         {:mod 0
                              :val 0}
             :technique      {:mod 0
                              :val 0}
             :cool           {:mod 0
                              :val 0}
             :attractiveness {:mod 0
                              :val 0}
             :luck           {:mod 0
                              :val 0}
             :move           {:mod 0
                              :val 0}
             :body           {:mod 0
                              :val 0}
             :empathy        {:mod 0
                              :val 0}}
   :points  (+ 9 (rand-int 82))
   :money   0
   })

(comment

  )
