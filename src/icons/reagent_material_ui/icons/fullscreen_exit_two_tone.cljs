(ns reagent-material-ui.icons.fullscreen-exit-two-tone
  "Imports @material-ui/icons/FullscreenExitTwoTone as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def fullscreen-exit-two-tone (create-svg-icon (e "path" #js {"d" "M5 16h3v3h2v-5H5v2zm3-8H5v2h5V5H8v3zm6 11h2v-3h3v-2h-5v5zm2-11V5h-2v5h5V8h-3z"})
                                               "FullscreenExitTwoTone"))
