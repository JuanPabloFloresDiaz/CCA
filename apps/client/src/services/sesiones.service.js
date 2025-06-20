// src/services/sesiones.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'sesiones';

/**
 * Obtiene todas las sesiones con paginación y búsqueda opcional.
 * GET /api/sesiones
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de sesiones.
 */
export const getAllSesiones = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene una sesión por su ID.
 * GET /api/sesiones/:id
 * @param {string} id - ID de la sesión.
 * @returns {Promise<Object>} Una promesa con los detalles de la sesión.
 */
export const getSesionById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Actualiza el estado de una sesión por su ID.
 * PUT /api/sesiones/:id/status
 * @param {string} id - ID de la sesión a actualizar.
 * @param {string} newStatus - Nuevo estado para la sesión ('activa', 'cerrada', 'expirada').
 * @returns {Promise<Object>} Una promesa con la sesión actualizada.
 */
export const updateSesionStatus = async (id, newStatus) => {
  // Spring Boot espera el newStatus como RequestParam, no en el body.
  // AxiosRequest lo enviaría en el body por defecto para PUT, por eso lo ajustamos para que sea un param.
  const params = { newStatus }; // Se envía como query param
  return AxiosRequest(`${RESOURCE}/${id}/status`, mapMethod('U'), {}, params);
};


/**
 * Elimina lógicamente una sesión por su ID.
 * DELETE /api/sesiones/soft-delete/:id
 * @param {string} id - ID de la sesión a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteSesion = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Obtiene sesiones filtradas por estado.
 * GET /api/sesiones/estado/:estado
 * @param {string} estado - Estado de la sesión ('activa', 'cerrada', 'expirada').
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de sesiones.
 */
export const getSesionesByEstado = async (estado, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/estado/${estado}`, mapMethod('R'), {}, params);
};
