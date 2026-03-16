#!/bin/bash
# Pre-commit security scanner — blocks if secrets are found in staged files

FILES=$(git diff --cached --name-only 2>/dev/null)

# Only scan when there are staged files — skip for general shell commands
if [ -z "$FILES" ]; then
  exit 0
fi

# Exclude example/template files, build wrappers, and this script from scanning
SCANNABLE=$(echo "$FILES" | grep -v '\.env\.example$' | grep -v 'scripts/sec\.sh$' | grep -v 'docker-compose.*\.yml$' | grep -v 'mvnw' | grep -v 'application\.yml$')

if [ -z "$SCANNABLE" ]; then
  exit 0
fi

FILES="$SCANNABLE"

PATTERNS=("API_KEY\s*=" "SECRET\s*=" "PASSWORD\s*=" "-----BEGIN.*PRIVATE KEY-----" "sk-[a-zA-Z0-9]{20}")

FOUND=0
for p in "${PATTERNS[@]}"; do
  if echo "$FILES" | xargs grep -l "$p" 2>/dev/null; then
    echo "⚠️  Potential secret matching '$p'" >&2
    FOUND=1
  fi
done

if [ $FOUND -eq 1 ]; then
  echo "❌ Secrets detected. Remove them before committing." >&2
  exit 2
else
  exit 0
fi
