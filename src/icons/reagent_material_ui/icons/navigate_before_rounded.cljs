(ns reagent-material-ui.icons.navigate-before-rounded
  "Imports @material-ui/icons/NavigateBeforeRounded as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def navigate-before-rounded (create-svg-icon (e "path" #js {"d" "M14.91 6.71a.9959.9959 0 00-1.41 0L8.91 11.3c-.39.39-.39 1.02 0 1.41l4.59 4.59c.39.39 1.02.39 1.41 0 .39-.39.39-1.02 0-1.41L11.03 12l3.88-3.88c.38-.39.38-1.03 0-1.41z"})
                                              "NavigateBeforeRounded"))
