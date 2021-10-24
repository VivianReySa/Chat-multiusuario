import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Cliente {

	public static void main(String[] args) {
		
		VentanaCliente cuadro = new VentanaCliente();
		cuadro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class VentanaCliente extends JFrame {
	
	public VentanaCliente() {

		setBounds(600, 300, 280, 350);
		PanelCliente panel = new PanelCliente();
		add(panel);
		setVisible(true);
	}

}

class PanelCliente extends JPanel implements Runnable {

	public PanelCliente() {	
		JLabel nombre = new JLabel("Nombre: ");
		add(nombre);
		alias=new JTextField(5);
		add(alias);
		
		//JLabel texto = new JLabel("*****CHAT*****");
		//add(texto);
		
		JLabel direccionIP = new JLabel("IP: ");
		add(direccionIP);
		ip=new JTextField(8);
		add(ip);
		
		espaciochat=new JTextArea(12,20);
		add(espaciochat);
		
		campo1 = new JTextField(20);
		add(campo1);

		miboton = new JButton("Enviar");
		EnviaTexto mimensaje = new EnviaTexto();
		miboton.addActionListener(mimensaje);
		add(miboton);
		
		//Se crea hilo
		Thread mihilo=new Thread(this);
		mihilo.start();
	}

	private class EnviaTexto implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			
			// Se crea el socket
			try {
				Socket misocket = new Socket("localhost", 6789);
				Usuario datos=new Usuario();
				datos.setAlias(alias.getText());
				datos.setIp(ip.getText());
				datos.setMensaje(campo1.getText());
				//Se crea el flujo para enviar el objeto al destinatario
				ObjectOutputStream paqueteDatos=new ObjectOutputStream(misocket.getOutputStream());
				paqueteDatos.writeObject(datos);
				misocket.close();
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		}

	}

	private JTextField campo1, alias, ip;
	private JTextArea espaciochat;
	private JButton miboton;

	
	public void run() {
		try {
			//Se crea el socket para que el cliente este atento al servidor
			ServerSocket servidor_cliente = new ServerSocket(9090);
			Socket cliente;
			Usuario paqueteRecibido;
			while(true) {
				cliente=servidor_cliente.accept();
				ObjectInputStream datosEntrada = new ObjectInputStream(cliente.getInputStream());
				//Se recibe toda la informacion que le llega al cliente por parte del servidor (alias, ip, mensaje) 
				paqueteRecibido=(Usuario) datosEntrada.readObject();
				espaciochat.append("\n"+ paqueteRecibido.getAlias() + ": " + paqueteRecibido.getMensaje());
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}

class Usuario implements Serializable{//Todas las instancias que pertenecen a esta clase podran ser enviadas a traves de la red 
	private String alias, ip, mensaje;
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}	
}






