(ns reagent-material-ui.scripts
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [camel-snake-kebab.core :as csk]
            [clojure.java.io :as io]
            [instaparse.core :as insta])
  (:import (java.io File)))

;; (write-icons "/Users/arttuka/workspace/arttuka/material-ui/packages/material-ui-icons/src")

(def exclude-clj #{"comment" "compare" "filter" "list" "loop" "map" "print" "remove" "repeat" "shuffle" "sort" "update"})
(def exclude-core #{"TextField" "TextareaAutosize"})
(def js-keyword-icons #{"class" "delete" "public"})
(def js-keyword-components #{"switch"})

(defn component-ns-name [js-ns js-name]
  (let [clj-name (csk/->kebab-case (or js-name js-ns))]
    (if (contains? js-keyword-components clj-name)
      (str clj-name "-component")
      clj-name)))

(defn icon-ns-name [js-name]
  (let [clj-name (csk/->kebab-case js-name)]
    (if (contains? js-keyword-icons clj-name)
      (str clj-name "-icon")
      clj-name)))

(defn mui-name [js-ns js-name]
  (if js-name
    (str "(.-" js-name " " js-ns ")")
    (str "(or (.-default " js-ns ") (.-" js-ns " " js-ns "))")))

(defn generate-picker-ns-content [js-name]
  (let [clj-name (csk/->kebab-case js-name)]
    (str "(ns reagent-material-ui.pickers." (component-ns-name js-name nil) \newline
         "  \"Imports @material-ui/pickers/" js-name " as a Reagent component." \newline
         "   Original documentation is at https://material-ui-pickers.dev/api/" js-name "/ .\"" \newline
         (when (contains? exclude-clj clj-name)
           (str "  (:refer-clojure :exclude [" clj-name "])" \newline))
         "  (:require [reagent-material-ui.util :refer [adapt-react-class]]" \newline
         "            [\"@material-ui/pickers\" :as pickers]))" \newline \newline
         "(def " clj-name " (adapt-react-class (.-" js-name " pickers) \"mui-" clj-name "\"))" \newline)))

(defn write-picker-ns [js-name]
  (let [content (generate-picker-ns-content js-name)
        path (str "./src/core/reagent_material_ui/pickers/" (csk/->snake_case (component-ns-name js-name nil)) ".cljs")]
    (spit path content)))

(defn generate-component-ns-content [mui-ns js-ns js-name]
  (let [clj-name (csk/->kebab-case (or js-name js-ns))]
    (str "(ns reagent-material-ui." mui-ns "." (component-ns-name js-ns js-name) \newline
         "  \"Imports @material-ui/" mui-ns "/" js-ns (when js-name (str "/" js-name)) " as a Reagent component." \newline
         "   Original documentation is at https://material-ui.com/api/" clj-name "/ .\"" \newline
         (when (contains? exclude-clj clj-name)
           (str "  (:refer-clojure :exclude [" clj-name "])" \newline))
         "  (:require [reagent-material-ui.util :refer [adapt-react-class]]" \newline
         "            [\"@material-ui/" mui-ns "/" js-ns "\" :as " js-ns "]))" \newline \newline
         "(def " clj-name " (adapt-react-class " (mui-name js-ns js-name) " \"mui-" clj-name "\"))" \newline)))

(defn generate-hook-ns-content [mui-ns js-ns js-name]
  (let [clj-name (csk/->kebab-case (or js-name js-ns))]
    (str "(ns reagent-material-ui." mui-ns "." (component-ns-name js-ns js-name) \newline
         "  \"Imports @material-ui/" mui-ns "/" js-ns (when js-name (str "/" js-name)) " as a React hook." \newline
         "   Note: React hooks can't be used in regular Reagent components: http://reagent-project.github.io/docs/master/ReactFeatures.html#hooks" \newline
         "   Original documentation is at https://material-ui.com/api/" clj-name "/ .\"" \newline
         (when (contains? exclude-clj clj-name)
           (str "  (:refer-clojure :exclude [" clj-name "])" \newline))
         "  (:require [reagent-material-ui.util :refer [wrap-js-function]]" \newline
         "            [\"@material-ui/" mui-ns "/" js-ns "\" :as " js-ns "]))" \newline \newline
         "(def " clj-name " (wrap-js-function " (mui-name js-ns js-name) "))" \newline)))

(defn write-component-ns [mui-ns js-ns js-name]
  (let [hook? (str/starts-with? (or js-name js-ns) "use")
        content ((if hook? generate-hook-ns-content generate-component-ns-content)
                 mui-ns js-ns js-name)
        path (str "./src/core/reagent_material_ui/" mui-ns "/" (csk/->snake_case (component-ns-name js-ns js-name)) ".cljs")]
    (spit path content)))

(defn generate-common-core-ns-content [js-names]
  (let [clj-names (for [[js-ns js-name] js-names]
                    (csk/->kebab-case (or js-name js-ns)))]
    (str "(ns reagent-material-ui.components" \newline
         "  \"Imports all components from @material-ui/core as Reagent components." \newline
         "   Original documentation is at https://material-ui.com/api/ .\"" \newline
         "  (:refer-clojure :exclude [" (str/join " " (keep exclude-clj clj-names)) "])" \newline
         "  (:require "
         (str/join "\n            " (for [clj-name clj-names]
                                      (str "reagent-material-ui.core." (component-ns-name clj-name nil))))
         "))" \newline \newline
         (str/join \newline (for [clj-name clj-names]
                              (str "(def " clj-name " reagent-material-ui.core." (component-ns-name clj-name nil) "/" clj-name ")")))
         \newline)))

(defn get-component-names
  ([]
   (get-component-names "components.edn"))
  ([file]
   (for [elem (edn/read-string (slurp (io/resource file)))]
     (if (vector? elem) elem [elem nil]))))


(defn get-picker-names []
  (edn/read-string (slurp (io/resource "pickers.edn"))))

(defn write-deps-file []
  (let [js-names (distinct (map first (get-component-names)))
        deps {:npm-deps     {"@material-ui/core"    "4.9.7"
                             "@material-ui/pickers" "3.2.10"
                             "@material-ui/lab"     "4.0.0-alpha.46"}
              :foreign-libs [{:file           "material-ui/material-ui.inc.js"
                              :file-min       "material-ui/material-ui.min.inc.js"
                              :provides       (into ["@material-ui/core"
                                                     "@material-ui/core/styles"
                                                     "@material-ui/pickers"]
                                                    (for [js-name js-names]
                                                      (str "@material-ui/core/" js-name)))
                              :global-exports (into {"@material-ui/core"        'MaterialUI
                                                     "@material-ui/core/styles" 'MaterialUIStyles
                                                     "@material-ui/pickers"     'MaterialUIPickers}
                                                    (for [js-name js-names]
                                                      [(str "@material-ui/core/" js-name) 'MaterialUI]))
                              :requires       ["react" "react-dom"]}]
              :externs      ["material-ui/material-ui.ext.js"]}]
    (spit "./deps.cljs" (with-out-str (pprint deps)))))

(defn write-core []
  (let [js-names (get-component-names)]
    (doseq [[js-ns js-name] js-names
            :when (not (contains? exclude-core (or js-name js-ns)))]
      (write-component-ns "core" js-ns js-name))
    (doseq [[js-ns js-name] (get-component-names "lab.edn")
            :when (not (contains? exclude-core (or js-name js-ns)))]
      (write-component-ns "lab" js-ns js-name))
    (run! write-picker-ns (get-picker-names))
    (spit "./src/core/reagent_material_ui/components.cljs" (generate-common-core-ns-content js-names))))

(def ebnf "
S = element <','>?
<element> = empty-element | nonempty-element
nonempty-element = <'<'> tag proplist <'>'> element* <'</'> <tag> <'>'>
empty-element = <'<'> tag proplist <'/>'>
proplist = propvalue*
<propvalue> = prop <'=\"'> value <'\"'>
<value> = #'[^\"]*'
<prop> = 'clipRule'|'cx'|'cy'|'d'|'fill'|'fillOpacity'|'fillRule'|'id'|'opacity'|'r'|'transform'
<tag> = 'React.Fragment'|'circle'|'defs'|'g'|'path'
")

(def parser (insta/parser ebnf :auto-whitespace (insta/parser "whitespace = #'\\s+'")))

(defn element->react [element]
  (let [[type tag proplist & children] element
        react-tag (if (= "React.Fragment" tag)
                    '(.-Fragment js/React)
                    tag)
        props (some->> (next proplist)
                       (apply hash-map))
        parsed-children (for [child children]
                          (if (vector? child)
                            (element->react child)
                            (throw (ex-info "Illegal child" {:element element
                                                             :child   child}))))]
    `(~'e ~react-tag ~@(when props [(symbol "#js")]) ~props ~@parsed-children)))

(defn read-icon [path]
  (with-open [rdr (io/reader path)]
    (let [jsx (nth (line-seq rdr) 4)
          parsed (parser jsx)]
      (if (insta/failure? parsed)
        (throw (ex-info "Failed to parse" {:path   path
                                           :jsx    jsx
                                           :result parsed}))
        (element->react (second parsed))))))

(defn generate-icon-ns-content [js-name content]
  (let [clj-name (csk/->kebab-case js-name)]
    (str "(ns reagent-material-ui.icons." (icon-ns-name js-name) \newline
         "  \"Imports @material-ui/icons/" js-name " as a Reagent component.\"" \newline
         (when (contains? exclude-clj clj-name)
           (str "  (:refer-clojure :exclude [" clj-name "])" \newline))
         "  (:require-macros [reagent-material-ui.macro :refer [e]])" \newline
         "  (:require [reagent-material-ui.util :refer [create-svg-icon]]))" \newline \newline
         "(def " clj-name " (create-svg-icon " (pr-str content) \newline
         (str/join (repeat (+ 23 (count clj-name)) \space))
         \" js-name "\"))" \newline)))

(defn write-icon-ns [js-name icon-path]
  (let [icon-content (read-icon icon-path)
        clj-path (str "./src/icons/reagent_material_ui/icons/" (csk/->snake_case (icon-ns-name js-name)) ".cljs")
        ns-content (generate-icon-ns-content js-name icon-content)]
    (spit clj-path ns-content)))

(defn write-icons [^String mui-path]
  (let [f (File. mui-path)
        filenames (seq (.list f))
        js-names (sort (for [filename filenames
                             :let [[_ js-name :as matches?] (re-matches #"([A-Z].*)\.js" filename)]
                             :when matches?]
                         js-name))]
    (doseq [js-name js-names]
      (write-icon-ns js-name (str mui-path "/" js-name ".js")))))

(defn color->string [[js-name values]]
  (let [clj-name (csk/->kebab-case js-name)
        indent (str/join (repeat (+ 7 (count clj-name)) \space))]
    (str "(def " clj-name " {"
         (str/join (str \newline indent)
                   (for [[key value] values
                         :let [num-key? (contains? #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9} (first key))]]
                     (str (when-not num-key?
                            \:)
                          key
                          (when num-key? (str/join (repeat (- 5 (count key)) \space)))
                          \space \" value \")))
         "})" \newline)))

(defn generate-color-ns-content [colors]
  (str "(ns reagent-material-ui.colors" \newline
       "  \"Imports all colors from @material-ui/core/colors." \newline
       "   Original documentation is at https://material-ui.com/customization/color/ .\")" \newline \newline
       (str/join \newline (map color->string colors))))

(defn read-color-js-file [^File f]
  (with-open [rdr (io/reader f)]
    (doall
     (for [line (line-seq rdr)
           :let [[_ key color :as matches?] (re-matches #" *([0-9A]*|black|white): '([#0-9a-f]*)'," line)]
           :when matches?]
       [key color]))))

(defn write-colors [^String mui-path]
  (let [index (File. mui-path "index.js")
        js-names (with-open [rdr (io/reader index)]
                   (doall
                    (for [line (line-seq rdr)]
                      (let [[_ color] (re-matches #".*\{ default as (.*) \}.*" line)]
                        color))))
        colors (for [js-name js-names]
                 [js-name (read-color-js-file (File. mui-path (str js-name ".js")))])]
    (spit "./src/core/reagent_material_ui/colors.cljs" (generate-color-ns-content colors))))

