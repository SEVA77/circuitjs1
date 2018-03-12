# Circuit Simulator (CircuitJS1)
The source code of the **Circuit Simulator** for **Web2Executable** program.

The javascript was compiled from https://github.com/sharpie7/circuitjs1.

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
