// src/services/aplicaciones.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'aplicaciones';

/**
 * Obtiene todas las aplicaciones con paginación y búsqueda opcional.
 * GET /api/aplicaciones
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de aplicaciones.
 */
export const getAllAplicaciones = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene una aplicación por su ID.
 * GET /api/aplicaciones/:id
 * @param {string} id - ID de la aplicación.
 * @returns {Promise<Object>} Una promesa con los detalles de la aplicación.
 */
export const getAplicacionById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea una nueva aplicación.
 * POST /api/aplicaciones
 * @param {Object} payload - Datos de la nueva aplicación.
 * @param {string} payload.nombre - Nombre de la aplicación.
 * @param {string} [payload.descripcion] - Descripción de la aplicación.
 * @param {string} payload.url - URL de la aplicación.
 * @param {string} payload.llaveIdentificadora - Llave identificadora única de la aplicación.
 * @returns {Promise<Object>} Una promesa con la aplicación creada.
 */
export const createAplicacion = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza una aplicación existente.
 * PUT /api/aplicaciones/:id
 * @param {string} id - ID de la aplicación a actualizar.
 * @param {Object} payload - Datos actualizados de la aplicación.
 * @param {string} payload.nombre - Nombre de la aplicación.
 * @param {string} [payload.descripcion] - Descripción de la aplicación.
 * @param {string} payload.url - URL de la aplicación.
 * @param {string} payload.llaveIdentificadora - Llave identificadora única de la aplicación.
 * @returns {Promise<Object>} Una promesa con la aplicación actualizada.
 */
export const updateAplicacion = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente una aplicación por su ID.
 * DELETE /api/aplicaciones/soft-delete/:id
 * @param {string} id - ID de la aplicación a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteAplicacion = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente una aplicación por su ID.
 * DELETE /api/aplicaciones/:id
 * @param {string} id - ID de la aplicación a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deleteAplicacion = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene aplicaciones filtradas por estado.
 * GET /api/aplicaciones/estado/:estado
 * @param {string} estado - Estado de la aplicación ('activo' o 'inactivo').
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de aplicaciones.
 */
export const getAplicacionesByEstado = async (estado, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/estado/${estado}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene todas las aplicaciones en formato simple (ID y nombre).
 * GET /api/aplicaciones/select
 * @returns {Promise<Array<Object>>} Una promesa con una lista de objetos simples (id, nombre).
 */
export const getAllAplicacionesForSelect = async () => {
  return AxiosRequest(`${RESOURCE}/select`, mapMethod('R'));
};
