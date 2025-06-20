// src/services/usuarios.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'usuarios';

/**
 * Obtiene todos los usuarios con paginación y búsqueda opcional.
 * GET /api/usuarios
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @param {string} [searchTerm] - Término de búsqueda opcional.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de usuarios.
 */
export const getAllUsuarios = async (page = 1, limit = 10, searchTerm = '') => {
  const params = { page, limit, searchTerm };
  return AxiosRequest(`${RESOURCE}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene un usuario por su ID.
 * GET /api/usuarios/:id
 * @param {string} id - ID del usuario.
 * @returns {Promise<Object>} Una promesa con los detalles del usuario.
 */
export const getUsuarioById = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('R'));
};

/**
 * Crea un nuevo usuario.
 * POST /api/usuarios
 * @param {Object} payload - Datos del nuevo usuario, incluyendo la contraseña.
 * @returns {Promise<Object>} Una promesa con el usuario creado.
 */
export const createUsuario = async (payload) => {
  return AxiosRequest(`${RESOURCE}`, mapMethod('C'), payload);
};

/**
 * Actualiza un usuario existente (sin contraseña).
 * PUT /api/usuarios/:id
 * @param {string} id - ID del usuario a actualizar.
 * @param {Object} payload - Datos actualizados del usuario (sin el campo de contraseña).
 * @returns {Promise<Object>} Una promesa con el usuario actualizado.
 */
export const updateUsuario = async (id, payload) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('U'), payload);
};

/**
 * Elimina lógicamente un usuario por su ID.
 * DELETE /api/usuarios/soft-delete/:id
 * @param {string} id - ID del usuario a eliminar lógicamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const softDeleteUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/soft-delete/${id}`, mapMethod('D'));
};

/**
 * Elimina definitivamente un usuario por su ID.
 * DELETE /api/usuarios/:id
 * @param {string} id - ID del usuario a eliminar definitivamente.
 * @returns {Promise<void>} Una promesa que se resuelve si la eliminación es exitosa.
 */
export const deleteUsuario = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}`, mapMethod('D'));
};

/**
 * Obtiene todos los usuarios en formato simple (ID, nombres, apellidos y email).
 * GET /api/usuarios/select
 * @returns {Promise<Array<Object>>} Una promesa con una lista de objetos simples.
 */
export const getAllUsuariosForSelect = async () => {
  return AxiosRequest(`${RESOURCE}/select`, mapMethod('R'));
};

/**
 * Obtiene usuarios filtrados por estado.
 * GET /api/usuarios/estado/:estado
 * @param {string} estado - Estado del usuario ('activo', 'inactivo', 'bloqueado').
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de usuarios.
 */
export const getUsuariosByEstado = async (estado, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/estado/${estado}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene usuarios filtrados por si tienen 2FA activo.
 * GET /api/usuarios/dos-factor-activo/:dosFactorActivo
 * @param {boolean} dosFactorActivo - Booleano para filtrar por 2FA activo (true) o inactivo (false).
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de usuarios.
 */
export const getUsuariosByDosFactorActivo = async (dosFactorActivo, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/dos-factor-activo/${dosFactorActivo}`, mapMethod('R'), {}, params);
};

/**
 * Obtiene usuarios que requieren cambio de contraseña.
 * GET /api/usuarios/requiere-cambio-contrasena/:requiereCambioContrasena
 * @param {boolean} requiereCambioContrasena - Booleano para filtrar por si requieren cambio de contraseña (true) o no (false).
 * @param {number} page - Número de página (inicia en 1).
 * @param {number} limit - Cantidad de elementos por página.
 * @returns {Promise<Object>} Una promesa con la respuesta paginada de usuarios.
 */
export const getUsuariosByRequiereCambioContrasena = async (requiereCambioContrasena, page = 1, limit = 10) => {
  const params = { page, limit };
  return AxiosRequest(`${RESOURCE}/requiere-cambio-contrasena/${requiereCambioContrasena}`, mapMethod('R'), {}, params);
};

/**
 * Verifica si la sesión de un usuario está bloqueada.
 * GET /api/usuarios/:id/is-session-blocked
 * @param {string} id - ID del usuario a verificar.
 * @returns {Promise<boolean>} Una promesa con un booleano indicando si la sesión está bloqueada.
 */
export const isSessionBlocked = async (id) => {
  return AxiosRequest(`${RESOURCE}/${id}/is-session-blocked`, mapMethod('R'));
};
