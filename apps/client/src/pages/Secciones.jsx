"use client"

import { useState, useMemo } from "react"
import { Box, Typography, TextField, Button, IconButton, Paper, InputAdornment, Tooltip } from "@mui/material"
import { Category, Search, Add, Help, Edit, Delete, Visibility } from "@mui/icons-material"
import {
  useReactTable,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  flexRender,
} from "@tanstack/react-table"

// Datos de ejemplo
const mockSecciones = [
  {
    id: 1,
    nombre: "Dashboard",
    descripcion: "Sección de visualización general del sistema.",
  },
  {
    id: 2,
    nombre: "Gestión de Usuarios",
    descripcion: "Sección para la administración de usuarios del sistema.",
  },
  {
    id: 3,
    nombre: "Gestión de Tipos de Usuarios",
    descripcion: "Sección para la administración de roles y tipos de usuario.",
  },
  {
    id: 4,
    nombre: "Gestión de Aplicaciones",
    descripcion: "Sección para la administración de aplicaciones registradas.",
  },
  {
    id: 5,
    nombre: "Gestión de Permisos",
    descripcion: "Sección para la administración de permisos de los tipos de usuario.",
  },
]

const Secciones = () => {
  const [globalFilter, setGlobalFilter] = useState("")

  // Funciones para las acciones (placeholder)
  const handleEdit = (seccion) => {
    console.log("Editar sección:", seccion)
  }

  const handleDelete = (seccion) => {
    console.log("Eliminar sección:", seccion)
  }

  const handleViewActions = (seccion) => {
    console.log("Ver acciones de sección:", seccion)
  }

  const handleAdd = () => {
    console.log("Agregar nueva sección")
  }

  const handleHelp = () => {
    console.log("Mostrar ayuda")
  }

  // Definición de columnas
  const columns = useMemo(
    () => [
      {
        accessorKey: "nombre",
        header: "Nombre",
        cell: ({ getValue }) => (
          <Typography variant="body2" sx={{ fontWeight: 500 }}>
            {getValue()}
          </Typography>
        ),
      },
      {
        accessorKey: "descripcion",
        header: "Descripción",
        cell: ({ getValue }) => (
          <Typography variant="body2" color="text.secondary">
            {getValue()}
          </Typography>
        ),
      },
      {
        id: "acciones",
        header: "Acciones",
        cell: ({ row }) => (
          <Box sx={{ display: "flex", gap: 1 }}>
            <Tooltip title="Editar">
              <IconButton
                size="small"
                onClick={() => handleEdit(row.original)}
                sx={{
                  color: "#00acc1",
                  "&:hover": {
                    bgcolor: "rgba(0, 172, 193, 0.1)",
                  },
                }}
              >
                <Edit fontSize="small" />
              </IconButton>
            </Tooltip>
            <Tooltip title="Eliminar">
              <IconButton
                size="small"
                onClick={() => handleDelete(row.original)}
                sx={{
                  color: "#d32f2f",
                  "&:hover": {
                    bgcolor: "rgba(211, 47, 47, 0.1)",
                  },
                }}
              >
                <Delete fontSize="small" />
              </IconButton>
            </Tooltip>
            <Tooltip title="Revisar acciones">
              <IconButton
                size="small"
                onClick={() => handleViewActions(row.original)}
                sx={{
                  color: "#00695c",
                  "&:hover": {
                    bgcolor: "rgba(0, 105, 92, 0.1)",
                  },
                }}
              >
                <Visibility fontSize="small" />
              </IconButton>
            </Tooltip>
          </Box>
        ),
      },
    ],
    [],
  )

  // Configuración de la tabla
  const table = useReactTable({
    data: mockSecciones,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    state: {
      globalFilter,
    },
    onGlobalFilterChange: setGlobalFilter,
    initialState: {
      pagination: {
        pageSize: 10,
      },
    },
  })

  return (
    <Box sx={{ p: 0 }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography
          variant="h4"
          component="h1"
          sx={{
            fontWeight: 700,
            color: "#006064",
            mb: 1,
            display: "flex",
            alignItems: "center",
            gap: 2,
          }}
        >
          <Category sx={{ fontSize: 32 }} />
          Gestión de Secciones
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Administra las secciones del sistema, sus nombres y descripciones. Aquí puedes crear, editar, eliminar y
          revisar las acciones disponibles para cada sección.
        </Typography>
      </Box>

      {/* Barra de herramientas */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 3,
          gap: 2,
        }}
      >
        <TextField
          placeholder="Buscar secciones..."
          value={globalFilter ?? ""}
          onChange={(e) => setGlobalFilter(e.target.value)}
          sx={{
            flex: 1,
            maxWidth: 400,
            "& .MuiOutlinedInput-root": {
              borderRadius: 2,
              "&:hover fieldset": {
                borderColor: "#00bcd4",
              },
              "&.Mui-focused fieldset": {
                borderColor: "#00acc1",
              },
            },
          }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <Search sx={{ color: "#00acc1" }} />
              </InputAdornment>
            ),
          }}
        />

        <Box sx={{ display: "flex", gap: 2 }}>
          <Tooltip title="Ayuda">
            <IconButton
              onClick={handleHelp}
              sx={{
                bgcolor: "#e0f7fa",
                color: "#00acc1",
                "&:hover": {
                  bgcolor: "#b2ebf2",
                },
              }}
            >
              <Help />
            </IconButton>
          </Tooltip>

          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={handleAdd}
            sx={{
              bgcolor: "#006064",
              borderRadius: 2,
              textTransform: "none",
              fontWeight: 600,
              px: 3,
              "&:hover": {
                bgcolor: "#004d50",
                transform: "translateY(-1px)",
              },
              transition: "all 0.2s ease-in-out",
            }}
          >
            Agregar Sección
          </Button>
        </Box>
      </Box>

      {/* Tabla */}
      <Paper
        elevation={2}
        sx={{
          borderRadius: 3,
          overflow: "hidden",
          boxShadow: "0 4px 20px rgba(0, 96, 100, 0.1)",
        }}
      >
        <Box sx={{ overflowX: "auto" }}>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#004d50" }}>
                {table.getHeaderGroups().map((headerGroup) =>
                  headerGroup.headers.map((header) => (
                    <th
                      key={header.id}
                      style={{
                        padding: "16px 24px",
                        textAlign: "center",
                        color: "white",
                        fontWeight: 600,
                        fontSize: "0.875rem",
                        letterSpacing: "0.5px",
                      }}
                    >
                      {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                    </th>
                  )),
                )}
              </tr>
            </thead>
            <tbody>
              {table.getRowModel().rows.map((row, index) => (
                <tr
                  key={row.id}
                  style={{
                    backgroundColor: index % 2 === 0 ? "#ffffff" : "#f8f9fa",
                    transition: "background-color 0.2s ease-in-out",
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.backgroundColor = "#e0f7fa"
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.backgroundColor = index % 2 === 0 ? "#ffffff" : "#f8f9fa"
                  }}
                >
                  {row.getVisibleCells().map((cell) => (
                    <td
                      key={cell.id}
                      style={{
                        padding: "16px 24px",
                        borderBottom: "1px solid #e0e0e0",
                      }}
                    >
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </Box>

        {/* Información de paginación */}
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            p: 2,
            borderTop: "1px solid #e0e0e0",
            bgcolor: "#fafafa",
          }}
        >
          <Typography variant="body2" color="text.secondary">
            Mostrando {table.getRowModel().rows.length} de {mockSecciones.length} secciones
          </Typography>

          <Box sx={{ display: "flex", gap: 1 }}>
            <Button
              size="small"
              onClick={() => table.previousPage()}
              disabled={!table.getCanPreviousPage()}
              sx={{
                color: "#00acc1",
                "&:hover": {
                  bgcolor: "rgba(0, 172, 193, 0.1)",
                },
              }}
            >
              Anterior
            </Button>
            <Button
              size="small"
              onClick={() => table.nextPage()}
              disabled={!table.getCanNextPage()}
              sx={{
                color: "#00acc1",
                "&:hover": {
                  bgcolor: "rgba(0, 172, 193, 0.1)",
                },
              }}
            >
              Siguiente
            </Button>
          </Box>
        </Box>
      </Paper>
    </Box>
  )
}

export default Secciones