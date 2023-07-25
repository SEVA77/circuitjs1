# Circuit Simulator (CircuitJS1)

![](https://my77thblog.pp.ua/images/PROJECTS/circuit-simulator/circuitjs1-1-2-3-en.png)

The source code for offline version of the **Circuit Simulator** based on [NW.js](https://github.com/nwjs/nw.js). It was originally written by Paul Falstad as a Java Applet. It was adapted by Iain Sharp to run in the browser using GWT. The program was compiled to offline version for Windows (x32, x64), Linux (x32, x64) and MacOS (x64) by Usevalad Khatkevich.

This program is distributed by me as a program for education. It is not recommended to use the program for modeling real circuits, since many components in the program are idealized.

The program supports the following languages: English, Russian, Danish, German, Polish, Spanish, French, Italian, Portuguese, Czech, Norwegian, Chinese.

For a web version of the application see:

Paul's Page: https://www.falstad.com/circuit/ \
Source code: https://github.com/pfalstad/circuitjs1

Iain's Page: https://lushprojects.com/circuitjs/ \
Source code: https://github.com/sharpie7/circuitjs1

## Downloads:

You can download this program for Windows (x32, x64), Linux (x32, x64) and Mac OS X (x64):
- [Latest release](https://github.com/SEVA77/circuitjs1/releases/latest)
- [All Releases](https://github.com/SEVA77/circuitjs1/releases)

> If you have problems with this application, you can try to use [this offline application of the main developer](http://www.falstad.com/circuit/offline/) based on Electron.

## Building the program

The tools you will need to build the project are:

* JDK 8+
* Maven 3+
* [Web2Executable](https://github.com/jyapayne/Web2Executable) or [nw-builder](https://github.com/nwutils/nw-builder) (more preferably)

Simply run `mvn install` from the circuitjs1 directory to build the project. Project directory after compilation for further building or debugging: `target/site`

After compiling the web application, you can then compile it into the local executables for a variety of platforms with the help of NW.js. To build this program on NW.js you will need a [Web2Executable](https://github.com/jyapayne/Web2Executable) or [nw-builder](https://github.com/nwutils/nw-builder). Command to compile the program via nw-builder:

```
nwbuild --glob=false target/site
```

For development run devmode:

```
mvn gwt:devmode
```

Devmode works directly in the `war` directory separate from the `target/site` directory.

## License

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

     Components of this program:
    JQuery - Distributed under the MIT License
    jQuery.NiceScroll - Distributed under the MIT License
    jQuery.scrollSpeed - is taken from the github.com/nathco/jQuery.scrollSpeed
    
    NW.js - Distributed under the MIT License
    Circuit Simulator - Distributed under the GNU General Public License version 2
    
    © Usevalad Khatkevich 2023

## Credits
	 Paul Falstad - Creator
	 Iain Sharp - JavaScript conversion, so there are more opportunities for the development of this application.
	 Brian Gordon - Mavenized version of circuitjs1
