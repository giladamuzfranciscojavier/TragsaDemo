package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.DBController;

import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnClientes = new JButton("Clientes");
	private JButton btnProveedores = new JButton("Proveedores");
	JButton btnProductos = new JButton("Productos");
	JMenuItem menuProveedores = new JMenuItem("Proveedores");
	JMenuItem menuClientes = new JMenuItem("Clientes");
	JMenuItem menuProductos = new JMenuItem("Productos");

	private static MainMenu frame = null;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();					
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setTitle("TragsaDemo");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 120);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tablas");
		menuBar.add(mnNewMenu);		
		
		menuClientes.setEnabled(false);
		menuClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClientes.doClick();
			}
		});
		mnNewMenu.add(menuClientes);
		
		menuProveedores.setEnabled(false);
		menuProveedores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnProveedores.doClick();
			}
		});
		mnNewMenu.add(menuProveedores);		
		
		menuProductos.setEnabled(false);
		menuProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnProductos.doClick();
			}
		});
		mnNewMenu.add(menuProductos);
		
		JMenu mnNewMenu_1 = new JMenu("Base de datos");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Conexión");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DBLogin(frame).setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Conexión por defecto");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DBController.connect(DBController.URL,DBController.USER, DBController.PSWD)) {
					JOptionPane.showMessageDialog(null, "Exito al conectar con la base de datos", "Conectado", JOptionPane.PLAIN_MESSAGE);
					toggleEnabledButtons(true);
				}
				else {
					JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
					toggleEnabledButtons(false);
				}
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		btnClientes.setBounds(10, 11, 120, 23);
		
		
		btnClientes.setEnabled(false);
		btnClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ClienteTableDialog(frame).setVisible(true);
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnClientes);
		btnProductos.setBounds(270, 11, 120, 23);
		
		btnProductos.setEnabled(false);
		btnProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});		
		btnProveedores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ProveedorTableDialog(frame).setVisible(true);;
			}
		});
		btnProveedores.setBounds(140, 11, 120, 23);
		
		btnProveedores.setEnabled(false);
		contentPane.add(btnProveedores);
		contentPane.add(btnProductos);
	}
	
	
	public void toggleEnabledButtons(boolean enabled) {
		btnProductos.setEnabled(enabled);
		menuProductos.setEnabled(enabled);
		btnClientes.setEnabled(enabled);
		menuClientes.setEnabled(enabled);
		btnProveedores.setEnabled(enabled);
		menuProveedores.setEnabled(enabled);
	}
	
}
