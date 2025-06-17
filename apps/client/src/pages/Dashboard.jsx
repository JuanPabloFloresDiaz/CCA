"use client"

import { useState } from "react"
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  CardActions,
  Button,
  Avatar,
  Chip,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Grid,
  Paper,
  Divider,
} from "@mui/material"
import {
  ExpandMore,
  Launch,
  Person,
  Dashboard as DashboardIcon,
  Security,
  AdminPanelSettings,
  SupervisorAccount,
  TrendingUp,
} from "@mui/icons-material"
import { LineChart, Line, XAxis, YAxis, ResponsiveContainer, Tooltip } from "recharts"

// Datos mock para la plantilla
const mockApplications = [
  {
    id: 1,
    name: "Sistema de Recursos Humanos",
    description: "Gestión integral de empleados, nóminas y procesos de RRHH",
    image: "/placeholder.svg?height=200&width=300",
    url: "https://rrhh.empresa.com",
    userRoles: [
      { name: "Administrador", description: "Acceso completo al sistema" },
      { name: "Supervisor", description: "Gestión de equipos y reportes" },
    ],
  },
  {
    id: 2,
    name: "Portal Financiero",
    description: "Control de presupuestos, gastos y reportes financieros",
    image: "/placeholder.svg?height=200&width=300",
    url: "https://finanzas.empresa.com",
    userRoles: [{ name: "Analista", description: "Consulta y análisis de datos financieros" }],
  },
  {
    id: 3,
    name: "Sistema de Inventarios",
    description: "Gestión de stock, productos y almacenes",
    image: "/placeholder.svg?height=200&width=300",
    url: "https://inventario.empresa.com",
    userRoles: [
      { name: "Operador", description: "Registro de movimientos de inventario" },
      { name: "Supervisor", description: "Supervisión y reportes de inventario" },
    ],
  },
  {
    id: 4,
    name: "CRM Empresarial",
    description: "Gestión de clientes, ventas y oportunidades comerciales",
    image: "/placeholder.svg?height=200&width=300",
    url: "https://crm.empresa.com",
    userRoles: [{ name: "Vendedor", description: "Gestión de clientes y oportunidades" }],
  },
]

const getRoleIcon = (roleName) => {
  switch (roleName.toLowerCase()) {
    case "administrador":
      return <AdminPanelSettings sx={{ color: "#00bcd4" }} />
    case "supervisor":
      return <SupervisorAccount sx={{ color: "#00acc1" }} />
    case "analista":
      return <TrendingUp sx={{ color: "#00838f" }} />
    default:
      return <Person sx={{ color: "#00bcd4" }} />
  }
}

const Dashboard = () => {
  const [expandedCard, setExpandedCard] = useState(null)

  const handleCardExpand = (cardId) => {
    setExpandedCard(expandedCard === cardId ? null : cardId)
  }

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography
          variant="h3"
          component="h1"
          sx={{
            fontWeight: 700,
            color: "#00acc1",
            mb: 1,
            display: "flex",
            alignItems: "center",
            gap: 2,
          }}
        >
          <DashboardIcon sx={{ fontSize: 40 }} />
          Dashboard
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Bienvenido a tu panel de control de accesos
        </Typography>
      </Box>

      <Grid container spacing={4}>
        {/* Sección de aplicaciones */}
        <Grid item xs={12} lg={8}>
          <Paper
            elevation={2}
            sx={{
              p: 3,
              borderRadius: 3,
              background: "linear-gradient(145deg, #ffffff 0%, #f8f9fa 100%)",
            }}
          >
            <Typography
              variant="h5"
              sx={{
                fontWeight: 600,
                color: "#00acc1",
                mb: 3,
                display: "flex",
                alignItems: "center",
                gap: 1,
              }}
            >
              <Security />
              Mis Aplicaciones
            </Typography>

            <Grid container spacing={3}>
              {mockApplications.map((app) => (
                <Grid item xs={12} md={6} key={app.id}>
                  <Card
                    elevation={3}
                    sx={{
                      borderRadius: 2,
                      transition: "all 0.3s ease-in-out",
                      "&:hover": {
                        transform: "translateY(-4px)",
                        boxShadow: "0 8px 25px rgba(0, 188, 212, 0.15)",
                      },
                    }}
                  >
                    <CardContent>
                      <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                        <Avatar
                          src={app.image}
                          sx={{
                            width: 56,
                            height: 56,
                            mr: 2,
                            bgcolor: "#e0f7fa",
                            border: "2px solid #00bcd4",
                          }}
                        >
                          <DashboardIcon sx={{ color: "#00bcd4" }} />
                        </Avatar>
                        <Box sx={{ flex: 1 }}>
                          <Typography variant="h6" sx={{ fontWeight: 600, color: "#00acc1" }}>
                            {app.name}
                          </Typography>
                          <Chip
                            label={`${app.userRoles.length} rol${app.userRoles.length > 1 ? "es" : ""}`}
                            size="small"
                            sx={{
                              bgcolor: "#e0f7fa",
                              color: "#00838f",
                              fontWeight: 500,
                            }}
                          />
                        </Box>
                      </Box>

                      <Typography variant="body2" color="text.secondary" sx={{ mb: 2, lineHeight: 1.6 }}>
                        {app.description}
                      </Typography>

                      <Accordion
                        expanded={expandedCard === app.id}
                        onChange={() => handleCardExpand(app.id)}
                        sx={{
                          boxShadow: "none",
                          "&:before": { display: "none" },
                          bgcolor: "transparent",
                        }}
                      >
                        <AccordionSummary
                          expandIcon={<ExpandMore sx={{ color: "#00bcd4" }} />}
                          sx={{
                            px: 0,
                            minHeight: "auto",
                            "& .MuiAccordionSummary-content": {
                              margin: "8px 0",
                            },
                          }}
                        >
                          <Typography variant="body2" sx={{ color: "#00acc1", fontWeight: 500 }}>
                            Ver mis roles en esta aplicación
                          </Typography>
                        </AccordionSummary>
                        <AccordionDetails sx={{ px: 0, pt: 0 }}>
                          <List dense>
                            {app.userRoles.map((role, index) => (
                              <ListItem key={index} sx={{ px: 0 }}>
                                <ListItemIcon sx={{ minWidth: 36 }}>{getRoleIcon(role.name)}</ListItemIcon>
                                <ListItemText
                                  primary={role.name}
                                  secondary={role.description}
                                  primaryTypographyProps={{
                                    fontWeight: 500,
                                    color: "#00acc1",
                                  }}
                                />
                              </ListItem>
                            ))}
                          </List>
                        </AccordionDetails>
                      </Accordion>
                    </CardContent>

                    <Divider />

                    <CardActions sx={{ p: 2 }}>
                      <Button
                        variant="contained"
                        endIcon={<Launch />}
                        fullWidth
                        sx={{
                          bgcolor: "#00bcd4",
                          borderRadius: 2,
                          textTransform: "none",
                          fontWeight: 600,
                          "&:hover": {
                            bgcolor: "#00acc1",
                            transform: "translateY(-1px)",
                          },
                          transition: "all 0.2s ease-in-out",
                        }}
                        onClick={() => window.open(app.url, "_blank")}
                      >
                        Ir a aplicación
                      </Button>
                    </CardActions>
                  </Card>
                </Grid>
              ))}
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  )
}

export default Dashboard
