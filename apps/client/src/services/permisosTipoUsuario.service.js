// src/services/permisosTipoUsuario.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'permisos-tipo-usuario';

/**
 * Obtiene todos los permisos de tipo de usuario con paginación y búsqueda opcional.
 * GET /api/permisos-tipo-usuario
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de permisos.
 */
export const getAllPermisosTipoUsuario = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene un permiso de tipo de usuario por su ID.
 * GET /api/permisos-tipo-usuario/:id
 * @param {string} id - ID del permiso.
 * @returns {Promise<Object>} Una promesa con los detalles del permiso.
 */
export const getPermisoTipoUsuarioById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea un nuevo permiso de tipo de usuario.
 * POST /api/permisos-tipo-usuario
 * @param {Object} payload - Datos del nuevo permiso (usuarioId, tipoUsuarioId, etc.).
 * @param {string} payload.accionId - ID de la acción asociada.
 * @param {string} payload.tipoUsuarioId - ID del tipo de usuario asociado.
 * @returns {Promise<Object>} Una promesa con el permiso creado.
 */
export const createPermisoTipoUsuario = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza un permiso de tipo de usuario existente.
 * PUT /api/permisos-tipo-usuario/:id
 * @param {string} id - ID del permiso a actualizar.
 * @param {Object} payload - Datos actualizados del permiso.
 * @param {string} payload.accionId - ID de la acción asociada.
 * @param {string} payload.tipoUsuarioId - ID del tipo de usuario asociado.
 * @returns {Promise<Object>} Una promesa con el permiso actualizado.
 */
export const updatePermisoTipoUsuario = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente un permiso de tipo de usuario por su ID.
 * DELETE /api/permisos-tipo-usuario/soft-delete/:id
 * @param {string} id - ID del permiso a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeletePermisoTipoUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente un permiso de tipo de usuario por su ID.
 * DELETE /api/permisos-tipo-usuario/:id
 * @param {string} id - ID del permiso a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deletePermisoTipoUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene permisos filtrados por el ID de un tipo de usuario.
 * GET /api/permisos-tipo-usuario/by-tipo-usuario/:tipoUsuarioId
 * @param {string} tipoUsuarioId - ID del tipo de usuario para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de permisos.
 */
export const getPermisosByTipoUsuarioId = async (tipoUsuarioId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-tipo-usuario/${tipoUsuarioId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene permisos filtrados por el ID de una aplicación.
 * GET /api/permisos-tipo-usuario/by-aplicacion/:aplicacionId
 * @param {string} aplicacionId - ID de la aplicación para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de permisos.
 */
export const getPermisosByAplicacionId = async (aplicacionId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-aplicacion/${aplicacionId}`, mapMethod('R'), {}, params);
};
