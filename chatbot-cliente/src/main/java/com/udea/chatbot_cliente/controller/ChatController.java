package com.udea.chatbot_cliente.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    public record PeticionChat(String sessionId, String mensaje) {}
    public record RespuestaChat(String respuesta, String error) {}

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/chat")
    public ResponseEntity<RespuestaChat> chat(@RequestBody PeticionChat req) {
        if (req == null || req.mensaje() == null || req.mensaje().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new RespuestaChat(null, "Escribe un mensaje para continuar."));
        }
        String sesion = (req.sessionId() == null || req.sessionId().isBlank()) ? "default" : req.sessionId();
        try {
            String respuesta = chatClient.prompt()
                    .user(req.mensaje())
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sesion))
                    .call()
                    .content();
            return ResponseEntity.ok(new RespuestaChat(respuesta, null));
        } catch (Exception e) {
            log.error("Error al consultar el modelo", e);
            return ResponseEntity.status(502)
                    .body(new RespuestaChat(null, "No pude responder ahora. Revisa que el servidor MCP (puerto 8089) y la clave GEMINI_API_KEY estén activos."));
        }
    }
}
