(ns reagent-material-ui.icons.format-clear-outlined
  "Imports @material-ui/icons/FormatClearOutlined as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def format-clear-outlined (create-svg-icon (e "path" #js {"d" "M20 8V5H6.39l3 3h1.83l-.55 1.28 2.09 2.1L14.21 8zM3.41 4.86L2 6.27l6.97 6.97L6.5 19h3l1.57-3.66L16.73 21l1.41-1.41z"})
                                            "FormatClearOutlined"))
