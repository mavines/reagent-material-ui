(ns reagent-material-ui.icons.personal-video-sharp
  "Imports @material-ui/icons/PersonalVideoSharp as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def personal-video-sharp (create-svg-icon (e "path" #js {"d" "M23 3H1v16h7v2h8v-2h6.99L23 3zm-2 14H3V5h18v12z"})
                                           "PersonalVideoSharp"))
