#!/bin/zsh -e
cd "$( dirname "${BASH_SOURCE[0]}" )/uberdeps"
clojure -M -m uberdeps.uberjar --deps-file ../deps.edn --target ../target/mapcheck.jar --aliases package:nrepl:...
cd "$( dirname "${BASH_SOURCE[0]}" )"