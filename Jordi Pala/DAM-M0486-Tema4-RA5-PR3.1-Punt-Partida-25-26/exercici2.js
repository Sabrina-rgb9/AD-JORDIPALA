// exercici2.js
const fs = require('fs');
const path = require('path');
const { MongoClient } = require('mongodb');
const PDFDocument = require('pdfkit');

// Configuració
const MONGO_URI = 'mongodb://root:password@localhost:27017';
const DB_NAME = 'stackexchange';
const COLLECTION_NAME = 'preguntes';
const OUT_DIR = path.join(__dirname, 'data/out');

// Assegurar que el directori de sortida existeix
if (!fs.existsSync(OUT_DIR)) {
    fs.mkdirSync(OUT_DIR, { recursive: true });
}

// Paraules a cercar al títol (segona consulta)
const PARAULES = ["pug", "wig", "yak", "nap", "jig", "mug", "zap", "gag", "oaf", "elf"];

// Funció per connectar a MongoDB
async function connectDB() {
    const client = new MongoClient(MONGO_URI);
    await client.connect();
    console.log('Connectat a MongoDB');
    return client;
}

// Funció per generar un PDF amb una llista de títols
function generarPDF(titols, nomFitxer, titolInforme) {
    return new Promise((resolve, reject) => {
        const doc = new PDFDocument();
        const stream = fs.createWriteStream(path.join(OUT_DIR, nomFitxer));
        doc.pipe(stream);

        // Capçalera
        doc.fontSize(16).text(titolInforme, { align: 'center' });
        doc.moveDown();

        // Llista de títols
        titols.forEach((titol, index) => {
            doc.fontSize(10).text(`${index + 1}. ${titol}`);
        });

        doc.end();
        stream.on('finish', resolve);
        stream.on('error', reject);
    });
}

// Consulta 1: Preguntes amb ViewCount > mitjana
async function consulta1(collection) {
    console.log('\n--- Consulta 1: ViewCount superior a la mitjana ---');

    // Calcular la mitjana de ViewCount (convertint a número)
    const pipeline = [
        {
            $group: {
                _id: null,
                mitjana: { $avg: { $toInt: '$question.ViewCount' } }
            }
        }
    ];
    const resultatAvg = await collection.aggregate(pipeline).toArray();
    const mitjana = resultatAvg[0]?.mitjana || 0;
    console.log(`Mitjana de ViewCount: ${mitjana.toFixed(2)}`);

    // Cercar preguntes amb ViewCount > mitjana
    const query = {
        $expr: { $gt: [{ $toInt: '$question.ViewCount' }, mitjana] }
    };
    const resultats = await collection.find(query).toArray();
    console.log(`Nombre de resultats: ${resultats.length}`);

    // Extreure els títols per al PDF
    const titols = resultats.map(doc => doc.question.Title || '(Sense títol)');
    await generarPDF(titols, 'informe1.pdf', 'Preguntes amb ViewCount superior a la mitjana');
    console.log('PDF generat: data/out/informe1.pdf');

    return resultats;
}

// Consulta 2: Preguntes que contenen alguna paraula al títol
async function consulta2(collection) {
    console.log('\n--- Consulta 2: Paraules al títol ---');

    // Crear una expressió regular amb totes les paraules (case-insensitive)
    const regex = new RegExp(PARAULES.join('|'), 'i');
    const query = { 'question.Title': { $regex: regex } };
    const resultats = await collection.find(query).toArray();
    console.log(`Nombre de resultats: ${resultats.length}`);

    // Extreure els títols per al PDF
    const titols = resultats.map(doc => doc.question.Title || '(Sense títol)');
    await generarPDF(titols, 'informe2.pdf', 'Preguntes amb paraules clau al títol');
    console.log('PDF generat: data/out/informe2.pdf');

    return resultats;
}

// Funció principal
async function main() {
    let client;
    try {
        client = await connectDB();
        const db = client.db(DB_NAME);
        const collection = db.collection(COLLECTION_NAME);

        // Executar consultes
        await consulta1(collection);
        await consulta2(collection);

        console.log('\nExercici 2 completat correctament.');
    } catch (error) {
        console.error('Error:', error.message);
    } finally {
        if (client) await client.close();
        console.log('Connexió tancada.');
    }
}

// Executar
main();