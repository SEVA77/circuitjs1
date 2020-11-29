# Circuit Simulator (CircuitJS1)

![](https://my77thblog.pp.ua/projects/demo/circuitjs1/circuitjs_1_2_1.PNG)

The source code for offline version of the **Circuit Simulator**. It was originally written by Paul Falstad as a Java Applet. It was adapted by Iain Sharp to run in the browser using GWT. The program was compiled to offline version for Windows (x32, x64), Linux (x32, x64) and MacOS (x64) by Usevalad Khatkevich.

This program is distributed by me as a program for education. It is not recommended to use the program for modeling real circuits, since many components in the program are idealized.

The program supports the following languages:
- English
- Russian
- Danish
- German
- Polish
- Spanish
- French
- Italian
- Portuguese

For a web version of the application see:

Paul's Page: http://www.falstad.com/circuit/  
Iain's Page: http://lushprojects.com/circuitjs/

# Downloads:

You can download this program for Windows (x32, x64), Linux (x32, x64) and Mac OS X (x64):
- [Circuit Simulator version 1.2.1](https://github.com/SEVA77/circuitjs1/releases/tag/1.2.1)
- [All versions...](https://github.com/SEVA77/circuitjs1/releases)

> If you have problems with this application, you can try to use [this offline application of the main developer](http://www.falstad.com/circuit/offline/).

# Building the program

If you want to building the web application for your website, you can use the instructions and source code of Iain Sharp (https://github.com/sharpie7/circuitjs1) or Paul Falstad (https://github.com/pfalstad/circuitjs1) or use archive (java/circuitjs1-master.zip) from this repository. 

After compiling the web application, you can then compile it into the local executables for a variety of platforms with the help of NW.js. To build a program on NW.js you will need a **Web2Executable** program. You can download it by following the link: https://github.com/jyapayne/Web2Executable/releases.

The compiled program has a large size. To reduce the size of executable files of the program, you can use to **Ultimate Packer for eXecutables** (UPX). You can download it by following the link: https://upx.github.io/.

> ***Warning! Not all executable files need to be compressed. For Windows, you can compress everything, except for the file d3dcompiler_47.dll. For Linux, you can compress only those files that are in the 'lib' folder. Otherwise, the program does not work.***
>
> > ***Therefore, I do not recommend the use of compression settings in Web2Executable.***


# License

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

     Components of this program:
    JQuery - Distributed under the MIT License
    jQuery.NiceScroll - Distributed under the MIT License
    jQuery.scrollSpeed - is taken from the github.com/nathco/jQuery.scrollSpeed
    
    NW.js - Distributed under the MIT License
    Circuit Simulator - Distributed under the GNU General Public License version 2
    
    © Usevalad Khatkevich 2020

# Credits
	 Paul Falstad - Creator
	 Iain Sharp - JavaScript conversion, so there are more opportunities for the development of this application.
