package br.inatel.pos.dm111.vfu.api.core.interceptor;

public record AppJWTToken(String issuer,
                          String subject,
                          String role,
                          String method,
                          String uri) {
}
