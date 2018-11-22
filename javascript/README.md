This folder contains the source code of the **Circuit Simulator** for **Web2Executable** program.

The files in this folder were compiled from https://github.com/sharpie7/circuitjs1 with additional files from the **Java** folder of this repository.

### If you want to compile this code yourself, you should consider the following:
In the compiled file **0612D584F56E34298E38D85B0ECA5BD5.cache.js** (the name of the file may be another) you need to change:
```javascript
f=(On('decodedURL',a),'?cct='+encodeURI(a));
```
to
```javascript
f=('http://lushprojects.com/circuitjs/circuitjs.html?cct='+encodeURI(a));
```
or
```javascript
f=('http://{your website}/circuitjs.html?cct='+encodeURI(a));
```
if you have the web application on your website.
Next comes the code:
```javascript
a=g[0]+f;
```
Here it is necessary to delete a variable g[0] so that
```javascript
a=f;
```
This code is responsible for the function "Export As Link" in the menu.

>Take care that a file with a name similar to 0612D584F56E34298E38D85B0ECA5BD5.cache.js is compiled for safari. This can be viewed in compilation-mappings.txt. Other files with a similar name can be deleted - the NW.JS shell does not read them.
