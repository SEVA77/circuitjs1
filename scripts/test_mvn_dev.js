console.log("test this dev...");

const mvn = require('maven').create({
    cwd: '.'
  });
mvn.execute("gwt:devmode", { 'skipTests': true }).then(() => {
    // As mvn.execute(..) returns a promise, you can use this block to continue
    // your stuff, once the execution of the command has been finished successfully.
    console.log("Complite!");
  });
