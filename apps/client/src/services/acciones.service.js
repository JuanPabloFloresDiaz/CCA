// src/services/acciones.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'acciones';

/**
 * Obtiene todas las acciones con paginación y búsqueda opcional.
 * GET /api/acciones
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de acciones.
 */
export const getAllAcciones = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene una acción por su ID.
 * GET /api/acciones/:id
 * @param {string} id - ID de la acción.
 * @returns {Promise<Object>} Una promesa con los detalles de la acción.
 */
export const getAccionById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea una nueva acción.
 * POST /api/acciones
 * @param {Object} payload - Datos de la nueva acción.
 * @param {string} payload.nombre - Nombre de la acción.
 * @param {string} [payload.descripcion] - Descripción de la acción.
 * @param {string} payload.aplicacionId - ID de la aplicación a la que pertenece la acción.
 * @param {string} payload.seccionId - ID de la sección a la que pertenece la acción.
 * @returns {Promise<Object>} Una promesa con la acción creada.
 */
export const createAccion = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza una acción existente.
 * PUT /api/acciones/:id
 * @param {string} id - ID de la acción a actualizar.
 * @param {Object} payload - Datos actualizados de la acción.
 * @param {string} payload.nombre - Nombre de la acción.
 * @param {string} [payload.descripcion] - Descripción de la acción.
 * @param {string} payload.aplicacionId - ID de la aplicación a la que pertenece la acción.
 * @param {string} payload.seccionId - ID de la sección a la que pertenece la acción.
 * @returns {Promise<Object>} Una promesa con la acción actualizada.
 */
export const updateAccion = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente una acción por su ID.
 * DELETE /api/acciones/soft-delete/:id
 * @param {string} id - ID de la acción a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteAccion = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente una acción por su ID.
 * DELETE /api/acciones/:id
 * @param {string} id - ID de la acción a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deleteAccion = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene acciones filtradas por el ID de una aplicación.
 * GET /api/acciones/by-aplicacion/:aplicacionId
 * @param {string} aplicacionId - ID de la aplicación para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de acciones.
 */
export const getAccionesByAplicacionId = async (aplicacionId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-aplicacion/${aplicacionId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene acciones filtradas por el ID de una sección.
 * GET /api/acciones/by-seccion/:seccionId
 * @param {string} seccionId - ID de la sección para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de acciones.
 */
export const getAccionesBySeccionId = async (seccionId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-seccion/${seccionId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene todas las acciones en formato simple (ID y nombre).
 * GET /api/acciones/select
 * @returns {Promise<Array<Object>>} Una promesa con una lista de objetos simples (id, nombre).
 */
export const getAllAccionesForSelect = async () => {
  return AxiosRequest(`${RESOURCE}/select`, mapMethod('R'));
};
