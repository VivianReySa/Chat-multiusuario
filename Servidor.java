import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class Servidor  {

	public static void main(String[] args) {
		VentanaServidor cuadro=new VentanaServidor();
		cuadro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class VentanaServidor extends JFrame implements Runnable {
	
	public VentanaServidor(){
		
		setBounds(1200,300,280,350);				
		JPanel panel= new JPanel();
		panel.setLayout(new BorderLayout());
		areatexto=new JTextArea();
		panel.add(areatexto,BorderLayout.CENTER);	
		add(panel);
		setVisible(true);
		
		//Se crea hilo
		Thread mihilo=new Thread(this);
		mihilo.start();	
		}
	
	private	JTextArea areatexto;
	
	public void run() {	
		//Se construye un socket de servidor
		try {
			ServerSocket servidor=new ServerSocket(6789);
			String alias, ip, mensaje;
			Usuario paqueteRecibido;
			//Ciclo para que se envie mas de un mensaje
			while(true) {
				Socket misocket=servidor.accept();
				//Se crea el flujo de entrada
				ObjectInputStream paqueteDatos=new ObjectInputStream(misocket.getInputStream());
				paqueteRecibido=(Usuario) paqueteDatos.readObject();
				alias=paqueteRecibido.getAlias();
				ip=paqueteRecibido.getIp();
				mensaje=paqueteRecibido.getMensaje();
				areatexto.append("\n" + alias + ": " + mensaje + " para " + ip);
				//Imprime hora actual
				LocalDateTime ahora= LocalDateTime.now();
			    int hora = ahora.getHour();
			    int minutos = ahora.getMinute();
			    int segundos = ahora.getSecond();
			    areatexto.append("\n" + hora + ":" + minutos + ":" + segundos);
			    
			    //Se crea un nuevo socket que sera el puente donde viajara la informacion
				Socket enviaDestinatario=new Socket(ip, 9090);
				ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());
				paqueteReenvio.writeObject(paqueteRecibido);
				paqueteReenvio.close();
				enviaDestinatario.close();
				//Se cierra el socket
				misocket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	
	
	