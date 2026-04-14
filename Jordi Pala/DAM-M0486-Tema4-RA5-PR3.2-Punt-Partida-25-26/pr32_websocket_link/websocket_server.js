const WebSocket = require('ws');
const { MongoClient } = require('mongodb');
const winston = require('winston');
const path = require('path');

// Configuración del logger
const logger = winston.createLogger({
    level: 'info',
    format: winston.format.combine(
        // Agrega timestamp y formato personalizado a los logs
        winston.format.timestamp(),
        winston.format.printf(({ timestamp, level, message }) => {
            return `${timestamp} [${level.toUpperCase()}]: ${message}`;
        })
    ),
    // Configura los transportes para mostrar logs en consola y guardarlos en un archivo
    transports: [
        new winston.transports.Console(),
        new winston.transports.File({ filename: path.join(__dirname, 'server.log') })
    ]
});

// Configuración MongoDB, conexion mediante el driver oficial de MongoDB para Node.js
const MONGO_URI = 'mongodb://localhost:27017'; // URI de conexión a MongoDB
const DB_NAME = 'game'; // Nombre de la base de datos
const COLLECTION_NAME = 'moviments'; // Nombre de la colección para guardar los movimientos

let movimentsCollection;

async function conectarMongo() {
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

// Función para calcular la distancia entre dos puntos
function calcularDistancia(p1, p2) {
    if (!p1 || !p2) return 0;
    const dx = p2.x - p1.x;
    const dy = p2.y - p1.y;
    return Math.sqrt(dx * dx + dy * dy); // sqrt es la función de raíz cuadrada para calcular la distancia euclidiana
}

// Función para finalizar una partida
async function finalitzarPartida(clientId, tancamentForcat = false) {
    const clientInfo = clients.get(clientId);
    if (!clientInfo || !clientInfo.partidaId) return; // No hay partida activa para este cliente

    // Calcular distancia recorrida
    const { partidaId, posicioInicial, posicioFinal, ws } = clientInfo;
    const distancia = calcularDistancia(posicioInicial, posicioFinal);

    logger.info(`Partida ${partidaId} finalitzada. Distància: ${distancia.toFixed(2)}`);

    // Enviar mensaje de finalización al cliente
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
            tipus: 'final',
            partidaId,
            distancia: distancia.toFixed(2)
        }));
    }

    // Limpiar estado del cliente
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

    // recepció de moviments del client
    ws.on('message', async (data) => {
        try {
            const missatge = JSON.parse(data); // parseja el missatge rebut de string a objecte
            logger.info(`Missatge rebut de ${clientId}: ${JSON.stringify(missatge)}`); // logueja el missatge rebut

            // Processar el missatge de moviment
            if (missatge.tipus === 'moviment') {
                const { x, y } = missatge;
                const ara = Date.now();
                const clientInfo = clients.get(clientId);

                // Si el client no té una partida activa, en crea una de nova
                if (!clientInfo.partidaId) {
                    clientInfo.partidaId = generarNouPartidaId();
                    clientInfo.posicions = [];
                    clientInfo.posicioInicial = { x, y };
                    clientInfo.ultimMoviment = ara;
                    logger.info(`Nova partida ${clientInfo.partidaId} per al client ${clientId}`);
                }

                // Guarda el moviment a MongoDB
                const moviment = {
                    partidaId: clientInfo.partidaId, // ID de la partida
                    clientId, // ID del client que ha fet el moviment
                    timestamp: new Date(ara), // timestamp del moviment
                    x, 
                    y
                };
                // Inserta el moviment a la base de dades
                await movimentsCollection.insertOne(moviment);
                clientInfo.posicions.push({ x, y, timestamp: ara });
                clientInfo.posicioFinal = { x, y };
                clientInfo.ultimMoviment = ara;

                // Si el client té un timeout actiu, el neteja
                if (clientInfo.timeout) clearTimeout(clientInfo.timeout);
                
                // Si no hi ha moviment en 10 segons, finalitza la partida ya que cada moviment reinicia el timeout
                clientInfo.timeout = setTimeout(() => {
                    finalitzarPartida(clientId);
                }, 10000);

                ws.send(JSON.stringify({ tipus: 'ok', x, y }));
            }
        } catch (err) {
            logger.error(`Error processant missatge: ${err.message}`); // logueja l'error
        }
    });

    ws.on('close', () => {
        logger.info(`Client desconnectat: ${clientId}`);
        const clientInfo = clients.get(clientId);
        // Si el client es desconecta, finalitza la partida si està activa i neteja el timeout
        if (clientInfo) {
            if (clientInfo.timeout) clearTimeout(clientInfo.timeout);
            if (clientInfo.partidaId) {
                finalitzarPartida(clientId, true);
            }
            clients.delete(clientId);clo
        }
    });
});

// Inicio
async function main() {
    await conectarMongo();
}

main().catch(err => {
    logger.error('Error crític: ' + err.message);
    process.exit(1);
});