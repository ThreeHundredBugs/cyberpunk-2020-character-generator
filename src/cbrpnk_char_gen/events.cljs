(ns cbrpnk-char-gen.events
  (:require
   [re-frame.core :as re-frame]
   [cbrpnk-char-gen.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cbrpnk-char-gen.subs :as subs]
   [cbrpnk-char-gen.utils :as utils]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::generate-init-data
 (fn-traced [db _]
            (let [style                        (utils/generate-style)
                  family                       (utils/generate-family)
                  motives                      (utils/generate-motives)
                  age                          (+ 16 2 (rand-int 11))
                  esm (utils/generate-events age (:stats db) (:money db))
                  events (:events esm)
                  stats (:stats esm)
                  money (:money esm)]

              (-> db
                  (assoc-in [:history :style] style)
                  (assoc-in [:history :family] family)
                  (assoc-in [:history :motives] motives)
                  (assoc-in [:history :age] age)
                  (assoc-in [:history :events] events)
                  (assoc :stats stats)
                  (assoc :money money)))))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-db
 ::update-form
 (fn-traced [db [_ id val]]
   (assoc db id val)))

(re-frame/reg-event-db
 ::update-stats
 (fn-traced [db [_ id val]]
   (let [stats @(:stats db)
         new-stats (assoc-in stats [id :val] val)
         db (assoc db :stats (atom new-stats))
         current-points (reduce + (map #(:val %) (vals @(:stats db))))
         db (assoc db :current-points current-points)]
     (if (> current-points (:points db))
       (assoc db :stats-warning true)
       (assoc db :stats-warning false)))))

;; TODO: ability level
#_(defn generate-money []
  (let [role @(re-frame/subscribe [::subs/form :role])
        month (/ (inc (rand-int 6)) 3)]
    (case role)
    ))

#_(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (let [money (generate-money)]
     (assoc db id val))))
