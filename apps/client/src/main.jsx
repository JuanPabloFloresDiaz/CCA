import React, { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {
  createRouter,
  createRootRoute,
  createRoute,
  RouterProvider,
  Outlet,
} from '@tanstack/react-router'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

// Pages
import Login from './pages/Login.jsx'
import CambiarClave from './pages/CambiarClave.jsx'
import Dashboard from './pages/Dashboard.jsx'
import Secciones from './pages/Secciones.jsx'
import Aplicaciones from './pages/Aplicaciones.jsx'
import Acciones from './pages/Acciones.jsx'
import TiposUsuarios from './pages/TiposUsuarios.jsx'
import Permisos from './pages/Permisos.jsx'
import Usuarios from './pages/Usuarios.jsx'
import Sesiones from './pages/Sesiones.jsx'
import Auditoria from './pages/Auditoria.jsx'
import EnviarCorreos from './pages/EnviarCorreos.jsx'
import Reportes from './pages/Reportes.jsx'
import Graficos from './pages/Graficos.jsx'

// CSS global
import './index.css'
import './App.css'

// Query client
const queryClient = new QueryClient()

// Root route
const rootRoute = createRootRoute({
  component: () => (
    <>
      <Outlet />
    </>
  ),
})

// Definimos todas las rutas hijas
const routeTree = rootRoute.addChildren([
  createRoute({ getParentRoute: () => rootRoute, path: '/', component: Login }),
  createRoute({ getParentRoute: () => rootRoute, path: '/cambiar-clave', component: CambiarClave }),
  createRoute({ getParentRoute: () => rootRoute, path: '/dashboard', component: Dashboard }),
  createRoute({ getParentRoute: () => rootRoute, path: '/secciones', component: Secciones }),
  createRoute({ getParentRoute: () => rootRoute, path: '/aplicaciones', component: Aplicaciones }),
  createRoute({ getParentRoute: () => rootRoute, path: '/acciones', component: Acciones }),
  createRoute({ getParentRoute: () => rootRoute, path: '/tipos-usuarios', component: TiposUsuarios }),
  createRoute({ getParentRoute: () => rootRoute, path: '/permisos', component: Permisos }),
  createRoute({ getParentRoute: () => rootRoute, path: '/usuarios', component: Usuarios }),
  createRoute({ getParentRoute: () => rootRoute, path: '/sesiones', component: Sesiones }),
  createRoute({ getParentRoute: () => rootRoute, path: '/auditoria', component: Auditoria }),
  createRoute({ getParentRoute: () => rootRoute, path: '/enviar-correos', component: EnviarCorreos }),
  createRoute({ getParentRoute: () => rootRoute, path: '/reportes', component: Reportes }),
  createRoute({ getParentRoute: () => rootRoute, path: '/graficos', component: Graficos }),
])

const router = createRouter({ routeTree })

// Render
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </StrictMode>
)
