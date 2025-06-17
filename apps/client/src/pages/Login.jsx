"use client"

import { useState } from "react"
import {
    Box,
    Button,
    TextField,
    Typography,
    Paper,
    Avatar,
    IconButton,
    InputAdornment,
    Fade,
    Container,
} from "@mui/material"
import { LockOutlined, Visibility, VisibilityOff, Email } from "@mui/icons-material"

const Login = () => {
    const [showPassword, setShowPassword] = useState(false)
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    })

    const handleClickShowPassword = () => setShowPassword(!showPassword)
    const handleMouseDownPassword = (event) => event.preventDefault()

    const handleChange = (event) => {
        setFormData({
            ...formData,
            [event.target.name]: event.target.value,
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        console.log("Login attempt:", formData)
    }

    return (
        <Container component="main" maxWidth="sm">
            <Box
                sx={{
                    minHeight: "100vh",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    py: 4,
                }}
            >
                <Fade in={true} timeout={800}>
                    <Paper
                        elevation={8}
                        sx={{
                            p: { xs: 3, sm: 5 },
                            borderRadius: 3,
                            background: "linear-gradient(145deg, #ffffff 0%, #f8f9fa 100%)",
                            boxShadow: "0 8px 32px rgba(0, 0, 0, 0.1)",
                        }}
                    >
                        {/* Header */}
                        <Box
                            sx={{
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "center",
                                mb: 4,
                            }}
                        >
                            <Avatar
                                sx={{
                                    m: 1,
                                    bgcolor: "#00bcd4",
                                    width: 56,
                                    height: 56,
                                    boxShadow: "0 4px 20px rgba(0, 188, 212, 0.3)",
                                }}
                            >
                                <LockOutlined sx={{ fontSize: 28 }} />
                            </Avatar>
                            <Typography
                                component="h1"
                                variant="h4"
                                sx={{
                                    fontWeight: 600,
                                    color: "text.primary",
                                    mb: 1,
                                }}
                            >
                                Bienvenido
                            </Typography>
                            <Typography variant="body1" color="text.secondary" sx={{ textAlign: "center" }}>
                                Inicia sesión en tu cuenta
                            </Typography>
                        </Box>

                        {/* Form */}
                        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="email"
                                label="Correo electrónico"
                                name="email"
                                autoComplete="email"
                                autoFocus
                                value={formData.email}
                                onChange={handleChange}
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <Email color="action" />
                                        </InputAdornment>
                                    ),
                                }}
                                sx={{
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
                            />
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                name="password"
                                label="Contraseña"
                                type={showPassword ? "text" : "password"}
                                id="password"
                                autoComplete="current-password"
                                value={formData.password}
                                onChange={handleChange}
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <LockOutlined color="action" />
                                        </InputAdornment>
                                    ),
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={handleClickShowPassword}
                                                onMouseDown={handleMouseDownPassword}
                                                edge="end"
                                            >
                                                {showPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    ),
                                }}
                                sx={{
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
                            />

                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{
                                    mt: 2,
                                    mb: 3,
                                    py: 1.5,
                                    borderRadius: 2,
                                    fontSize: "1.1rem",
                                    fontWeight: 600,
                                    textTransform: "none",
                                    backgroundColor: "#00bcd4",
                                    boxShadow: "0 4px 15px rgba(0, 188, 212, 0.3)",
                                    "&:hover": {
                                        backgroundColor: "#00acc1",
                                        boxShadow: "0 6px 20px rgba(0, 172, 193, 0.4)",
                                        transform: "translateY(-1px)",
                                    },
                                    transition: "all 0.2s ease-in-out",
                                }}
                            >
                                Iniciar Sesión
                            </Button>
                        </Box>
                    </Paper>
                </Fade>
            </Box>
        </Container>
    )
}

export default Login
