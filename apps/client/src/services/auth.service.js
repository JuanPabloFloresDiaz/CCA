// src/services/auth.service.js

import AxiosRequest from './AxiosRequest';
import { mapMethod } from '../utils/MapMethod';

const RESOURCE = 'auth';

/**
 * Inicia sesión de un usuario.
 * POST /api/auth/login
 * @param {{ email: string, contrasena: string }} payload - Las credenciales del usuario.
 * @returns {Promise<Object>} La respuesta de la API, incluyendo el token JWT.
 */
export const loginUser = async (payload) => {
  // El endpoint es '/api/auth/login'
  // El método es POST (mapMethod('C'))
  // El payload es el objeto con email y contrasena
  return AxiosRequest(`${RESOURCE}/login`, mapMethod('C'), payload);
};

/**
 * Cambia la contraseña de un usuario autenticado.
 * PUT /api/auth/change-password
 * Requiere el token JWT en los encabezados de la solicitud.
 * @param {{ currentPassword: string, newPassword: string }} payload - La contraseña actual y la nueva contraseña.
 * @returns {Promise<void>} Una promesa que se resuelve si el cambio es exitoso.
 */
export const changePassword = async (payload) => {
  // El endpoint es '/api/auth/change-password'
  // El método es PUT (mapMethod('U'))
  // El payload es el objeto con currentPassword y newPassword
  return AxiosRequest(`${RESOURCE}/change-password`, mapMethod('U'), payload);
};

/**
 * Cierra la sesión de un usuario (invalida el token JWT).
 * POST /api/auth/logout
 * Requiere el token JWT en los encabezados de la solicitud.
 * @returns {Promise<void>} Una promesa que se resuelve si el cierre de sesión es exitoso.
 */
export const logoutUser = async () => {
  // El endpoint es '/api/auth/logout'
  // El método es POST (mapMethod('C'))
  // No hay payload para esta operación, así que pasamos un objeto vacío.
  return AxiosRequest(`${RESOURCE}/logout`, mapMethod('C'), {});
};
