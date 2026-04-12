# Exercici 1 (2.5 punts)

L’objectiu de l’exercici és familiaritzar-te amb xat-api. Respon la les preguntes dins el requadre que trobaràs al final de l’exercici.

## Configuració i Estructura Bàsica:

1. Per què és important organitzar el codi en una estructura de directoris com controllers/, routes/, models/, etc.? Quins avantatges ofereix aquesta organització?

Resposta: Manté el projecte ordenat, facilita manteniment, proves i reutilització; separa responsabilitats i fa més fàcil trobar i modificar funcionalitats.

2. Analitzant el fitxer server.js, quina és la seqüència correcta per inicialitzar una aplicació Express? Per què és important l'ordre dels middlewares?

Resposta: Importar dependències → carregar `.env` → crear l'app → configurar middlewares (body parser, CORS, etc.) → registrar rutes → middleware d'errors → `app.listen`. L'ordre és important perquè els middlewares processen la petició en seqüència i poden modificar-la.

3. Com gestiona el projecte les variables d'entorn? Quins avantatges ofereix usar dotenv respecte a hardcodejar els valors?

Resposta: Utilitza fitxers `.env` amb `dotenv`. Això evita hardcodejar secrets, facilita tenir configuracions separades per entorn i millora la seguretat i portabilitat.

## API REST i Express:

4. Observant chatRoutes.js, com s'implementa el routing en Express? Quina és la diferència entre els mètodes HTTP GET i POST i quan s'hauria d'usar cadascun?

Resposta: S'usa un `Router` d'Express amb `router.get`, `router.post`, etc. `GET` s'ha d'utilitzar per recuperar dades sense modificar l'estat; `POST` per crear o enviar dades que modifiquin l'estat del servidor.

5. En el fitxer chatController.js, per què és important separar la lògica del controlador de les rutes? Quins principis de disseny s'apliquen?

Resposta: Per aplicar el principi de responsabilitat única: les rutes defineixen endpoints i els controladors contenen la lògica. Això millora la testabilitat, la reutilització i la llegibilitat.

6. Com gestiona el projecte els errors HTTP? Analitza el middleware errorHandler.js i explica com centralitza la gestió d'errors.

Resposta: Hi ha un middleware central (`errorHandler.js`) que captura errors, assigna codi HTTP i missatge, fa logging i retorna una resposta JSON uniforme, centralitzant la gestió d'errors.

## Documentació amb Swagger:

7. Observant la configuració de Swagger a swagger.js i els comentaris a chatRoutes.js, com s'integra la documentació amb el codi? Quins beneficis aporta aquesta aproximació?

Resposta: Swagger s'integra mitjançant configuració i anotacions als endpoints perquè la documentació es generi automàticament a partir del codi. Això manté la documentació sincronitzada amb la implementació i facilita l'adopció per altres desenvolupadors.

8. Com es documenten els diferents endpoints amb els decoradors de Swagger? Per què és important documentar els paràmetres d'entrada i sortida?

