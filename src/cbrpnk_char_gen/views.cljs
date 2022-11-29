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

(defn select-input [id label options & [opts]]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field.row.g-2.py-2
     [:label.col-form-label.col-auto label]
     [:div.control.col-auto
      [:div.select
       [:select.form-control {:value @value
                              :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}
        (map (fn [o] [:option {:key o :value o} o]) options)]]]]))

;; home

(defn home-panel []
  [:div
   [:h1
    "Добро пожаловать в генератор персонажей Cyberpunk 2020!"]

   [:div
    [:button.btn.btn-primary {:on-click #(re-frame/dispatch [::events/navigate :create])}
     "Создать"]]])

(defmethod routes/panels :home-panel [] [home-panel])

(defn stats []
  (let [move      @(re-frame/subscribe [::subs/form :stats/move])
        body      @(re-frame/subscribe [::subs/form :stats/body])
        run       (* 3 move)
        jump      (/ move 4)
        savethrow body
        mbody     (cond
                    (<= body 2)                  0
                    (and (> body 2) (<= body 4)) 1
                    (and (> body 4) (<= body 7)) 2
                    (and (> body 7) (<= body 9)) 3
                    (= body 10)                  4
                    :else                        5)
        postpone  (* 10 body)
        raise     (* 40 body)
        ]
    (def stats stats)
    [:div.row.g-2
     [:div.col-auto
      [:div.card
       [:div.card-body
        [:h5.card-title "Статы"]
        [:div.card-text.container.g-2
         (select-input :stats/intellect      :Интеллект         [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/reflex         :Рефлекс           [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/technique      :Техника           [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/cool           :Крутость          [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/attractiveness :Привлекательность [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/luck           :Удача             [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/move           :Передвижение      [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/body           :Тело              [0 1 2 3 4 5 6 7 8 9])
         (select-input :stats/empathy        :Эмпатия           [0 1 2 3 4 5 6 7 8 9])

         [:div.row.g-2.py-2
          [:label.col-auto "Бег"]
          [:div.col-auto.text-secondary run]]

         [:div.row.g-2.py-2
          [:label.col-auto "Прыжок"]
          [:div.col-auto.text-secondary jump]]

         [:div.row.g-2.py-2
          [:label.col-auto "Перенести"]
          [:div.col-auto.text-secondary postpone]]

         [:div.row.g-2.py-2
          [:label.col-auto "Поднять"]
          [:div.col-auto.text-secondary raise]]

         [:div.row.g-2.py-2
          [:label.col-auto "Спасбросок"]
          [:div.col-auto.text-secondary savethrow]]

         [:div.row.g-2.py-2
          [:label.col-auto "Модификатор телосложения"]
          [:div.col-auto.text-secondary mbody]]

         ]

         ]]]]))

(defn create-char-form []
  [:div.form
   (text-input :name :Прозвище)
   (select-input :role :Класс ["Рокербой" "Соло" "Нетраннер" "Техник" "Медиа" "Коп" "Корпорат" "Фиксер" "Номад"])

   (stats)
   ]
  )

(defn create-panel []
  [:div
   [:h1.pb-4 "Твой персонаж"]

   (create-char-form)

   [:div.py-4
    [:button.btn.btn-primary {:on-click #(re-frame/dispatch [::events/navigate :home])}
     "Назад"]]])

(defmethod routes/panels :create-panel [] [create-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div.container.py-4
     (routes/panels @active-panel)]))
