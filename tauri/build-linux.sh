#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

node_major="$(node --version | sed -E 's/^v([0-9]+).*/\1/')"
if (( node_major < 20 )); then
  echo "Node.js 20 or newer is required; found $(node --version)." >&2
  exit 1
fi

cd "$script_dir"
npm install
cargo clean --manifest-path src-tauri/Cargo.toml -p circuitjs-tauri
npm run build:linux
