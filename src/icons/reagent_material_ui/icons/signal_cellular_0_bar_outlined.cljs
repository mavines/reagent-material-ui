(ns reagent-material-ui.icons.signal-cellular-0-bar-outlined
  "Imports @material-ui/icons/SignalCellular0BarOutlined as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def signal-cellular-0-bar-outlined (create-svg-icon (e "path" #js {"d" "M2 22h20V2L2 22z", "fillOpacity" ".3"})
                                                     "SignalCellular0BarOutlined"))
