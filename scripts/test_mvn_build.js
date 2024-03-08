console.log("test this work...");
const mvn = require('maven').create({
    cwd: '.'
  });
mvn.execute(['clean', 'install'], { 'skipTests': true }).then(() => {
    // As mvn.execute(..) returns a promise, you can use this block to continue
    // your stuff, once the execution of the command has been finished successfully.
    console.log("Complite!");
  });