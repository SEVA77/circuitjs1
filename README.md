# Circuit Simulator (CircuitJS1)

![](https://pbs.twimg.com/media/Dmglty4XoAAOL3U.jpg:large)

The source code of the **Circuit Simulator**. It was originally written by Paul Falstad as a Java Applet. It was adapted by Iain Sharp to run in the browser using GWT. The program was ported to Windows (x32, x64), Linux (x32, x64) and MacOS (x64) by Usevalad Khatkevich.

This program is distributed by me as a program for education. It is not recommended to use the program for modeling real circuits, since many components in the program are idealized.

The program supports the following languages:
- English
- Russian
- Danish
- German
- Polish

For a web version of the application see:

Paul's Page: http://www.falstad.com/circuit/  
Iain's Page: http://lushprojects.com/circuitjs/

# Downloads:

You can download this program for Windows (x32, x64), Linux (x32, x64) and Mac OS X (x64):
- [Circuit Simulator version 1.0.2](https://github.com/CEBA77/circuitjs1/releases/tag/1.0.2)
- [Circuit Simulator version 1.0.1](https://github.com/CEBA77/circuitjs1/releases/tag/1.0.1)
- [Circuit Simulator version 1.0.0](https://github.com/CEBA77/circuitjs1/releases/tag/1.0.0)
- [All versions...](https://github.com/CEBA77/circuitjs1/releases)

# Building the program

If you want to building the web application for your website, you can use the instructions of Iain Sharp (https://github.com/sharpie7/circuitjs1) or Paul Falstad (https://github.com/pfalstad/circuitjs1).

After compiling the web application, you can then compile it into the local executables for a variety of platforms with the help of NW.js. To build a program on NW.js you will need a **Web2Executable** program. You can download it by following the link: https://github.com/jyapayne/Web2Executable/releases.

The compiled program has a large size. To reduce the size of executable files of the program, you can use to **Ultimate Packer for eXecutables** (UPX). You can download it by following the link: https://upx.github.io/.

> ***Warning! Not all executable files need to be compressed. For Windows, you can compress everything, except for the file d3dcompiler_47.dll. For Linux, you can compress only those files that are in the 'lib' folder. Otherwise, the program does not work.***
>> ***Therefore, I do not recommend the use of compression settings in Web2Executable.***

# License

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
