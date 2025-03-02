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
					if(UsuarioController.readUsuario(cliente, proveedor, txtdni.getText())!=null) {
						JOptionPane.showMessageDialog(self, "Ya existe ese usuario","Error", JOptionPane.WARNING_MESSAGE);
					}
					else {							
						if(chckbxAddAsOther.isSelected()) {
							if(UsuarioController.createUpdateUsuario(true, true, new ClienteProveedor(txtdni.getText(), txtnombre.getText()))) {
								JOptionPane.showMessageDialog(self, "Usuario creado con éxito","Éxito", JOptionPane.INFORMATION_MESSAGE);
								self.dispose();
							}
							else {
								JOptionPane.showMessageDialog(self, "Error al crear el usuario","Error", JOptionPane.WARNING_MESSAGE);
							}
						}
						
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

}
