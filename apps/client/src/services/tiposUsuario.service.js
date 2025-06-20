// src/services/tiposUsuario.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'tipos-usuario';

/**
 * Obtiene todos los tipos de usuario con paginación y búsqueda opcional.
 * GET /api/tipos-usuario
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de tipos de usuario.
 */
export const getAllTiposUsuario = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene un tipo de usuario por su ID.
 * GET /api/tipos-usuario/:id
 * @param {string} id - ID del tipo de usuario.
 * @returns {Promise<Object>} Una promesa con los detalles del tipo de usuario.
 */
export const getTipoUsuarioById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea un nuevo tipo de usuario.
 * POST /api/tipos-usuario
 * @param {Object} payload - Datos del nuevo tipo de usuario.
 * @param {string} payload.nombre - Nombre del tipo de usuario.
 * @param {string} [payload.descripcion] - Descripción del tipo de usuario.
 * @param {string} payload.aplicacionId - ID de la aplicación asociada.
 * @param {string} payload.estado - Estado del tipo de usuario ('activo', 'inactivo').
 * @returns {Promise<Object>} Una promesa con el tipo de usuario creado.
 */
export const createTipoUsuario = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza un tipo de usuario existente.
 * PUT /api/tipos-usuario/:id
 * @param {string} id - ID del tipo de usuario a actualizar.
 * @param {Object} payload - Datos actualizados del tipo de usuario.
 * @param {string} payload.nombre - Nombre del tipo de usuario.
 * @param {string} [payload.descripcion] - Descripción del tipo de usuario.
 * @param {string} payload.aplicacionId - ID de la aplicación asociada.
 * @param {string} payload.estado - Estado del tipo de usuario ('activo', 'inactivo').
 * @returns {Promise<Object>} Una promesa con el tipo de usuario actualizado.
 */
export const updateTipoUsuario = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente un tipo de usuario por su ID.
 * DELETE /api/tipos-usuario/soft-delete/:id
 * @param {string} id - ID del tipo de usuario a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteTipoUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente un tipo de usuario por su ID.
 * DELETE /api/tipos-usuario/:id
 * @param {string} id - ID del tipo de usuario a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deleteTipoUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene tipos de usuario filtrados por estado.
 * GET /api/tipos-usuario/estado/:estado
 * @param {string} estado - Estado del tipo de usuario ('activo' o 'inactivo').
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de tipos de usuario.
 */
export const getTiposUsuarioByEstado = async (estado, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/estado/${estado}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene tipos de usuario filtrados por aplicación.
 * GET /api/tipos-usuario/by-aplicacion/:aplicacionId
 * @param {string} aplicacionId - ID de la aplicación para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de tipos de usuario.
 */
export const getTiposUsuarioByAplicacionId = async (aplicacionId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-aplicacion/${aplicacionId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene todos los tipos de usuario en formato simple (ID y nombre).
 * GET /api/tipos-usuario/select
 * @returns {Promise<Array<Object>>} Una promesa con una lista de objetos simples (id, nombre).
 */
export const getAllTiposUsuarioForSelect = async () => {
  return AxiosRequest(`${RESOURCE}/select`, mapMethod('R'));
};
