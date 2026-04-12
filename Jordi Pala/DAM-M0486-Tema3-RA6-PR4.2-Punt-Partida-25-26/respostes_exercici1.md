## Configuració i estructura bàsica 🔧

1. **Per què organitzar el codi en `controllers/`, `routes/`, `models/`, etc.?**

   Manté el projecte ordenat, facilita manteniment, proves i reutilització; separa responsabilitats i fa més fàcil localitzar i modificar funcionalitats.

2. **Seqüència correcta per inicialitzar Express i per què importa l'ordre dels middlewares?**

   Importar dependències → carregar `.env` → crear l'app → configurar middlewares (body-parser, CORS, etc.) → registrar rutes → middleware d'errors → `app.listen`. L'ordre importa perquè els middlewares processen la petició en seqüència i influencien el comportament.

3. **Com gestiona el projecte les variables d'entorn i avantatges de `dotenv`?**

   Utilitza `.env` i `dotenv` per carregar configuracions. Evita hardcodejar secrets, permet configurar entorns diferents i millora seguretat i portabilitat.

---

## API REST i Express 📡

4. **Com s'implementa el routing i diferència entre `GET` i `POST`?**

   S'usa `Router` d'Express (`router.get`, `router.post`, ...). `GET` per recuperar dades sense canviar estat; `POST` per crear o modificar recursos.

5. **Per què separar controlador i rutes (`chatController.js`)?**

   Aplica el principi de responsabilitat única: les rutes defineixen endpoints i els controladors contenen la lògica. Millora testabilitat i reutilització.

6. **Com gestiona el projecte els errors HTTP (`errorHandler.js`)?**

   Té un middleware centralitzat que captura errors, assigna codi HTTP i missatge, fa logging i retorna una resposta JSON uniforme.

---

## Documentació amb Swagger 📚

7. **Com s'integra Swagger amb el codi i beneficis?**

   Swagger es configura amb `swagger.js` i anotacions als rutes per generar la documentació automàticament. Manté la doc sincronitzada amb el codi i facilita ús per tercers.

8. **Com es documenten els endpoints i per què documentar params i respostes?**

   Amb comentaris/annotations Swagger (descripció, paràmetres, esquemes). Documentar evita malentesos i ajuda a consumir l'API correctament.

9. **Com provar endpoints des de Swagger UI i avantatges?**

   Swagger UI permet executar peticions des del navegador amb inputs d'exemple i veure respostes immediatament; accelera proves i debug sense eines externes.

---

## Base de Dades i Models 🗄️

10. **Com s'implementen relacions (Conversation, Prompt) amb Sequelize i per què usar UUID?**

    Sequelize defineix associacions (`hasMany`, `belongsTo`) per modelar relacions. UUID s'usa per garantir identificadors únics i evitar col·lisions, útil en entorns distribuïts.

11. **Com gestiona migracions i riscos d'usar `sync()` en producció?**

    És recomanable usar migracions (Sequelize CLI) per canvis controlats. `sync()` pot recrear o alterar taules i causar pèrdues de dades si s'usa imprudentment en producció.

12. **Avantatges d'un ORM com Sequelize vs SQL directe?**

    Abstracció, validacions a nivell de model, prevenció d'injeccions, portabilitat entre SGBD i codi més net i mantenible.

---

## Logging i monitorització 📈

13. **Com s'implementa el logging estructurat i quins nivells existeixen?**

    S'utilitza un logger (p.ex. Winston) amb logging estructurat i nivells `error`, `warn`, `info`, `debug`. S'usa cada nivell segons severitat i necessitat d'informació.

14. **Per què diferents transports (consola, fitxer) i com es configuren?**

    Consola per desenvolupament i fitxers per historial/auditories. Es configuren a `logger.js` definint transports i formats (timestamp, JSON).

15. **Com ajuda el logging en producció i què cal loguejar?**

    Permet rastrejar i diagnosticar incidents. Cal loguejar timestamps, request id, endpoint, paràmetres crítics, errors i stack traces.

