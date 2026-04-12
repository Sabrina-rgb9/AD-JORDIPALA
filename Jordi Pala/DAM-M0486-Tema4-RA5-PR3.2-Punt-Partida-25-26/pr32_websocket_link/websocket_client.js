const WebSocket = require('ws');
const readline = require('readline');

// Configurar captura de teclas
readline.emitKeypressEvents(process.stdin);
if (process.stdin.isTTY) {
    process.stdin.setRawMode(true);
}
process.stdin.resume();

let posicio = { x: 0, y: 0 };
let connectat = false;
let ws;

function connectar() {
    ws = new WebSocket('ws://localhost:8080');

    ws.on('open', () => {
        console.log('✅ Connectat al servidor. Utilitza les fletxes per moure\'t, \'q\' per sortir.');
        connectat = true;
    });

    ws.on('message', (data) => {
        const missatge = JSON.parse(data);
        if (missatge.tipus === 'final') {
            console.log(`\n🏁 Partida ${missatge.partidaId} finalitzada! Distància recorreguda: ${missatge.distancia} unitats.\n`);
        }
    });

    ws.on('close', () => {
        console.log('❌ Connexió tancada pel servidor.');
        connectat = false;
        process.exit(0);
    });

    ws.on('error', (err) => {
        console.error('Error de connexió:', err.message);
    });
}

// Capturar tecles
process.stdin.on('keypress', (str, key) => {
    if (key.name === 'q' || (key.ctrl && key.name === 'c')) {
        console.log('Sortint...');
        if (ws) ws.close();
        process.exit(0);
    }

    if (!connectat) return;

    switch (key.name) {
        case 'up': posicio.y += 1; break;
        case 'down': posicio.y -= 1; break;
        case 'left': posicio.x -= 1; break;
        case 'right': posicio.x += 1; break;
        default: return;
    }

    console.log(`📍 Posició: (${posicio.x}, ${posicio.y})`);
    ws.send(JSON.stringify({ tipus: 'moviment', x: posicio.x, y: posicio.y }));
});

console.log('🎮 Client de joc WebSocket');
connectar();