Resposta: Es fan servir anotacions/comentaris Swagger (descripció, paràmetres, esquemes d'entrada i sortida). Documentar-ho és clau per entendre què cal enviar i què esperar, evitant malentesos.

9. Com podem provar els endpoints directament des de la interfície de Swagger? Quins avantatges ofereix això durant el desenvolupament?

Resposta: Swagger UI permet executar peticions des del navegador, enviar cossos d'exemple i veure respostes; això accelera proves i depuració sense necessitat d'eines addicionals.

## Base de Dades i Models:

10. Analitzant els models Conversation.js i Prompt.js, com s'implementen les relacions entre models utilitzant Sequelize? Per què s'utilitza UUID com a clau primària?

Resposta: Sequelize defineix associacions amb `hasMany` i `belongsTo` per modelar relacions entre taules. S'utilitza UUID com a PK per garantir identificadors únics i evitar col·lisions, especialment útil en entorns distribuïts.

11. Com gestiona el projecte les migracions i sincronització de la base de dades? Quins riscos té usar sync() en producció?

Resposta: És recomanable utilitzar migracions (Sequelize CLI) per canvis controlats i reversibles. `sync()` pot recrear o modificar taules automàticament i això pot provocar pèrdues de dades en producció.

12. Quins avantatges ofereix usar un ORM com Sequelize respecte a fer consultes SQL directes?

Resposta: ORM aporta abstracció sobre la base de dades, validacions al model, prevenció d'injeccions SQL i facilita la portabilitat entre SGBD; el codi sol ser més net i fàcil de mantenir.

## Logging i Monitorització:

13. Observant logger.js, com s'implementa el logging estructurat? Quins nivells de logging existeixen i quan s'hauria d'usar cadascun?

Resposta: S'utilitza un logger (p.ex. Winston) que fa logging estructurat amb nivells `error`, `warn`, `info` i `debug`. `error` per fallades crítiques, `warn` per advertències, `info` per esdeveniments normals i `debug` per detalls durant desenvolupament.

14. Per què és important tenir diferents transports de logging (consola, fitxer)? Com es configuren en el projecte?

Resposta: Tenir diferents transports permet veure logs en temps real (consola) i conservar històrics o auditories (fitxer). Es configuren a `logger.js` definint transports i formats (timestamps, JSON, etc.).

15. Com ajuda el logging a debugar problemes en producció? Quina informació crítica s'hauria de loguejar?

Resposta: El logging ajuda a identificar i correlacionar errors i incidents; cal loguejar timestamps, request id, endpoint, paràmetres clau, errors i stack traces per diagnosticar incidents.

---

## Respostes Exercici 1 — Respostes breus ✅

### Configuració i estructura bàsica 🔧

1. Manté el projecte ordenat, facilita manteniment, tests i reutilització, i separa responsabilitats.
2. Importar dependències → carregar `.env` → crear l'app → registrar middlewares (body-parser, CORS) → registrar rutes → middleware d'errors → `app.listen`. L'ordre importa perquè els middlewares processen la petició en seqüència.
3. S'usen variables d'entorn amb `dotenv`. Evita hardcodejar secrets i facilita configuracions per entorn.

### API REST i Express 📡

4. S'usa `Router` d'Express (`router.get`, `router.post`, ...). `GET` per llegir dades, `POST` per crear o enviar dades que modifiquen l'estat.
5. Separar controladors de rutes aplica el principi de responsabilitat única: millora testabilitat i reutilització.
6. Hi ha un middleware central (`errorHandler.js`) que captura errors, assigne codi HTTP, fa logging i retorna una resposta JSON uniforme.

### Documentació amb Swagger 📚

7. Swagger s'integra amb la configuració i anotacions als endpoints per generar doc automàticament; manté la doc sincronitzada.
8. Es documenten amb comentaris/annotations (descripció, paràmetres, esquemes). Documentar evita errors i facilita usabilitat.
9. Swagger UI permet provar peticions des del navegador, accelerant el desenvolupament i el debugging.

### Base de dades i models 🗄️

10. Sequelize fa relacions amb `hasMany`/`belongsTo`; s'usa UUID per claus úniques i fiables en entorns distribuïts.
11. Migracions s'haurien de gestionar amb migracions (CLI). `sync()` pot alterar o recrear taules i provocar pèrdues en producció.
12. ORM aporta abstracció, validacions, prevenció d'injeccions i codi més net respecte a SQL directe.

### Logging i monitorització 📈

13. Logging estructurat (p.ex. Winston) amb nivells `error`, `warn`, `info`, `debug`; s'usen segons severitat.
14. Transports (consola per dev, fitxer per històrics) es configuren a `logger.js`.
15. En producció cal loguejar timestamps, request id, endpoint, paràmetres clau, errors i stack traces per diagnosticar incidents.

---

# Exercici 2 (2.5 punts)

Dins de practica-codi trobaràs src/exercici2.js

1. Modifica el codi per tal que, pels dos primers jocs i les 2 primeres reviews de cada joc, crei una estadística que indiqui el nombre de reviews positives, negatives o neutres.
2. Modifica el prompt si cal.
3. Guarda la sortida en el directori data amb el nom exercici2_resposta.json

## Exemple de sortida

```json
{
  "timestamp": "2025-01-09T12:30:45.678Z",
  "games": [
    {
      "appid": "730",
      "name": "Counter-Strike 2",
      "statistics": {
        "positive": 1,
        "negative": 0,
        "neutral": 1,
        "error": 0
      }
    },
    {
      "appid": "570",
      "name": "Dota 2",
      "statistics": {
        "positive": 1,
        "negative": 1,
        "neutral": 0,
        "error": 0
      }
    }
  ]
}
```

# Exercici 3 (2.5 punts)

Dins de `practica-codi` trobaràs `src/exercici3.js`.

Modifica el codi per tal que retorni un anàlisi detallat sobre l’animal.
Modifica el prompt si cal.

La informació que volem obtenir és:

- Nom de l’animal.
- Classificació taxonòmica (mamífer, au, rèptil, etc.)
- Hàbitat natural
- Dieta
- Característiques físiques (mida, color, trets distintius)
- Estat de conservació

Guarda la sortida en el directori `data` amb el nom `exercici3_resposta.json`.

```json
{
    "analisis": [
        {
            "imatge": {
                "nom_fitxer": "nom_del_fitxer.jpg"
            },
            "analisi": {
                "nom_comu": "nom comú de l'animal",
                "nom_cientific": "nom científic si és conegut",
                "taxonomia": {
                    "classe": "mamífer/au/rèptil/amfibi/peix",
                    "ordre": "ordre taxonòmic",
                    "familia": "família taxonòmica"
                },
                "habitat": {
                    "tipus": ["tipus d'hàbitats"],
                    "regioGeografica": ["regions on viu"],
                    "clima": ["tipus de climes"]
                },
                "dieta": {
                    "tipus": "carnívor/herbívor/omnívor",
                    "aliments_principals": ["llista d'aliments"]
                },
                "caracteristiques_fisiques": {
                    "mida": {
                        "altura_mitjana_cm": "altura mitjana",
                        "pes_mitja_kg": "pes mitjà"
                    },
                    "colors_predominants": ["colors"],
                    "trets_distintius": ["característiques"]
                },
                "estat_conservacio": {
                    "classificacio_IUCN": "estat",
                    "amenaces_principals": ["amenaces"]
                }
            }
        }
    ]
} 
```
# Exercici 4 (2.5 punts)

**Implementa un nou endpoint a xat-api per realitzar anàlisi de sentiment**

Haurà de complir els següents requisits:

- Estar disponible a l’endpoint `POST /api/chat/sentiment-analysis`
- Disposar de documentació swagger
- Emmagatzemar informació a la base de dades
- Usar el logger a fitxer