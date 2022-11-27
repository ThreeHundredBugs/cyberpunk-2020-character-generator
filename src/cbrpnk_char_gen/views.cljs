(ns cbrpnk-char-gen.views
  (:require
   [re-frame.core :as re-frame]
   [cbrpnk-char-gen.styles :as styles]
   [cbrpnk-char-gen.events :as events]
   [cbrpnk-char-gen.routes :as routes]
   [cbrpnk-char-gen.subs :as subs]))


(defn text-input [id label]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field.row.g-2
     [:label.col-form-label.col-auto {:for id} label]
     [:div.col-auto
      [:input.form-control {:type "text"
                            :id id
                            :value @value
                            :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}]]]))

;; home

(defn home-panel []
  [:div
   [:h1
    (str "Welcome to the character generator!")]

   [:div
    [:button.btn.btn-primary {:on-click #(re-frame/dispatch [::events/navigate :create])}
     "Create Character"]]])

(defmethod routes/panels :home-panel [] [home-panel])

(defn create-char-form []
  [:div.form (text-input :name "Name")]
  )

(defn create-panel []
  [:div
   [:h1.pb-4 "Your Character"]

   (create-char-form)

   [:div.py-4
    [:button.btn.btn-primary {:on-click #(re-frame/dispatch [::events/navigate :home])}
     "go to Home Page"]]])

(defmethod routes/panels :create-panel [] [create-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div.container.py-4
     (routes/panels @active-panel)]))
