package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.UsuarioController;
import models.usuarios.Usuario;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClienteTableDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private MainMenu parent;
	
	private ClienteTableDialog self;

	public ClienteTableDialog(MainMenu parent) {
		super(parent,true);
		this.parent=parent;
		self = this;
		setLocationRelativeTo(null);
		setBounds(100, 100, 510, 401);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JButton btnAddCliente = new JButton("Añadir Cliente");
		btnAddCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddUsuarioDialog(self, true, false).setVisible(true);
				loadClientes();
			}
		});
		GridBagConstraints gbc_btnAddCliente = new GridBagConstraints();
		gbc_btnAddCliente.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddCliente.gridx = 1;
		gbc_btnAddCliente.gridy = 1;
		contentPanel.add(btnAddCliente, gbc_btnAddCliente);
		
		btnNewButton_1 = new JButton("Borrar Cliente");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dni = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
				int opcion = JOptionPane.showConfirmDialog(self, "¿Borrar también como proveedor?", "Confirmar borrado", JOptionPane.YES_NO_CANCEL_OPTION);
				if(opcion==JOptionPane.YES_OPTION) {
					UsuarioController.deleteUsuario(true, true, dni);
				}
				else if(opcion==JOptionPane.NO_OPTION) {
					UsuarioController.deleteUsuario(true, false, dni);
				}
				else {
					return;
				}
				
				loadClientes();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 1;
		contentPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		btnNewButton_3 = new JButton("Ver Productos Comprados");
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 6;
		gbc_btnNewButton_3.gridy = 1;
		contentPanel.add(btnNewButton_3, gbc_btnNewButton_3);
		
		btnNewButton_2 = new JButton("Comprar Producto");
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 6;
		gbc_btnNewButton_2.gridy = 3;
		contentPanel.add(btnNewButton_2, gbc_btnNewButton_2);
		{
			table = new JTable();
			GridBagConstraints gbc_table = new GridBagConstraints();
			gbc_table.gridwidth = 11;
			gbc_table.fill = GridBagConstraints.BOTH;
			gbc_table.gridx = 0;
			gbc_table.gridy = 4;
			contentPanel.add(table, gbc_table);
		}
		
		loadClientes();
				
	}
	
	private void loadClientes() {
		String[] cols = {"DNI", "Nombre"};
		DefaultTableModel dtm = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
	
		List<Usuario> list = UsuarioController.readAllUsuarios(true, false);
		
		if(list==null) {
			parent.toggleEnabledButtons(false);
			JOptionPane.showMessageDialog(parent, "Se ha perdido la conexión con la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
			this.dispose();
		}
		
		UsuarioController.readAllUsuarios(true, false).forEach((it)->{dtm.addRow(new String[] {it.getDNI(), it.getNombre()});});
		table.setModel(dtm);
	}

}
