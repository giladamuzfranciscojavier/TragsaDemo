package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.DBController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPasswordField;
import javax.swing.JLabel;

public class DBLogin extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField user;
	private JPasswordField pswd;
	
	private static DBLogin self;
	private JTextField url;


	public DBLogin(MainMenu parent) {
		super(parent,true);
		setResizable(false);
		self=this;
		setLocationRelativeTo(parent);
		setBounds(100, 100, 304, 196);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblBaseDeDatos = new JLabel("Base de Datos");
			GridBagConstraints gbc_lblBaseDeDatos = new GridBagConstraints();
			gbc_lblBaseDeDatos.insets = new Insets(0, 0, 5, 5);
			gbc_lblBaseDeDatos.gridx = 1;
			gbc_lblBaseDeDatos.gridy = 1;
			contentPanel.add(lblBaseDeDatos, gbc_lblBaseDeDatos);
		}
		{
			url = new JTextField();
			GridBagConstraints gbc_url = new GridBagConstraints();
			gbc_url.insets = new Insets(0, 0, 5, 5);
			gbc_url.fill = GridBagConstraints.HORIZONTAL;
			gbc_url.gridx = 3;
			gbc_url.gridy = 1;
			contentPanel.add(url, gbc_url);
			url.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("Usuario");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 2;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			user = new JTextField();
			GridBagConstraints gbc_user = new GridBagConstraints();
			gbc_user.insets = new Insets(0, 0, 5, 5);
			gbc_user.fill = GridBagConstraints.HORIZONTAL;
			gbc_user.gridx = 3;
			gbc_user.gridy = 2;
			contentPanel.add(user, gbc_user);
			user.setColumns(10);
		}
		{
			JLabel lblContrasea = new JLabel("Contraseña");
			GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
			gbc_lblContrasea.insets = new Insets(0, 0, 0, 5);
			gbc_lblContrasea.anchor = GridBagConstraints.EAST;
			gbc_lblContrasea.gridx = 1;
			gbc_lblContrasea.gridy = 3;
			contentPanel.add(lblContrasea, gbc_lblContrasea);
		}
		{
			pswd = new JPasswordField();
			GridBagConstraints gbc_pswd = new GridBagConstraints();
			gbc_pswd.insets = new Insets(0, 0, 0, 5);
			gbc_pswd.fill = GridBagConstraints.HORIZONTAL;
			gbc_pswd.gridx = 3;
			gbc_pswd.gridy = 3;
			contentPanel.add(pswd, gbc_pswd);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Aceptar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						connect(parent);
					}
					
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						self.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	//Trata de conectar con la base de datos. En caso de conseguirlo desbloquea los botones del menú principal
	private void connect(MainMenu parent) {
		if(DBController.connect(url.getText(),user.getText(), String.valueOf(pswd.getPassword()))) {
			JOptionPane.showMessageDialog(null, "Exito al conectar con la base de datos", "Conectado", JOptionPane.PLAIN_MESSAGE);
			parent.toggleEnabledButtons(true);
			self.dispose();
		}
		else {
			parent.toggleEnabledButtons(false);
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
		}
	}

}
