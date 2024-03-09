let url = "http://127.0.0.1:8888";

fetch(url)
.then(function (response) {
    if (response.status >= 200 && response.status < 300) {
        return console.log(response.status);
    }
})
.catch ((error) => {
    console.log('Request failed')
})
