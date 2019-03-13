// var strt="";
// for (var i=0; i < localStorage.length; i++) {
//   strt += "Key: " + localStorage.key(i) + "; \n Value: " + localStorage.getItem(localStorage.key(i)) + "\n \n";
// }
// console.log(strt);


// !!!Нужен обработчик ошибки, когда отсутствует ключ 'shortcuts'
let shortcuts_data = localStorage.getItem('shortcuts');
// console.log(shortcuts_data);

let shortcuts_data_split = shortcuts_data.split(";");
// console.log("Value in array:" + shortcuts_data_split);
// console.log("In shortcuts_data_split array " + shortcuts_data_split.length + " elements");

function gen_t() {
  let shortcuts_data_endsplit = new Array();
  for (let i = 1; i < shortcuts_data_split.length; i++) { //ignore shortcuts_data_split[0] = '1'
    shortcuts_data_endsplit[i] = shortcuts_data_split[i].split("=");
    // console.log(shortcuts_data_endsplit[i]);
  }
  console.log(shortcuts_data_endsplit);
  // Получили новый массив. Это супер!!!!

};

gen_t();