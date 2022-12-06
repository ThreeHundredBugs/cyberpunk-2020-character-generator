(ns cbrpnk-char-gen.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [cbrpnk-char-gen.events :as events]
   [cbrpnk-char-gen.routes :as routes]
   [cbrpnk-char-gen.views :as views]
   [cbrpnk-char-gen.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::events/generate-init-data])
  (dev-setup)
  (js/setTimeout #(mount-root) 300))
