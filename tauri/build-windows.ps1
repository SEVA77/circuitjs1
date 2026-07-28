$ErrorActionPreference = "Stop"

if (Get-Command nvm -ErrorAction SilentlyContinue) {
    & nvm use 24
    if ($LASTEXITCODE -ne 0) {
        throw "Unable to switch to Node.js 24 with nvm."
    }
}

$nodeVersion = [version]((node --version).TrimStart("v"))
if ($nodeVersion.Major -lt 20) {
    throw "Node.js 20 or newer is required."
}

Write-Host "Using Node.js $nodeVersion"

Push-Location $PSScriptRoot
try {
    npm install
    if ($LASTEXITCODE -ne 0) { throw "npm install failed with exit code $LASTEXITCODE." }
    cargo clean --manifest-path "src-tauri\Cargo.toml" -p circuitjs-tauri
    if ($LASTEXITCODE -ne 0) { throw "Unable to refresh embedded web assets." }
    npm run build:windows
    if ($LASTEXITCODE -ne 0) { throw "Tauri build failed with exit code $LASTEXITCODE." }
} finally {
    Pop-Location
}
