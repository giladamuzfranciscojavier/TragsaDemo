package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.UsuarioController;
import models.usuarios.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UpdateUsuarioDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtdni;
	private JTextField txtnombre;
	
	private UpdateUsuarioDialog self;
	
 
	public UpdateUsuarioDialog(JDialog parent, Usuario usuario) {
		super(parent,true);
		setResizable(false);
		
		setTitle("Editar Usuario");
		
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
			txtdni.setEnabled(false);
			txtdni.getInputMap().put(KeyStroke.getKeyStroke("control V"), "none");
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
			txtnombre.getInputMap().put(KeyStroke.getKeyStroke("control V"), "none");
			contentPanel.add(txtnombre);
		}
		{
			JButton okButton = new JButton("Aceptar");
			okButton.setBounds(103, 147, 91, 23);
			contentPanel.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addUsuario();						
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
		txtdni.setText(usuario.getDNI());
		txtnombre.setText(usuario.getNombre());
	}
	
	
	//Añade un usuario de un tipo o de ambos
	private void addUsuario() {
				
		String nombre = txtnombre.getText().strip();
		String dni = txtdni.getText();
		
		if(nombre.isBlank()) {
			JOptionPane.showMessageDialog(self, "Comprueba que no queden campos sin cubrir", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		else {			
			
			if(UsuarioController.createUpdateUsuario(true, true, new ClienteProveedor(dni, nombre))) {
				JOptionPane.showMessageDialog(self, "Usuario editado con éxito","Éxito", JOptionPane.INFORMATION_MESSAGE);
				self.dispose();
			}
			else {
				JOptionPane.showMessageDialog(self, "Error al editar el usuario","Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
