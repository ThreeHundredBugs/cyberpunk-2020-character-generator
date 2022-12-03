(ns cbrpnk-char-gen.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::form
 (fn [db [_ id]]
   (id db)))

(re-frame/reg-sub
 ::get-stat
 (fn [db [_ id]]
   (get-in db [:stats id])))

(re-frame/reg-sub
 ::points
 (fn [db]
   (:points db)))

(re-frame/reg-sub
 ::current-points
 (fn [db]
   (:current-points db)))

(re-frame/reg-sub
 ::get-money
 (fn [db]
   @(:money db)))

#_(re-frame/reg-sub
 ::stats
 (fn [db]
   (get-in db [:form :stats])))
