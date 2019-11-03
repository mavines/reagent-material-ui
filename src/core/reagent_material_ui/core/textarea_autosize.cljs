(ns reagent-material-ui.core.textarea-autosize
  "Imports @material-ui/core/TextareaAutosize as a Reagent component.
   Original documentation is at https://material-ui.com/api/textarea-autosize/ ."
  (:require-macros [reagent-material-ui.macro :refer [forward-ref]])
  (:require [reagent.core :as r :refer [atom]]
            [reagent-material-ui.util :refer [adapt-react-class debounce js->clj' use-fork-ref
                                              use-callback use-effect use-layout-effect use-ref use-state]]
            [cljsjs.react]
            [goog.object :as obj]))

;; Converted from https://github.com/mui-org/material-ui/blob/v4.5.1/packages/material-ui/src/TextareaAutosize/TextareaAutosize.js
;; Original code is copyright (c) Material UI contributors. Used used under the terms of the MIT License.

(defn ^:private different? [a b]
  (< 1 (.abs js/Math (- (or a 0) (or b 0)))))

(defn ^:private get-style-value [computed-style property]
  (or (js/parseInt (obj/get computed-style property) 10) 0))

(def react-textarea-autosize
  (forward-ref textarea-autosize [props ref]
    (let [props (js->clj' props)
          {:keys [on-change placeholder rows rows-max style value]} props
          other-props (dissoc props :input-ref :on-change :rows :rows-max :style :value)
          controlled? (some? value)
          input-ref (use-ref nil)
          shadow-ref (use-ref nil)
          handle-input-ref-prop (use-fork-ref (:input-ref props) input-ref)
          handle-ref (use-fork-ref ref handle-input-ref-prop)
          [state set-state] (use-state #js {})
          sync-height (use-callback #(let [input (.-current input-ref)
                                           shadow (.-current shadow-ref)
                                           computed-style (.getComputedStyle js/window input)
                                           _ (set! (.-width (.-style shadow)) (.-width computed-style))
                                           _ (set! (.-value shadow) (or (.-value input) placeholder "x"))
                                           box-sizing (obj/get computed-style "box-sizing")
                                           padding (+ (get-style-value computed-style "padding-bottom")
                                                      (get-style-value computed-style "padding-top"))
                                           border (+ (get-style-value computed-style "border-bottom-width")
                                                     (get-style-value computed-style "border-top-width"))
                                           inner-height (- (.-scrollHeight shadow) padding)
                                           _ (set! (.-value shadow) "x")
                                           single-row-height (- (.-scrollHeight shadow) padding)
                                           outer-height (cond-> inner-height
                                                          rows (max (* (js/Number. rows) single-row-height))
                                                          rows-max (min (* (js/Number. rows-max) single-row-height))
                                                          true (max single-row-height))
                                           outer-height (if (= "border-box" box-sizing)
                                                          (+ outer-height padding border)
                                                          outer-height)
                                           overflow? (not (different? outer-height inner-height))]
                                       (set-state (fn [prev-state]
                                                    (if (or (and (pos? outer-height)
                                                                 (different? (.-outerHeight prev-state) outer-height))
                                                            (not= overflow? (.-overflow prev-state)))
                                                      #js {:overflow    overflow?
                                                           :outerHeight outer-height}
                                                      prev-state))))
                                    #js [rows rows-max placeholder])
          handle-change (fn [e]
                          (when-not controlled?
                            (sync-height))
                          (when on-change
                            (on-change e)))]
      (use-effect #(let [handle-resize (debounce sync-height 166)]
                     (.addEventListener js/window "resize" handle-resize)
                     (fn []
                       (.clear handle-resize)
                       (.removeEventListener js/window "resize" handle-resize)))
                  #js [sync-height])
      (use-layout-effect sync-height)
      (r/as-element
       [:<>
        [:textarea (merge (when-not (undefined? value)
                            {:value value})
                          {:on-change handle-change
                           :ref       handle-ref
                           :rows      (or rows 1)
                           :style     (merge {:height   (.-outerHeight state)
                                              :overflow (when (.-overflow state) :hidden)}
                                             style)}
                          other-props)]
        [:textarea {:aria-hidden true
                    :class-name  (:class-name props)
                    :read-only   true
                    :ref         shadow-ref
                    :tab-index   -1
                    :style       (merge {:visibility :hidden
                                         :position   :absolute
                                         :overflow   :hidden
                                         :height     0
                                         :top        0
                                         :left       0
                                         :transform  "translateZ(0)"}
                                        style)}]]))))

(def textarea-autosize (adapt-react-class react-textarea-autosize))