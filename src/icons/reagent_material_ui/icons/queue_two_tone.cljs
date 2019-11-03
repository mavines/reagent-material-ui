(ns reagent-material-ui.icons.queue-two-tone
  "Imports @material-ui/icons/QueueTwoTone as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [reagent-material-ui.util :refer [create-svg-icon]]))

(def queue-two-tone (create-svg-icon (e (.-Fragment js/React) nil (e "path" #js {"d" "M8 16h12V4H8v12zm1-7h4V5h2v4h4v2h-4v4h-2v-4H9V9z", "opacity" ".3"}) (e "path" #js {"d" "M2 20c0 1.1.9 2 2 2h14v-2H4V6H2v14zM20 2H8c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H8V4h12v12zm-7-1h2v-4h4V9h-4V5h-2v4H9v2h4z"}))
                                     "QueueTwoTone"))