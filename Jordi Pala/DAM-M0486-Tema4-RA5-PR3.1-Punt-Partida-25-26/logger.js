cat > logger.js << 'EOF'
/**
 * Logger simple para la práctica
 * Escribe en archivo y muestra por consola
 */

const fs = require('fs');
const path = require('path');

class Logger {
  constructor(logFile) {
    this.logFile = logFile;
    // Asegurar que el directorio existe
    const dir = path.dirname(logFile);
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
    // Limpiar archivo de log anterior
    fs.writeFileSync(logFile, '');
  }

  info(message) {
    this._log('INFO', message);
  }

  error(message) {
    this._log('ERROR', message);
  }

  warn(message) {
    this._log('WARN', message);
  }

  success(message) {
    this._log('SUCCESS', message);
  }

  _log(level, message) {
    const timestamp = new Date().toISOString().replace('T', ' ').substring(0, 19);
    const logEntry = `${timestamp} [${level}]: ${message}\n`;
    
    // Mostrar por consola con colores
    const colors = {
      INFO: '\x1b[34m',   // Azul
      ERROR: '\x1b[31m',  // Rojo
      WARN: '\x1b[33m',   // Amarillo
      SUCCESS: '\x1b[32m' // Verde
    };
    
    console.log(`${colors[level] || ''}${message}\x1b[0m`);
    
    // Guardar en archivo (sin colores)
    fs.appendFileSync(this.logFile, logEntry);
  }
}

// Crear instancia del logger
const logger = new Logger(path.join(__dirname, 'data/logs/exercici1.log'));

module.exports = logger;
EOF