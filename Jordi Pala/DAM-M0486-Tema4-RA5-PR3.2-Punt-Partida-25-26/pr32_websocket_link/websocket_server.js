const WebSocket = require('ws');
const { MongoClient } = require('mongodb');
const winston = require('winston');
const path = require('path');

// Configuración del logger
const logger = winston.createLogger({
    level: 'info',
    format: winston.format.combine(
        winston.format.timestamp(),
        winston.format.printf(({ timestamp, level, message }) => {
            return `${timestamp} [${level.toUpperCase()}]: ${message}`;
        })
    ),
    transports: [
        new winston.transports.Console(),
        new winston.transports.File({ filename: path.join(__dirname, 'server.log') })
    ]
});

// Configuración MongoDB (sin autenticación para Windows)
const MONGO_URI = 'mongodb://localhost:27017';
const DB_NAME = 'game';
const COLLECTION_NAME = 'moviments';

let movimentsCollection;

async function connectMongo() {
    const client = new MongoClient(MONGO_URI);
    await client.connect();
    const db = client.db(DB_NAME);
    movimentsCollection = db.collection(COLLECTION_NAME);
    logger.info('Connectat a MongoDB');
    return client;
}

// Estado del servidor
const clients = new Map();
let nextPartidaId = 1;

function generarNouPartidaId() {
    return `partida_${Date.now()}_${nextPartidaId++}`;
}

function calcularDistancia(p1, p2) {
    if (!p1 || !p2) return 0;
    const dx = p2.x - p1.x;
    const dy = p2.y - p1.y;
    return Math.sqrt(dx * dx + dy * dy);
}

async function finalitzarPartida(clientId, tancamentForcat = false) {
    const clientInfo = clients.get(clientId);
    if (!clientInfo || !clientInfo.partidaId) return;

    const { partidaId, posicioInicial, posicioFinal, ws } = clientInfo;
    const distancia = calcularDistancia(posicioInicial, posicioFinal);

    logger.info(`Partida ${partidaId} finalitzada. Distància: ${distancia.toFixed(2)}`);

    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
            tipus: 'final',
            partidaId,
            distancia: distancia.toFixed(2)
        }));
    }

    if (!tancamentForcat) {
        clientInfo.partidaId = null;
        clientInfo.posicions = [];
        clientInfo.ultimMoviment = null;
        clientInfo.posicioInicial = null;
        clientInfo.posicioFinal = null;
        if (clientInfo.timeout) {
            clearTimeout(clientInfo.timeout);
            clientInfo.timeout = null;
        }
    }
}

// Servidor WebSocket
const wss = new WebSocket.Server({ port: 8080 });

wss.on('listening', () => {
    logger.info(`Servidor WebSocket escoltant al port 8080`);
});

wss.on('connection', (ws) => {
    const clientId = `client_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    logger.info(`Nou client connectat: ${clientId}`);

    clients.set(clientId, {
        ws,
        partidaId: null,
        posicions: [],
        ultimMoviment: null,
        posicioInicial: null,
        posicioFinal: null,
        timeout: null
    });

    ws.on('message', async (data) => {
        try {
            const missatge = JSON.parse(data);
            logger.info(`Missatge rebut de ${clientId}: ${JSON.stringify(missatge)}`);

            if (missatge.tipus === 'moviment') {
                const { x, y } = missatge;
                const ara = Date.now();
                const clientInfo = clients.get(clientId);

                if (!clientInfo.partidaId) {
                    clientInfo.partidaId = generarNouPartidaId();
                    clientInfo.posicions = [];
                    clientInfo.posicioInicial = { x, y };
                    clientInfo.ultimMoviment = ara;
                    logger.info(`Nova partida ${clientInfo.partidaId} per al client ${clientId}`);
                }

                const moviment = {
                    partidaId: clientInfo.partidaId,
                    clientId,
                    timestamp: new Date(ara),
                    x,
                    y
                };
                await movimentsCollection.insertOne(moviment);
                clientInfo.posicions.push({ x, y, timestamp: ara });
                clientInfo.posicioFinal = { x, y };
                clientInfo.ultimMoviment = ara;

                if (clientInfo.timeout) clearTimeout(clientInfo.timeout);

                clientInfo.timeout = setTimeout(() => {
                    finalitzarPartida(clientId);
                }, 10000);

                ws.send(JSON.stringify({ tipus: 'ok', x, y }));
            }
        } catch (err) {
            logger.error(`Error processant missatge: ${err.message}`);
        }
    });

    ws.on('close', () => {
        logger.info(`Client desconnectat: ${clientId}`);
        const clientInfo = clients.get(clientId);
        if (clientInfo) {
            if (clientInfo.timeout) clearTimeout(clientInfo.timeout);
            if (clientInfo.partidaId) {
                finalitzarPartida(clientId, true);
            }
            clients.delete(clientId);
        }
    });
});

// Inicio
async function main() {
    await connectMongo();
}

main().catch(err => {
    logger.error('Error crític: ' + err.message);
    process.exit(1);
});