(ns cbrpnk-char-gen.db)

(def default-db
  {:role   "Рокербой"
   :stats  {:intellect      0
            :reflex         0
            :technique      0
            :cool           0
            :attractiveness 0
            :luck           0
            :move           0
            :body           0
            :empathy        0}
   :points (+ 9 (rand-int 81))})
