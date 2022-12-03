(ns cbrpnk-char-gen.views
  (:require
   [re-frame.core :as re-frame]
   [cbrpnk-char-gen.styles :as styles]
   [cbrpnk-char-gen.events :as events]
   [cbrpnk-char-gen.routes :as routes]
   [cbrpnk-char-gen.subs :as subs]
   [cbrpnk-char-gen.db :as db]))


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

(defn select-stat-input [id label]
  (let [value (re-frame/subscribe [::subs/get-stat id])]
    [:div.field.row.g-2.py-2
     [:label.col-form-label.col-auto label]
     [:div.control.col-auto
      [:div.select
       [:select.form-control {:value @value
                              :on-change #(re-frame/dispatch [::events/update-stats id (-> % .-target .-value js/parseInt)])}
        (map (fn [o] [:option {:key o :value o} o]) [0 1 2 3 4 5 6 7 8 9 10])]]]]))

(defn text-row [label text]
  [:div.row.g-2.py-2
          [:label.col-auto label]
          [:div.col-auto.text-secondary text]])

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
  (let [points         @(re-frame/subscribe [::subs/points])
        current-points @(re-frame/subscribe [::subs/current-points])
        stats-warning  @(re-frame/subscribe [::subs/form :stats-warning])
        move           @(re-frame/subscribe [::subs/get-stat :move])
        body           @(re-frame/subscribe [::subs/get-stat :body])
        empathy        @(re-frame/subscribe [::subs/get-stat :empathy])
        run            (* 3 move)
        jump           (/ run 4)
        savethrow      body
        mbody          (cond
                         (<= body 2)                  0
                         (and (> body 2) (<= body 4)) 1
                         (and (> body 4) (<= body 7)) 2
                         (and (> body 7) (<= body 9)) 3
                         (= body 10)                  4
                         :else                        5)
        postpone       (* 10 body)
        raise          (* 40 body)
        humanity       (* 10 empathy)]

    [:div.row.g-2.py-4
     [:div.col-auto
      [:div.card
       (when stats-warning
         {:class :border-danger})
       [:div.card-body
        [:h5.card-title "Статы"]
        [:div.card-text.container.g-2
         (when stats-warning
           [:div.alert.alert-danger
            "Недостаточно очков!"])
         [:div (str "Количество очков: " (- points current-points))]
         (select-stat-input :intellect      :Интеллект)
         (select-stat-input :reflex         :Рефлекс)
         (select-stat-input :technique      :Техника)
         (select-stat-input :cool           :Крутость)
         (select-stat-input :attractiveness :Привлекательность)
         (select-stat-input :luck           :Удача)
         (select-stat-input :move           :Передвижение)
         (select-stat-input :body           :Тело)
         (select-stat-input :empathy        :Эмпатия)]
        [:div.card-footer
         (text-row "Бег" run)
         (text-row "Прыжок" jump)
         (text-row "Перенести" postpone)
         (text-row "Поднять" raise)
         (text-row "Спасбросок" savethrow)
         (text-row "Модификатор телосложения" mbody)
         (text-row "Человечность" humanity)]]]]]))

(defn money []
  (add-watch db/atom-money
             :money
                (fn [key ref old-state new-state]
                (re-frame/dispatch [::events/update-form :money new-state])))

    (let [money @(re-frame/subscribe [::subs/form :money])]
      (text-row "Деньги" money))
  )


(defn history []
  (let [history @(re-frame/subscribe [::subs/form :history])]
    [:div.row.g-2
     [:div.col-auto
      [:div.card
       [:div.card-body
        [:h5.card-title "Стиль"]
        [:div.card-text.mb-2
         (text-row "Раса" (get-in history [:style :race]))
         (text-row "Одежда" (get-in history [:style :clothes]))
         (text-row "Прическа" (get-in history [:style :haircut]))
         (text-row "Особенность" (get-in history [:style :feature]))]
        [:h5.card-title "Семья"]
        [:div.card-text.mb-2
         (text-row "Рейтинг семьи" (get-in history [:family :rating]))
         (text-row "Родители" (get-in history [:family :parents]))
         (text-row "Статус семьи" (get-in history [:family :status]))
         (text-row "Детсво прошло" (get-in history [:family :childhood]))]
        [:h5.card-title "Братья и сестры"]
        (if (empty? (get-in history [:family :siblings]))
           [:div "Ты единственный ребенок"]
           [:div
            (for [sibling (get-in history [:family :siblings])]
              [:div
               [:hr]
               (text-row "Пол" (get sibling :gender))
               (text-row "Возраст" (get sibling :age))
               (text-row "Отношения" (get sibling :relations))])])
        [:h5.card-title "Мотивы"]
        [:div.card-text.mb-2
         (text-row "Характер" (get-in history [:motives :character]))
         (text-row "Важный человек" (get-in history [:motives :valuable-person]))
         (text-row "Важная вещь" (get-in history [:motives :valuable-thing]))
         (text-row "Отношение к людям" (get-in history [:motives :attitude-towards-people]))
         (text-row "Хобби" (get-in history [:motives :hobby]))]
        [:h5.card-title "Предыстория"]
        [:div.card-text.mb-2
         (text-row "Всего лет" (get-in history [:age]))
         (for [event (get-in history [:events])]
           [:div
            [:hr]
            (text-row "Возраст" (get event :age))
            (text-row "Событие" (get event :event))])]]]]]))

(defn create-char-form []
  [:div.form
   (money)
   (text-input :name :Прозвище)
   (select-input :role :Класс ["Рокербой" "Соло" "Нетраннер" "Техник" "Медиа" "Коп" "Корпорат" "Фиксер" "Номад"])
   (stats)

   (history)
   ]
  )

(defn create-panel []
  [:div
   [:h1.pb-4 "Твой персонаж"]

   (create-char-form)

   [:div.py-4
    [:button.btn.btn-outline-primary
     {:on-click #(re-frame/dispatch [::events/navigate :home])}
     "Назад"]]])

(defmethod routes/panels :create-panel [] [create-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div.container.py-4
     (routes/panels @active-panel)]))
