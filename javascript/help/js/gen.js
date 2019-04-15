// var strt="";
// for (var i=0; i < localStorage.length; i++) {
//   strt += "Key: " + localStorage.key(i) + "; \n Value: " + localStorage.getItem(localStorage.key(i)) + "\n \n";
// }
// console.log(strt);


// (!!!)Нужен обработчик ошибки, когда отсутствует ключ 'shortcuts'
let shortcuts_data = localStorage.getItem('shortcuts');
// console.log(shortcuts_data);

let shortcuts_data_split = shortcuts_data.split(";");
// console.log("Value in array:" + shortcuts_data_split);
// console.log("In shortcuts_data_split array " + shortcuts_data_split.length + " elements");

function gen_t() {
  let shortcuts_data_endsplit = new Array();
  for (let i = 1; i < shortcuts_data_split.length; i++) { //ignore shortcuts_data_split[0] = '1'
    shortcuts_data_endsplit[i-1] = shortcuts_data_split[i].split("=");
    // console.log(shortcuts_data_endsplit[i]);
  }
  console.log(shortcuts_data_endsplit);
  // console.log('l = ' + shortcuts_data_endsplit.length);
  // Получили новый массив. Это супер!!!!

  /**
  <tr>
  <td><center><font class="_1"><b>&nbsp;W&nbsp;</b></font></center></td>
  <td><center><font class="_2"><b>&rArr;</b></font></center></td>
  <td><center><img class="_3" src="help/bk_img/wire.png"></center></td>
  </tr>

  <tr>
  <td height="40px"><center><font class="Shift"><b>&nbsp;Shift&nbsp;</b></font><br><font size="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#10010;</font><font class="Shift"><b>&nbsp;L&nbsp;</b></font></center></td>
  <td><center><font class="_2"><b>&rArr;</b></font></center></td>
  <td><center><img class="_3" src="help/bk_img/inductor.png"></center></td>
  </tr>
  **/

$( document ).ready(function(){
  // $("table.gen_c").html()
  // console.log("in jquery: " + shortcuts_data_endsplit);
  var gen_content;

  //(!) Сделать сортировку!
// (!) УЧЕСТЬ ВЕРХНИЙ И НИЖНИЙ РЕГИСТРЫ!!!
  for (let i = 0; i < shortcuts_data_endsplit.length; i++) {
    gen_content += '<tr><td><center><font class="_1"><b>'+String.fromCharCode(shortcuts_data_endsplit[i][0])+'</b></font></center></td><td><center><font class="_2"><b>&rArr;</b></font></center></td><td><center><font class="_1"><b>'+shortcuts_data_endsplit[i][1]+'</b></font></center></td></tr>';
  };
   // console.log(gen_content);
   $("table.gen_c").html(gen_content);
	});



};

gen_t();
