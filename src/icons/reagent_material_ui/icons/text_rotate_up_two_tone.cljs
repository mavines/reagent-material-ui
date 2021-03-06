(ns reagent-material-ui.icons.text-rotate-up-two-tone
  "Imports @material-ui/icons/TextRotateUpTwoTone as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def text-rotate-up-two-tone (create-svg-icon (e "path" #js {"d" "M18 4l-3 3h2v13h2V7h2l-3-3zm-6.2 11.5v-5l2.2-.9V7.5L3 12.25v1.5l11 4.75v-2.1l-2.2-.9zM4.98 13L10 11.13v3.74L4.98 13z"})
                                              "TextRotateUpTwoTone"))
