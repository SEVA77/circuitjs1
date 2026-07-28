# CircuitJS1 Tauri Desktop Wrapper

[中文说明](README.zh-CN.md)

This directory packages CircuitJS1 as a Tauri 2 desktop application for
Windows and Linux. Tauri embeds the static application in the sibling `war`
directory; no web server is required at runtime.

## Required first step: compile CircuitJS1

The Tauri project only packages existing web files. It does **not** compile
the Java/GWT application. Before running either Tauri build script, compile
CircuitJS1 from the repository root:

```text
npm install
npm run buildgwt
```

Then copy the entire generated GWT runtime:

```powershell
Copy-Item -Recurse -Force target\site\circuitjs1 war\circuitjs1
```

Verify this file exists before packaging:

```text
war/circuitjs1/circuitjs1.nocache.js
```

Copying only `circuitjs1.nocache.js` is not sufficient because it loads the
`*.cache.js` files and other resources in the same directory.

## Changes

- Added a minimal Tauri 2/Rust wrapper in `tauri/src-tauri`.
- Configured `war/circuitjs.html` as the desktop entry page.
- Added an NSIS setup executable target for Windows.
- Added AppImage and Debian package targets for Linux.
- Added platform build scripts and a GitHub Actions workflow.
- Added the Windows GUI subsystem flag to prevent a console window flashing
  during startup.
- Forced the application crate to be rebuilt before packaging so edits in
  `war` are always embedded instead of being skipped by Cargo's cache.

## Web assets

The application expects the compiled GWT runtime at:

```text
war/circuitjs1/circuitjs1.nocache.js
```

The entire `war/circuitjs1` directory is generated and ignored by Git. A clean
checkout can generate it with:

```text
npm install
npm run buildgwt
```

The complete website will be in `target/site`. Copy its generated runtime into
`war` before a local Tauri build:

```powershell
Copy-Item -Recurse -Force target\site\circuitjs1 war\circuitjs1
```

GitHub Actions performs these steps automatically.

## Windows build

Requirements:

- Node.js 20 or newer (the script uses `nvm use 24` when nvm-windows exists)
- Rust stable with the MSVC toolchain
- Microsoft C++ Build Tools with “Desktop development with C++”
- Microsoft Edge WebView2
- Compiled `war/circuitjs1` assets

From the repository root:

```powershell
.\tauri\build-windows.ps1
```

Outputs:

```text
tauri/src-tauri/target/release/circuitjs-tauri.exe
tauri/src-tauri/target/release/bundle/nsis/*-setup.exe
```

Close a running `circuitjs-tauri.exe` before rebuilding, otherwise Windows may
lock the output file.

## Linux build

Linux packages must be built on Linux. For Debian/Ubuntu, install the Rust,
Node.js, and Tauri WebKit dependencies, then run from the repository root:

```bash
chmod +x tauri/build-linux.sh
./tauri/build-linux.sh
```

Outputs are written below:

```text
tauri/src-tauri/target/release/bundle/deb/
tauri/src-tauri/target/release/bundle/appimage/
```

## GitHub Actions

Run the **Build Tauri desktop packages** workflow manually, or push a tag:

```text
tauri-v1.2.5
```

The workflow builds and uploads separate Windows and Linux artifacts.

## Usage

Windows users can run the standalone executable or install the NSIS setup
package. Linux users can install the `.deb` package or make the `.AppImage`
executable and launch it:

```bash
chmod +x CircuitJS1*.AppImage
./CircuitJS1*.AppImage
```

The application is offline and does not require a local web server.
