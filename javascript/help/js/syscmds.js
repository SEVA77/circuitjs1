function open_webapp(wa_link) { // wa - web app)))

    let langtext_wa = new Array();

    //English:
    langtext_wa[0] = " This experimental function loads the web application into the window of this program. You have selected the application located at: \n" + wa_link + "\n This function is created for the case that there are no new versions of this program for a long time. The previous state of this program can be returned only by restarting the program. \n Are you sure you want to launch the web application? (Need Internet access!)";
    //Russian:
    langtext_wa[1] = " Эта экспериментальная функция загружает веб приложение в окно этой программы. Вы выбрали приложение, находящееся по адресу: \n" + wa_link + "\n Эта функция создана в случае, если длительное время нет новых версий этой программы. Прежнее состояние можно будет вернуть только перезапустив программу.\n Вы уверены, что хотите запустить веб приложение? (Нужен доступ в интернет!)";

    function language_wa() {
        var lang_value = localStorage.getItem('language');
        //console.log(typeof(lang_value));
        if (lang_value == null || lang_value == "en") {
            //console.log("language is en or null");
            return langtext_wa[0];
        } else if (lang_value == "ru") {
            //console.log("language is ru");
            return langtext_wa[1];
        } else {
            //console.log("language is other value");
            return langtext_wa[0];
        }
    }

    if (confirm(language_wa())) {
        nw.Window.get().on('new-win-policy', function (frame, url, policy) {
            policy.ignore();
            nw.Shell.openExternal(url);
            return document.location.href = "wa_link";
        });
    }
}
