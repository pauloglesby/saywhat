#!/usr/bin/env bash

if grep -q 'addCommandAlias("preCommit",' build.sbt; then
  exec sbt preCommit
fi
exit 0
