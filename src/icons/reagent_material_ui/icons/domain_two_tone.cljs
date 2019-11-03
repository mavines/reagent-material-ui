(ns reagent-material-ui.icons.domain-two-tone
  "Imports @material-ui/icons/DomainTwoTone as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [reagent-material-ui.util :refer [create-svg-icon]]))

(def domain-two-tone (create-svg-icon (e (.-Fragment js/React) nil (e "path" #js {"d" "M12 11h2v2h-2v2h2v2h-2v2h8V9h-8v2zm4 0h2v2h-2v-2zm0 4h2v2h-2v-2z", "opacity" ".3"}) (e "path" #js {"d" "M12 7V3H2v18h20V7H12zM6 19H4v-2h2v2zm0-4H4v-2h2v2zm0-4H4V9h2v2zm0-4H4V5h2v2zm4 12H8v-2h2v2zm0-4H8v-2h2v2zm0-4H8V9h2v2zm0-4H8V5h2v2zm10 12h-8v-2h2v-2h-2v-2h2v-2h-2V9h8v10zm-4-8h2v2h-2zm0 4h2v2h-2z"}))
                                      "DomainTwoTone"))