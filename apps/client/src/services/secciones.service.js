// src/services/secciones.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'secciones';

/**
 * Obtiene todas las secciones con paginación y búsqueda opcional.
 * GET /api/secciones
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de secciones.
 */
export const getAllSecciones = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene una sección por su ID.
 * GET /api/secciones/:id
 * @param {string} id - ID de la sección.
 * @returns {Promise<Object>} Una promesa con los detalles de la sección.
 */
export const getSeccionById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea una nueva sección.
 * POST /api/secciones
 * @param {Object} payload - Datos de la nueva sección.
 * @param {string} payload.nombre - Nombre de la sección.
 * @param {string} [payload.descripcion] - Descripción de la sección.
 * @returns {Promise<Object>} Una promesa con la sección creada.
 */
export const createSeccion = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza una sección existente.
 * PUT /api/secciones/:id
 * @param {string} id - ID de la sección a actualizar.
 * @param {Object} payload - Datos actualizados de la sección.
 * @param {string} payload.nombre - Nombre de la sección.
 * @param {string} [payload.descripcion] - Descripción de la sección.
 * @returns {Promise<Object>} Una promesa con la sección actualizada.
 */
export const updateSeccion = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente una sección por su ID.
 * DELETE /api/secciones/soft-delete/:id
 * @param {string} id - ID de la sección a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteSeccion = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente una sección por su ID.
 * DELETE /api/secciones/:id
 * @param {string} id - ID de la sección a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deleteSeccion = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene todas las secciones en formato simple (ID y nombre).
 * GET /api/secciones/select
 * @returns {Promise<Array<Object>>} Una promesa con una lista de objetos simples (id, nombre).
 */
export const getAllSeccionesForSelect = async () => {
  return AxiosRequest(`${RESOURCE}/select`, mapMethod('R'));
};
