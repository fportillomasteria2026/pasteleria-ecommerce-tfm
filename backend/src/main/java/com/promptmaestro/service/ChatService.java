package com.promptmaestro.service;

import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired(required = false)
    private VertexAiGeminiChatModel chatModel;

    @Value("${spring.autoconfigure.exclude:}")
    private String excludeConfig;

    private static final String BUSINESS_CONTEXT = """
        Eres el asistente virtual de BELIETA, una pasteleria artesanal en Alhaurin de la Torre, Malaga.
        Responde SOLO sobre temas relacionados con el negocio. Si la pregunta no es sobre el negocio, responde amablemente que solo puedes ayudar con consultas de la pasteleria.

        DATOS DEL NEGOCIO:
        - Nombre: Belieta Pasteleria Artesanal
        - Direccion: C/ La Torre, 31, CP 29130 Alhaurin de la Torre, Malaga
        - Telefono: 744 60 18 61
        - Horario: Lunes a Sabado, 9:00 - 20:00
        - WhatsApp: 744 60 18 61

        PRODUCTOS Y SERVICIOS:
        - Tartas personalizadas para bodas, cumpleanos y ocasiones especiales
        - Pasteleria fina: croissants, macarons y delicias francesas
        - Postres clasico: tiramisu, cheesecake y otros
        - Elaboraciones especiales y bandejas surtidas
        - Regalos y cajas de dulces artesanales

        REGLAS:
        - Responde en espanol, de forma breve y amable (maximo 3-4 lineas)
        - Si te preguntan por precios, di que llamen al telefono para presupuesto personalizado
        - Si te preguntan por pedidos, recomienda contactar por WhatsApp
        - Usa un tono cercano y profesional
        """;

    public String chat(String userMessage) {
        if (chatModel == null || (excludeConfig != null && excludeConfig.contains("VertexAiGeminiAutoConfiguration"))) {
            return getMockResponse(userMessage);
        }

        String prompt = BUSINESS_CONTEXT + "\n\nPregunta del cliente: " + userMessage;
        return chatModel.call(prompt);
    }

    private String getMockResponse(String message) {
        String lower = message.toLowerCase();

        if (lower.contains("horario") || lower.contains("hora") || lower.contains("abierto")) {
            return "Nuestro horario es de Lunes a Sabado de 9:00 a 20:00. Te esperamos!";
        }
        if (lower.contains("direccion") || lower.contains("donde") || lower.contains("ubicacion") || lower.contains("mapa")) {
            return "Estamos en C/ La Torre, 31, CP 29130 Alhaurin de la Torre, Malaga. Puedes vernos en el mapa en la seccion Quienes Somos.";
        }
        if (lower.contains("telefono") || lower.contains("llamar") || lower.contains("contacto")) {
            return "Puedes llamarnos al 744 60 18 61 o escribirnos por WhatsApp al mismo numero.";
        }
        if (lower.contains("whatsapp")) {
            return "Escribenos por WhatsApp al 744 60 18 61 y te atendemos encantados!";
        }
        if (lower.contains("tarta") || lower.contains("pastel") || lower.contains("cumpleanos") || lower.contains("boda")) {
            return "Hacemos tartas personalizadas para toda ocasion: bodas, cumpleanos, comuniones... Llamanos para presupuesto sin compromiso!";
        }
        if (lower.contains("precio") || lower.contains("coste") || lower.contains("cuanto")) {
            return "Cada producto es unico! Llamanos al 744 60 18 61 para un presupuesto personalizado.";
        }
        if (lower.contains("pedido") || lower.contains("pedir") || lower.contains("encargar")) {
            return "Puedes hacer tu pedido por WhatsApp al 744 60 18 61 o llamarnos directamente. Estamos encantados de ayudarte!";
        }
        if (lower.contains("hola") || lower.contains("buenos") || lower.contains("buenas")) {
            return "Hola! Bienvenido a Belieta. En que podemos ayudarte hoy?";
        }
        if (lower.contains("gracias")) {
            return "De nada! Si necesitas cualquier cosa, aqui estamos. Un saludo!";
        }

        return "Gracias por tu consulta! Para mas informacion, puedes llamarnos al 744 60 18 61 o escribirnos por WhatsApp. En que mas puedo ayudarte?";
    }
}
