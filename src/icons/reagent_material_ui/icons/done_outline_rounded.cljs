(ns reagent-material-ui.icons.done-outline-rounded
  "Imports @material-ui/icons/DoneOutlineRounded as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def done-outline-rounded (create-svg-icon (e "path" #js {"d" "M20.47 5.63c.39.39.39 1.01 0 1.4L9.13 18.37c-.39.39-1.01.39-1.4 0l-4.2-4.2a.9839.9839 0 010-1.4c.39-.39 1.01-.39 1.4 0l3.5 3.5L19.07 5.63c.39-.39 1.01-.39 1.4 0zm-2.11-2.12l-9.93 9.93-2.79-2.79c-.78-.78-2.05-.78-2.83 0l-1.4 1.4c-.78.78-.78 2.05 0 2.83l5.6 5.6c.78.78 2.05.78 2.83 0L22.59 7.74c.78-.78.78-2.05 0-2.83l-1.4-1.4c-.79-.78-2.05-.78-2.83 0z"})
                                           "DoneOutlineRounded"))
