let client = null;
let channels = new Map();

function showLog(logBody) {
    const log = document.createElement("p");
    log.appendChild(document.createTextNode(logBody));
    const logsElement = document.getElementById("logs");
    logsElement.appendChild(log);
}

function createConnection() {
    // client = Stomp.client("wss://for4chat.herokuapp.com/chat")
    client = Stomp.client("ws://localhost:8080/queue")
    console.log('Stomp connect');
    client.connect({},
        function (frame) {
        },
        function (frame) {
            createConnection();
        });
}

function subscribe() {
    const channel = document.getElementById("Channel").value.trim();
    let subscription = client.subscribe("/game/" + channel, function (message) {
        const body = JSON.parse(message.body);
        let logBody = "";
        logBody = logBody.concat("|| id: ", body.id);
        logBody = logBody.concat( " || status: ", body.status);
        logBody = logBody.concat(" || players: ", body.players.length);
        logBody = logBody.concat(" || pawns: ", body.pawns.length);
        showLog(logBody);
    });
    channels.set(channel, subscription);
}

function unsubscribe() {
    const channel = document.getElementById("Channel").value.trim();
    let subscription = channels.get(channel);
    subscription.unsubscribe();
}
