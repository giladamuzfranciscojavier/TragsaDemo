package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.UsuarioController;
import models.usuarios.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddUsuarioDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtdni;
	private JTextField txtnombre;
	JCheckBox chckbxAddAsOther = new JCheckBox("");
	
	private AddUsuarioDialog self;
	
	private boolean cliente;
	private boolean proveedor;

 
	public AddUsuarioDialog(JDialog parent, boolean cliente, boolean proveedor) {
		super(parent,true);
		setResizable(false);
		
		//Ajusta el título y el texto de la checkbox en base al origen
		if(cliente) {
			setTitle("Añadir Cliente");
			chckbxAddAsOther.setText("Añadir también como Proveedor");
		}
		else if(proveedor) {
			setTitle("Añadir Proveedor");
			chckbxAddAsOther.setText("Añadir también como Cliente");
		}
		
		self=this;
		setBounds(100, 100, 321, 232);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("DNI");
			lblNewLabel.setBounds(13, 49, 77, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			txtdni = new JTextField();
			txtdni.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if(txtdni.getText().length()>8 && !e.isActionKey()) {
						e.consume();
					}
				}
			});
			txtdni.setBounds(69, 49, 226, 20);
			txtdni.setColumns(10);
			contentPanel.add(txtdni);
		}
		{
			JLabel lblNombre = new JLabel("Nombre");
			lblNombre.setBounds(10, 83, 80, 14);
			contentPanel.add(lblNombre);
		}
		{
			txtnombre = new JTextField();
			txtnombre.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if(txtnombre.getText().length()>49 && !e.isActionKey()) {
						e.consume();
					}
				}
			});
			txtnombre.setBounds(69, 80, 226, 20);
			txtnombre.setColumns(10);
			contentPanel.add(txtnombre);
		}
		{
			chckbxAddAsOther.setBounds(69, 106, 226, 21);
			contentPanel.add(chckbxAddAsOther);
		}
		{
			JButton okButton = new JButton("Aceptar");
			okButton.setBounds(103, 147, 91, 23);
			contentPanel.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addUsuario(cliente, proveedor);						
				}

				
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancelar");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					self.dispose();
				}
			});
			cancelButton.setBounds(204, 147, 91, 23);
			contentPanel.add(cancelButton);
			cancelButton.setActionCommand("Cancel");
		}
	}
	
	
	//Añade un usuario de un tipo o de ambos
	private void addUsuario(boolean cliente, boolean proveedor) {
		
		//Si ya existe el usuario no hace nada
		if(UsuarioController.readUsuario(cliente, proveedor, txtdni.getText())!=null) {
			JOptionPane.showMessageDialog(self, "Ya existe ese usuario","Error", JOptionPane.WARNING_MESSAGE);
		}
		
		String nombre = txtnombre.getText().strip();
		String dni = txtdni.getText().strip();
		
		if(nombre.isBlank() || dni.isBlank()) {
			JOptionPane.showMessageDialog(self, "Comprueba que no queden campos sin cubrir", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(nombre.length()<1) {
			JOptionPane.showMessageDialog(self, "No se puede crear un producto sin nombre", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		else {			
			
			//Si la checkbox está marcada se añade como ambos tipos
			if(chckbxAddAsOther.isSelected()) {
				if(UsuarioController.createUpdateUsuario(true, true, new ClienteProveedor(dni, nombre))) {
					JOptionPane.showMessageDialog(self, "Usuario creado con éxito","Éxito", JOptionPane.INFORMATION_MESSAGE);
					self.dispose();
				}
				else {
					JOptionPane.showMessageDialog(self, "Error al crear el usuario","Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			//Si no está marcada se añade como el tipo correspondiente al origen
			else {
				if(UsuarioController.createUpdateUsuario(cliente, proveedor, new Cliente(txtdni.getText(), txtnombre.getText()))) {
					JOptionPane.showMessageDialog(self, "Usuario creado con éxito","Éxito", JOptionPane.INFORMATION_MESSAGE);
					self.dispose();
				}	
				else {
					JOptionPane.showMessageDialog(self, "Error al crear el usuario","Error", JOptionPane.WARNING_MESSAGE);
				}							
			}
		}
	}

}
