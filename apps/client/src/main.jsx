import React, { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {
  createRouter,
  createRootRoute,
  createRoute,
  RouterProvider,
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

// Layout
import MenuLayout from './layout/MenuLayout.jsx'

// Estilos globales
import './index.css'
import './App.css'

// Instancia de React Query
const queryClient = new QueryClient()

// Root (sin componente)
const rootRoute = createRootRoute()

// Ruta del login (sin layout)
const authRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/',
  component: Login,
})

// Ruta que envuelve todas las demás (usa MenuLayout)
const appRoute = createRoute({
  getParentRoute: () => rootRoute,
  id: 'app',
  component: MenuLayout,
})

// Rutas protegidas (hijas de appRoute)
const routeTree = rootRoute.addChildren([
  authRoute,
  appRoute.addChildren([
    createRoute({ path: '/cambiar-clave', component: CambiarClave, getParentRoute: () => appRoute }),
    createRoute({ path: '/dashboard', component: Dashboard, getParentRoute: () => appRoute }),
    createRoute({ path: '/secciones', component: Secciones, getParentRoute: () => appRoute }),
    createRoute({ path: '/aplicaciones', component: Aplicaciones, getParentRoute: () => appRoute }),
    createRoute({ path: '/acciones', component: Acciones, getParentRoute: () => appRoute }),
    createRoute({ path: '/tipos-usuarios', component: TiposUsuarios, getParentRoute: () => appRoute }),
    createRoute({ path: '/permisos', component: Permisos, getParentRoute: () => appRoute }),
    createRoute({ path: '/usuarios', component: Usuarios, getParentRoute: () => appRoute }),
    createRoute({ path: '/sesiones', component: Sesiones, getParentRoute: () => appRoute }),
    createRoute({ path: '/auditoria', component: Auditoria, getParentRoute: () => appRoute }),
    createRoute({ path: '/enviar-correos', component: EnviarCorreos, getParentRoute: () => appRoute }),
    createRoute({ path: '/reportes', component: Reportes, getParentRoute: () => appRoute }),
    createRoute({ path: '/graficos', component: Graficos, getParentRoute: () => appRoute }),
  ]),
])

const router = createRouter({ routeTree })

// Renderizado principal
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </StrictMode>
)
