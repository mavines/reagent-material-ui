(ns reagent-material-ui.util
  (:require-macros [reagent-material-ui.macro :refer [e forward-ref]])
  (:require [reagent.core :as r]
            [reagent.impl.util :refer [fun-name]]
            [clojure.string :as str]
            [clojure.walk :refer [postwalk]]
            [camel-snake-kebab.core :refer [->kebab-case-keyword ->camelCaseKeyword ->camelCaseString]]
            [cljsjs.react]
            [material-ui]))

(defn adapt-react-class
  ([c]
   (adapt-react-class c (fun-name c)))
  ([c display-name]
   (let [adapted (r/adapt-react-class c)]
     (set! (.-displayName adapted) display-name)
     adapted)))

(def ^:private color-key? #{:A100 :A200 :A400 :A700 "A100" "A200" "A400" "A700"})
(defn ^:private numeric-string? [s]
  (and (string? s)
       (some? (re-matches #"[0-9]+" s))))

(defn ^:private key->str [k]
  (let [n (name k)]
    (cond
      (color-key? k) n
      (str/starts-with? n "data-") n
      (str/starts-with? n "aria-") n
      :else (->camelCaseString k))))

(defn ^:private convert-map-keys [m f]
  (postwalk (fn [x]
              (if (map-entry? x)
                [(f (key x)) (val x)]
                x))
            m))

(defn clj->js'
  [obj]
  (clj->js (convert-map-keys obj (fn [k]
                                   (if (keyword? k)
                                     (key->str k)
                                     k)))))

(defn js->clj' [obj]
  (convert-map-keys (js->clj obj) (fn [k]
                                    (cond
                                      (color-key? k) (keyword k)
                                      (numeric-string? k) (js/parseInt k)
                                      :else (->kebab-case-keyword k)))))

(defn wrap-clj-function [f]
  (fn [& args]
    (clj->js' (apply f (map js->clj' args)))))

(defn wrap-js-function [f]
  (fn [& args]
    (js->clj' (apply f (map clj->js' args)))))

(defn wrap-all-clj-functions [m]
  (postwalk (fn [x]
              (if (fn? x)
                (wrap-clj-function x)
                x))
            m))

(defn reactify-component [component]
  (let [reactified (forward-ref [props ref]
                     (let [clj-props (merge (js->clj' props)
                                            {:ref      ref
                                             :children (.-children props)})]
                       (r/as-element [component clj-props])))]
    (set! (.-displayName reactified) (fun-name component))
    reactified))

(defn wrap-jss-styles [styles]
  (if (fn? styles)
    (fn [theme]
      (-> (js->clj' theme)
          (styles)
          (wrap-all-clj-functions)
          (clj->js')))
    (clj->js (wrap-all-clj-functions styles))))

(defn apply-hoc [hoc component]
  (-> component
      (reactify-component)
      (hoc)
      (adapt-react-class)))

(defn get-anycase
  ([m k]
   (get-anycase m k nil))
  ([m k default]
   (if-let [entry (or (find m (->kebab-case-keyword k))
                      (find m (->camelCaseKeyword k)))]
     (val entry)
     default)))

(defn assoc-anycase
  ([m k v]
   (assoc (dissoc m (->camelCaseKeyword k)) (->kebab-case-keyword k) v))
  ([m k v & kvs]
   (let [ret (assoc-anycase m k v)]
     (if kvs
       (recur ret (first kvs) (second kvs) (nnext kvs))
       ret))))

(defn debounce [f ms]
  (let [timeout (volatile! nil)
        ret (fn [& args]
              (js/clearTimeout @timeout)
              (vreset! timeout (js/setTimeout #(apply f args) ms)))]
    (set! (.-clear ret) #(js/clearTimeout @timeout))
    ret))

(defn remove-undefined-vals [m]
  (persistent!
   (reduce-kv (fn [acc k v]
                (if (undefined? v)
                  (dissoc! acc k)
                  acc))
              (transient m)
              m)))

(defn set-ref [ref value]
  (cond
    (fn? ref) (ref value)
    ref (set! (.-current ref) value)))

(defn use-fork-ref [ref-a ref-b]
  (.useMemo js/React
            #(when (or ref-a ref-b)
               (fn [value]
                 (set-ref ref-a value)
                 (set-ref ref-b value)))
            #js [ref-a ref-b]))

(defn use-callback [callback props]
  (.useCallback js/React callback props))

(defn use-effect [effect props]
  (.useEffect js/React effect props))

(defn use-layout-effect [effect]
  (.useLayoutEffect js/React effect))

(defn use-ref [value]
  (.useRef js/React value))

(defn use-state [initial-state]
  (.useState js/React initial-state))

(defn create-svg-icon [path display-name]
  (let [component (.memo js/React (forward-ref [props ref]
                                    (e (.-SvgIcon js/MaterialUI) (.assign js/Object #js {:ref ref} props) path)))]
    (adapt-react-class component display-name)))