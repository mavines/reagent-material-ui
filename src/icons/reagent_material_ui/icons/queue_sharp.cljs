(ns reagent-material-ui.icons.queue-sharp
  "Imports @material-ui/icons/QueueSharp as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def queue-sharp (create-svg-icon (e "path" #js {"d" "M4 6H2v16h16v-2H4V6zm18-4H6v16h16V2zm-3 9h-4v4h-2v-4H9V9h4V5h2v4h4v2z"})
                                  "QueueSharp"))
