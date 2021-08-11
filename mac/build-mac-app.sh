#!/bin/bash
#
# Builds edisen.app for OS X.  This assumes you've already run
# ./gradlew clean build installDist.  In fact, you usually don't
# run this directly but rather just run
# ./gradlew clean build installDist generateJre generateMacApp
#

# The version of edisen you're building.  This appears in the generated
# .dmg file name.
APP_VERSION=1.0.0

#
# You probably don't want to change anything below this line.
#

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd "${SCRIPT_DIR}/../build/install" || exit

jpackage --input edisen \
  --icon "${SCRIPT_DIR}/edisen.icns" \
  --name Edisen \
  --app-version "${APP_VERSION}" \
  --main-class org.fife.edisen.ui.Main \
  --main-jar lib/edisen-${APP_VERSION}.jar
