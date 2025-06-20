// src/services/auditoria.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'auditoria-accesos';

/**
 * Obtiene todos los registros de auditoría con paginación y búsqueda opcional.
 * GET /api/auditoria-accesos
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de registros de auditoría.
 */
export const getAllAuditoriaAccesos = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene un registro de auditoría por su ID compuesto (UUID y Fecha).
 * GET /api/auditoria-accesos/:uuidId/fecha/:fecha
 * @param {string} uuidId - UUID del registro de auditoría.
 * @param {string} fecha - Fecha del registro en formato ISO 8601 (ej. "2024-06-15T14:30:00Z").
 * @returns {Promise<Object>} Una promesa con los detalles del registro de auditoría.
 */
export const getAuditoriaAccesoByIdAndFecha = async (uuidId, fecha) => {
  // Asegurarse de que la fecha se envíe correctamente en la URL
  // Dependiendo de cómo Spring Boot lo espera, podría ser necesario codificar el URI.
  // Axios lo maneja bien para parámetros de ruta.
  return AxiosRequest(`${RESOURCE}/${uuidId}/fecha/${fecha}`, mapMethod('R'));
};

/**
 * Obtiene registros de auditoría filtrados por el ID de una aplicación.
 * GET /api/auditoria-accesos/by-aplicacion/:aplicacionId
 * @param {string} aplicacionId - ID de la aplicación para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de registros de auditoría.
 */
export const getAuditoriaAccesosByAplicacionId = async (aplicacionId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-aplicacion/${aplicacionId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene registros de auditoría filtrados por el ID de una acción.
 * GET /api/auditoria-accesos/by-accion/:accionId
 * @param {string} accionId - ID de la acción para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de registros de auditoría.
 */
export const getAuditoriaAccesosByAccionId = async (accionId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-accion/${accionId}`, mapMethod('R'), {}, params);
};
