(ns reagent-material-ui.icons.view-week-sharp
  "Imports @material-ui/icons/ViewWeekSharp as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def view-week-sharp (create-svg-icon (e "path" #js {"d" "M7 5H2v14h5V5zm14 0h-5v14h5V5zm-7 0H9v14h5V5z"})
                                      "ViewWeekSharp"))
