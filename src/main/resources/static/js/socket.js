var socket = new SockJS('/secured/game');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {

    var url = stompClient.ws._transport.url;
    console.log(stompClient.ws._transport.url);
    url = url.replace("wss://task6-deploy.herokuapp.com/secured/game/",  "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    console.log("Your current session is: " + url);
    var sessionId = url;

    stompClient.subscribe('/secured/user/queue/sp-user' + "-user" + sessionId, function (messageOutput) {
        showMessageOutput(JSON.parse(messageOutput.body));
    });
});

function determineSymbol(symbol) {
    result = symbol === 'X'
        ? "<img src=\"/img/cross.png\">"
        : "<img src=\"/img/circle.png\">";

    return result;
}

function sendMessage(x, y) {

    var from = document.getElementById("player1").innerText;
    var to = document.getElementById("player2").innerText;
    var game = document.getElementById("game").innerText;

    stompClient.send("/app/secured/game", {},
        JSON.stringify({'from': from, 'to': to, 'x' : x, 'y' : y, 'gameName' : game}));
}


function showMessageOutput(messageOutput) {

    if (messageOutput.action === "addname") {
        document.getElementById("player2").innerHTML = messageOutput.from;
    } else if (messageOutput.action === "move") {
        var result = determineSymbol(messageOutput.result);

        document.getElementById("table")
            .rows[messageOutput.x]
            .cells[messageOutput.y]
            .children[0].innerHTML = result;

    } else if (messageOutput.action === "endgame") {
        alert(messageOutput.gameResult);
        window.location.href = "/";
    }
}