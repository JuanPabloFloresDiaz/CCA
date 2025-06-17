"use client"

import { Outlet, Link, useRouter } from "@tanstack/react-router"
import {
  Box,
  CssBaseline,
  Drawer,
  Toolbar,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  ListItemIcon,
  AppBar,
  Typography,
  Avatar,
  Button,
} from "@mui/material"
import {
  Dashboard,
  Category,
  Apps,
  PlayArrow,
  Group,
  Security,
  People,
  History,
  Assessment,
  Email,
  BarChart,
  Analytics,
  Logout,
} from "@mui/icons-material"

const drawerWidth = 280

const menuItems = [
  { label: "Dashboard", to: "/dashboard", icon: Dashboard },
  { label: "Secciones", to: "/secciones", icon: Category },
  { label: "Aplicaciones", to: "/aplicaciones", icon: Apps },
  { label: "Acciones", to: "/acciones", icon: PlayArrow },
  { label: "Tipos de Usuarios", to: "/tipos-usuarios", icon: Group },
  { label: "Permisos", to: "/permisos", icon: Security },
  { label: "Usuarios", to: "/usuarios", icon: People },
  { label: "Sesiones", to: "/sesiones", icon: History },
  { label: "Auditoría", to: "/auditoria", icon: Assessment },
  { label: "Enviar Correos", to: "/enviar-correos", icon: Email },
  { label: "Reportes", to: "/reportes", icon: BarChart },
  { label: "Gráficos", to: "/graficos", icon: Analytics }
]

export default function MenuLayout() {
  const router = useRouter()
  const pathname = router.state.location.pathname

  const handleLogout = () => {
    // Aquí irá la lógica de logout
    console.log("Cerrando sesión...")
  }

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />

      {/* AppBar */}
      <AppBar
        position="fixed"
        sx={{
          width: `calc(100% - ${drawerWidth}px)`,
          ml: `${drawerWidth}px`,
          bgcolor: "#006064", 
          boxShadow: "0 2px 10px rgba(0, 96, 100, 0.2)",
        }}
      >
        <Toolbar>
          <Typography
            variant="h6"
            noWrap
            component="div"
            sx={{
              fontWeight: 600,
              color: "white",
              letterSpacing: "0.5px",
            }}
          >
            Panel de Administración
          </Typography>
        </Toolbar>
      </AppBar>

      {/* Drawer */}
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: "border-box",
            background: "linear-gradient(180deg, #004d50 0%, #00695c 100%)",
            color: "white",
            borderRight: "none",
          },
        }}
      >
        {/* Header del Drawer con Logo */}
        <Box
          sx={{
            p: 3,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            background: "rgba(0, 0, 0, 0.1)",
            borderBottom: "1px solid rgba(255, 255, 255, 0.1)",
          }}
        >
          <Avatar
            src="/assets/react.svg"
            sx={{
              width: 60,
              height: 60,
              mb: 2,
              bgcolor: "rgba(255, 255, 255, 0.1)",
              border: "2px solid rgba(255, 255, 255, 0.2)",
            }}
          />
          <Typography
            variant="h6"
            sx={{
              fontWeight: 700,
              textAlign: "center",
              color: "white",
              lineHeight: 1.2,
              fontSize: "1.1rem",
            }}
          >
            Centro de Control
          </Typography>
          <Typography
            variant="body2"
            sx={{
              textAlign: "center",
              color: "rgba(255, 255, 255, 0.7)",
              fontSize: "0.85rem",
            }}
          >
            de Accesos
          </Typography>
        </Box>

        {/* Lista de Menú */}
        <Box sx={{ overflow: "auto", flex: 1, py: 1 }}>
          <List sx={{ px: 1 }}>
            {menuItems.map(({ label, to, icon: Icon }) => (
              <ListItem key={to} disablePadding sx={{ mb: 0.5 }}>
                <ListItemButton
                  component={Link}
                  to={to}
                  selected={pathname === to}
                  sx={{
                    borderRadius: 2,
                    mx: 1,
                    transition: "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)",
                    color: "white",
                    "&:hover": {
                      bgcolor: "rgba(255, 255, 255, 0.1)",
                      transform: "translateX(8px)",
                      boxShadow: "0 4px 12px rgba(0, 0, 0, 0.2)",
                    },
                    "&.Mui-selected": {
                      bgcolor: "rgba(255, 255, 255, 0.15)",
                      transform: "translateX(8px)",
                      boxShadow: "0 4px 12px rgba(0, 0, 0, 0.3)",
                      "&:hover": {
                        bgcolor: "rgba(255, 255, 255, 0.2)",
                      },
                      "&::before": {
                        content: '""',
                        position: "absolute",
                        left: 0,
                        top: "50%",
                        transform: "translateY(-50%)",
                        width: 4,
                        height: "60%",
                        bgcolor: "#00e5ff", 
                        borderRadius: "0 2px 2px 0",
                      },
                    },
                    position: "relative",
                    overflow: "hidden",
                    "&::after": {
                      content: '""',
                      position: "absolute",
                      top: 0,
                      left: "-100%",
                      width: "100%",
                      height: "100%",
                      background: "linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent)",
                      transition: "left 0.5s",
                    },
                    "&:hover::after": {
                      left: "100%",
                    },
                  }}
                >
                  <ListItemIcon
                    sx={{
                      color: "inherit",
                      minWidth: 40,
                      transition: "transform 0.2s ease-in-out",
                      ".MuiListItemButton-root:hover &": {
                        transform: "scale(1.1)",
                      },
                    }}
                  >
                    <Icon />
                  </ListItemIcon>
                  <ListItemText
                    primary={label}
                    primaryTypographyProps={{
                      fontWeight: pathname === to ? 600 : 500,
                      fontSize: "0.95rem",
                    }}
                  />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>

        {/* Botón de Cerrar Sesión */}
        <Box sx={{ p: 2, borderTop: "1px solid rgba(255, 255, 255, 0.1)" }}>
          <Button
            fullWidth
            variant="contained"
            startIcon={<Logout />}
            onClick={handleLogout}
            sx={{
              bgcolor: "#d32f2f",
              color: "white",
              borderRadius: 2,
              py: 1.2,
              fontWeight: 600,
              textTransform: "none",
              boxShadow: "0 4px 12px rgba(211, 47, 47, 0.3)",
              transition: "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)",
              "&:hover": {
                bgcolor: "#c62828",
                transform: "translateY(-2px)",
                boxShadow: "0 6px 16px rgba(211, 47, 47, 0.4)",
              },
              "&:active": {
                transform: "translateY(0)",
              },
            }}
          >
            Cerrar Sesión
          </Button>
        </Box>
      </Drawer>

      {/* Contenido Principal */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          bgcolor: "#fafafa",
          minHeight: "100vh",
        }}
      >
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  )
}
