(ns reagent-material-ui.icons.bookmark-border
  "Imports @material-ui/icons/BookmarkBorder as a Reagent component."
  (:require-macros [reagent-material-ui.macro :refer [e]])
  (:require [react :as react]
            [reagent-material-ui.util :refer [create-svg-icon]]))

(def bookmark-border (create-svg-icon (e "path" #js {"d" "M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2zm0 15l-5-2.18L7 18V5h10v13z"})
                                      "BookmarkBorder"))
