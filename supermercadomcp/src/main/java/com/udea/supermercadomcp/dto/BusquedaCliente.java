package com.udea.supermercadomcp.dto;

import com.udea.supermercadomcp.model.Cliente;

public record BusquedaCliente(boolean encontrado, Cliente cliente, String mensaje) {}
