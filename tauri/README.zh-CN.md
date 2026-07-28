# CircuitJS1 Tauri 桌面封装

[English](README.md)

## 必须先编译 CircuitJS1

Tauri 工程只负责包装已有的网页文件，**不会**编译 Java/GWT 应用。执行任何
Tauri 打包脚本之前，必须先在仓库根目录编译 CircuitJS1：

```text
npm install
npm run buildgwt
```

然后复制完整的 GWT 运行目录：

```powershell
Copy-Item -Recurse -Force target\site\circuitjs1 war\circuitjs1
```

打包前确认以下文件存在：

```text
war/circuitjs1/circuitjs1.nocache.js
```

不能只复制 `circuitjs1.nocache.js`，因为它还会加载同目录的
`*.cache.js`、示例电路、语言包和其他资源。缺少完整目录时，程序窗口虽然
可以打开，但模拟器菜单、画布和元件不会加载。

本目录使用 Tauri 2 将 CircuitJS1 封装为 Windows 和 Linux 桌面程序。Tauri
直接嵌入同级目录 `war` 中的静态网页，程序运行时不需要启动 Web 服务器。

## 本次改动

- 在 `tauri/src-tauri` 中新增精简的 Tauri 2/Rust 外壳。
- 使用 `war/circuitjs.html` 作为桌面程序入口。
- Windows 输出独立 EXE 和 NSIS 安装程序。
- Linux 输出 AppImage 和 Debian 安装包。
- 新增 Windows、Linux 构建脚本和 GitHub Actions 双平台流水线。
- Windows Release 使用 GUI 子系统，解决启动时闪现黑色控制台窗口的问题。
- 打包前强制重新编译应用外壳，确保 `war` 的最新改动被嵌入，不再复用
  Cargo 缓存中的旧网页。

## Web 资源

程序需要以下 GWT 编译入口：

```text
war/circuitjs1/circuitjs1.nocache.js
```

`war/circuitjs1` 是生成目录，已被 Git 忽略。全新克隆的仓库可执行：

```text
npm install
npm run buildgwt
```

完整网页将生成在 `target/site`。本地打包 Tauri 前，把运行目录复制到
`war`：

```powershell
Copy-Item -Recurse -Force target\site\circuitjs1 war\circuitjs1
```

GitHub Actions 会自动执行编译和复制，不需要人工处理。

## Windows 打包

环境要求：

- Node.js 20 或更高版本；检测到 nvm-windows 时脚本自动执行 `nvm use 24`
- Rust stable MSVC 工具链
- Microsoft C++ Build Tools，并安装“使用 C++ 的桌面开发”
- Microsoft Edge WebView2
- `war/circuitjs1` 中已有完整编译资源

在仓库根目录运行：

```powershell
.\tauri\build-windows.ps1
```

输出位置：

```text
tauri/src-tauri/target/release/circuitjs-tauri.exe
tauri/src-tauri/target/release/bundle/nsis/*-setup.exe
```

重新打包前请关闭正在运行的 `circuitjs-tauri.exe`，否则 Windows 可能锁定
输出文件。

## Linux 打包

Linux 安装包必须在 Linux 系统中构建。在 Debian/Ubuntu 上安装 Rust、
Node.js 和 Tauri 所需的 WebKit 系统依赖后，在仓库根目录运行：

```bash
chmod +x tauri/build-linux.sh
./tauri/build-linux.sh
```

输出位置：

```text
tauri/src-tauri/target/release/bundle/deb/
tauri/src-tauri/target/release/bundle/appimage/
```

## GitHub Actions

可在 GitHub Actions 中手动运行 **Build Tauri desktop packages**，也可以推送
以下格式的标签自动构建：

```text
tauri-v1.2.5
```

流水线会分别上传 Windows 和 Linux 构建产物。

## 使用方法

Windows 可以直接运行 `circuitjs-tauri.exe`，也可以运行 `*-setup.exe` 安装。

Linux 可以安装 `.deb`，或直接运行 AppImage：

```bash
chmod +x CircuitJS1*.AppImage
./CircuitJS1*.AppImage
```

应用完全离线运行，不需要本地 Web 服务器。
