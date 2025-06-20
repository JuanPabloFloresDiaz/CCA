// src/services/usuariosTipoUsuario.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'usuarios-tipos-usuario';

/**
 * Obtiene todas las relaciones Usuario-TipoUsuario con paginación y búsqueda opcional.
 * GET /api/usuarios-tipos-usuario
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de relaciones.
 */
export const getAllUsuariosTipoUsuario = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene una relación Usuario-TipoUsuario por su ID.
 * GET /api/usuarios-tipos-usuario/:id
 * @param {string} id - ID de la relación.
 * @returns {Promise<Object>} Una promesa con los detalles de la relación.
 */
export const getUsuariosTipoUsuarioById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea una nueva relación Usuario-TipoUsuario.
 * POST /api/usuarios-tipos-usuario
 * @param {Object} payload - Datos de la nueva relación.
 * @param {string} payload.usuarioId - ID del usuario.
 * @param {string} payload.tipoUsuarioId - ID del tipo de usuario.
 * @returns {Promise<Object>} Una promesa con la relación creada.
 */
export const createUsuariosTipoUsuario = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza una relación Usuario-TipoUsuario existente.
 * PUT /api/usuarios-tipos-usuario/:id
 * @param {string} id - ID de la relación a actualizar.
 * @param {Object} payload - Datos actualizados de la relación.
 * @param {string} payload.usuarioId - ID del usuario.
 * @param {string} payload.tipoUsuarioId - ID del tipo de usuario.
 * @returns {Promise<Object>} Una promesa con la relación actualizada.
 */
export const updateUsuariosTipoUsuario = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente una relación Usuario-TipoUsuario por su ID.
 * DELETE /api/usuarios-tipos-usuario/soft-delete/:id
 * @param {string} id - ID de la relación a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteUsuariosTipoUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente una relación Usuario-TipoUsuario por su ID.
 * DELETE /api/usuarios-tipos-usuario/:id
 * @param {string} id - ID de la relación a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deleteUsuariosTipoUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene relaciones Usuario-TipoUsuario filtradas por el ID de un usuario.
 * GET /api/usuarios-tipos-usuario/by-usuario/:usuarioId
 * @param {string} usuarioId - ID del usuario para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de relaciones.
 */
export const getUsuariosTipoUsuarioByUsuarioId = async (usuarioId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-usuario/${usuarioId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene relaciones Usuario-TipoUsuario filtradas por el ID de un tipo de usuario.
 * GET /api/usuarios-tipos-usuario/by-tipo-usuario/:tipoUsuarioId
 * @param {string} tipoUsuarioId - ID del tipo de usuario para filtrar.
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de relaciones.
 */
export const getUsuariosTipoUsuarioByTipoUsuarioId = async (tipoUsuarioId, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/by-tipo-usuario/${tipoUsuarioId}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene los permisos agrupados por sección para un usuario específico y una aplicación dada.
 * GET /api/usuarios-tipos-usuario/:userId/permissions-by-section/:applicationIdentifier
 * @param {string} userId - ID del usuario.
 * @param {string} applicationIdentifier - Llave identificadora de la aplicación.
 * @returns {Promise<Array<Object>>} Una promesa con una lista de permisos agrupados por sección.
 */
export const getPermissionsForUserAndApplicationGroupedBySection = async (userId, applicationIdentifier) => {
  return AxiosRequest(`${RESOURCE}/${userId}/permissions-by-section/${applicationIdentifier}`, mapMethod('R'));
};
