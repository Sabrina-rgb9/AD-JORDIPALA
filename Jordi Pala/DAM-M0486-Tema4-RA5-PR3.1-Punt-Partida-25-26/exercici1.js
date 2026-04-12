// exercici1.js
const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');
const { decode } = require('html-entities');

// Configuració
const XML_PATH = path.join(__dirname, 'data/xml/Posts.xml');
const LOG_PATH = path.join(__dirname, 'data/logs/exercici1.log');
const MONGO_URI = 'mongodb://root:password@localhost:27017';
const DB_NAME = 'stackexchange';
const COLLECTION_NAME = 'preguntes';
const LIMIT = 10000;

function log(missatge) {
  const timestamp = new Date().toISOString().replace('T', ' ').substring(0, 19);
  const linia = `${timestamp} - ${missatge}\n`;
  // Escriure en l'archiu (crea'l si no existeix)
  fs.appendFileSync(LOG_PATH, linia);
  // Mostrar per consola
  console.log(missatge);
}

async function processarXML() {
  log('Iniciant processament del XML...');

  const preguntes = [];
  const fileStream = fs.createReadStream(XML_PATH, { encoding: 'utf-8' });
  const rl = require('readline').createInterface({ input: fileStream });

  let liniaCount = 0;
  for await (const linia of rl) {
    liniaCount++;
    // Buscar línies que continguin <row .../>
    if (linia.includes('<row') && linia.includes('/>')) {
      // Extraure atributs amb una expressió regular senzilla
      const attrs = {};
      const regex = /(\w+)=["']([^"']*)["']/g;
      let match;
      while ((match = regex.exec(linia)) !== null) {
        attrs[match[1]] = match[2];
      }

      if (attrs.PostTypeId === '1') {
        // Decodificar entitats HTML en el Body
        const bodyDecodificat = attrs.Body ? decode(attrs.Body) : '';

        const document = {
          question: {
            Id: attrs.Id,
            PostTypeId: attrs.PostTypeId,
            AcceptedAnswerId: attrs.AcceptedAnswerId || null,
            CreationDate: attrs.CreationDate,
            Score: attrs.Score || '0',
            ViewCount: attrs.ViewCount || '0',
            Body: bodyDecodificat,
            OwnerUserId: attrs.OwnerUserId || null,
            LastActivityDate: attrs.LastActivityDate,
            Title: attrs.Title || '',
            Tags: attrs.Tags || '',
            AnswerCount: attrs.AnswerCount || '0',
            CommentCount: attrs.CommentCount || '0',
            ContentLicense: attrs.ContentLicense || ''
          }
        };
        preguntes.push(document);
      }
    }
    if (liniaCount % 10000 === 0) {
      log(`Processades ${liniaCount} línies... Trobades ${preguntes.length} preguntes`);
    }
  }

  log(`Total línies llegides: ${liniaCount}`);
  log(`Total preguntes trobades: ${preguntes.length}`);
  return preguntes;
}

function seleccionarTop(preguntes) {
  log(`Seleccionant les ${LIMIT} preguntes amb més ViewCount...`);
  // Ordenar de major a menor ViewCount 
  preguntes.sort((a, b) => {
    const vA = parseInt(a.question.ViewCount) || 0;
    const vB = parseInt(b.question.ViewCount) || 0;
    return vB - vA;
  });
  const top = preguntes.slice(0, LIMIT);
  log(`Seleccionades ${top.length} preguntes.`);
  if (top.length > 0) {
    log(`  - La que té més vistes: ${top[0].question.ViewCount}`);
    log(`  - La darrera seleccionada: ${top[top.length-1].question.ViewCount}`);
  }
  return top;
}

async function inserirMongo(preguntes) {
  const client = new MongoClient(MONGO_URI);
  try {
    await client.connect();
    log('Connectat a MongoDB correctament.');

    const db = client.db(DB_NAME);
    const collection = db.collection(COLLECTION_NAME);

    // Netejar la col·lecció per començar de zero 
    await collection.deleteMany({});
    log('Col·lecció netejada.');

    if (preguntes.length > 0) {
      const result = await collection.insertMany(preguntes);
      log(`${result.insertedCount} documents inserits a MongoDB.`);
    }

    const total = await collection.countDocuments();
    log(`Total documents a la col·lecció: ${total}`);
  } catch (error) {
    log(`ERROR: ${error.message}`);
    throw error;
  } finally {
    await client.close();
    log('Connexió tancada.');
  }
}

async function main() {
  // Crear carpeta de logs si no existeix
  const logDir = path.dirname(LOG_PATH);
  if (!fs.existsSync(logDir)) {
    fs.mkdirSync(logDir, { recursive: true });
  }

  // Comprovar que existeix el fitxer XML
  if (!fs.existsSync(XML_PATH)) {
    log(`ERROR: No es troba el fitxer XML a ${XML_PATH}`);
    process.exit(1);
  }

  try {
    const totesPreguntes = await processarXML();
    if (totesPreguntes.length === 0) {
      log('No s\'han trobat preguntes al fitxer.');
      process.exit(1);
    }

    const topPreguntes = seleccionarTop(totesPreguntes);
    await inserirMongo(topPreguntes);

  } catch (error) {
    log(`Error fatal: ${error.message}`);
    process.exit(1);
  }
}

// Executar
main();