(ns reagent-material-ui.icons.crop-din-sharp
  "Imports @material-ui/icons/CropDinSharp as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def crop-din-sharp (create-svg-icon (e "path" #js {"d" "M21 3H3v18h18V3zm-2 16H5V5h14v14z"})
                                     "CropDinSharp"))
