(ns reagent-material-ui.icons.iso-two-tone
  "Imports @material-ui/icons/IsoTwoTone as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def iso-two-tone (create-svg-icon (e react/Fragment nil (e "path" #js {"d" "M19 19V5L5 19h14zm-2-3.5V17h-5v-1.5h5z", "opacity" ".3"}) (e "path" #js {"d" "M12 15.5h5V17h-5zM19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zM5.5 7.5h2v-2H9v2h2V9H9v2H7.5V9h-2V7.5zM19 19H5L19 5v14z"}))
                                   "IsoTwoTone"))
