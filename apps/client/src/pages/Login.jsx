import React from 'react';
import { Box, Button, TextField, Typography, Paper } from '@mui/material';

const Login = () => {
    return (
        <Box
            component={Paper}
            elevation={3}
            sx={{
                maxWidth: 400,
                mx: 'auto',
                p: 4,
                mt: 8,
            }}
        >
            <Typography variant="h5" component="h2" gutterBottom>
                Iniciar Sesión
            </Typography>
            <Box component="form">
                <TextField
                    label="Correo electrónico"
                    type="email"
                    name="email"
                    id="email"
                    fullWidth
                    required
                    margin="normal"
                />
                <TextField
                    label="Contraseña"
                    type="password"
                    name="password"
                    id="password"
                    fullWidth
                    required
                    margin="normal"
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ mt: 2, py: 1.5 }}
                >
                    Entrar
                </Button>
            </Box>
        </Box>
    );
};

export default Login;