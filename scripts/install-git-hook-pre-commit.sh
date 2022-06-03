#!/usr/bin/env bash

PRJ_ROOT=$(git rev-parse --show-toplevel)

cp "$PRJ_ROOT/scripts/git-hook-pre-commit.sh" "$PRJ_ROOT/.git/hooks/pre-commit"
