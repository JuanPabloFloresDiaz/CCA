// src/utils/mapMethod.js

/** Mapeo de CRUD a HTTP methods */
const METHOD_MAP = {
  C: 'POST',
  R: 'GET',
  U: 'PUT',
  D: 'DELETE',
  P: 'PATCH',
}

/**
 * Convierte un código CRUD en verbo HTTP.
 * @param {'C'|'R'|'U'|'D'|'P'} code
 * @returns {string}
 */
export function mapMethod(code) {
  const method = METHOD_MAP[code]
  if (!method) {
    throw new Error(`Método no válido: ${code}`)
  }
  return method
}
