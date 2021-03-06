(ns reagent-material-ui.icons.signal-cellular-no-sim-two-tone
  "Imports @material-ui/icons/SignalCellularNoSimTwoTone as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def signal-cellular-no-sim-two-tone (create-svg-icon (e react/Fragment nil (e "path" #js {"d" "M10.83 5L9.36 6.47 17 14.11V5zM7 9.79V19h9.23z", "opacity" ".3"}) (e "path" #js {"d" "M10.83 5H17v9.11l2 2V5c0-1.1-.9-2-2-2h-7L7.94 5.06l1.42 1.42L10.83 5zm10.43 16.21L3.79 3.74 2.38 5.15 5 7.77V19c0 1.11.9 2 2 2h11.23l1.62 1.62 1.41-1.41zM7 19V9.79L16.23 19H7z"}))
                                                      "SignalCellularNoSimTwoTone"))
