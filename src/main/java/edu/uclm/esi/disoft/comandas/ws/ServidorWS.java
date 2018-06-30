package edu.uclm.esi.disoft.comandas.ws;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONObject;


@ServerEndpoint(value = "/ServidorWS")
public class ServidorWS {
	private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		String sessionID = session.getId();
		sessions.put(sessionID, session);
	}

	@OnMessage
	public void onMessage(Session session, String msg) {
		JSONObject jso = new JSONObject(msg);
		String tipo = jso.getString("tipo");
		if (tipo.equals("solicitarPlatos")) {
			solicitarPlato(jso.getJSONArray("platos"));
		}
	}

	@OnClose
	public void close(Session session) {

	}

	public static void solicitarPlato(JSONArray platos) {
		JSONObject mensaje = new JSONObject();
		mensaje.put("type", "SolicitudDePlatos");
		mensaje.put("platos", platos);
		Enumeration<Session> sesiones = sessions.elements();
		while (sesiones.hasMoreElements()) {
			Session sesion = sesiones.nextElement();
			sesion.getAsyncRemote().sendText(mensaje.toString());// con el getAsyncRemote se envia el contenido
			System.out.println("hola dentro");
			System.out.println(mensaje.toString());
		}
		System.out.println("hola fuera");
	}

}
