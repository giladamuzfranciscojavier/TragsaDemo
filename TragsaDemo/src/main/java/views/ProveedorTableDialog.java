package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.UsuarioController;
import models.usuarios.Cliente;
import models.usuarios.Usuario;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProveedorTableDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton btnDeleteProveedor;
	private MainMenu parent;
	
	private ProveedorTableDialog self;
	private JButton btnUpdateProveedor;

	public ProveedorTableDialog(MainMenu parent) {
		super(parent,true);
		setResizable(false);
		setTitle("Proveedores");
		this.parent=parent;
		self = this;
		setLocationRelativeTo(parent);
		setBounds(100, 100, 510, 401);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JButton btnAddProveedor = new JButton("Añadir Proveedor");
		btnAddProveedor.setBounds(10, 35, 150, 23);
		btnAddProveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddUsuarioDialog(self, false, true).setVisible(true);
				refreshTable();
			}
		});
		contentPanel.setLayout(null);
		contentPanel.add(btnAddProveedor);
		
		btnDeleteProveedor = new JButton("Borrar Proveedor");
		btnDeleteProveedor.setBounds(334, 35, 150, 23);
		btnDeleteProveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dni = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
				int opcion = JOptionPane.showConfirmDialog(self, "¿Borrar también como cliente?", "Confirmar borrado", JOptionPane.YES_NO_CANCEL_OPTION);
				if(opcion==JOptionPane.YES_OPTION) {
					UsuarioController.deleteUsuario(true, true, dni);
				}
				else if(opcion==JOptionPane.NO_OPTION) {
					UsuarioController.deleteUsuario(false, true, dni);
				}
				else {
					return;
				}
				
				refreshTable();
			}
		});
		contentPanel.add(btnDeleteProveedor);
		{
			table = new JTable();
			table.setBounds(0, 69, 494, 288);
			contentPanel.add(table);
		}
		
		btnUpdateProveedor = new JButton("Editar Proveedor");
		btnUpdateProveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dni = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
				String nombre = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
				new UpdateUsuarioDialog(self, new Cliente(dni,nombre)).setVisible(true);
				refreshTable();
			}
		});
		btnUpdateProveedor.setBounds(174, 35, 150, 23);
		contentPanel.add(btnUpdateProveedor);
		
		refreshTable();
				
	}
	
	
	//Refresca la tabla de datos. Se realiza al inicio y después de potenciales cambios en los datos
	private void refreshTable() {
		String[] cols = {"DNI", "Nombre"};
		DefaultTableModel dtm = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
	
		List<Usuario> list = UsuarioController.readAllUsuarios(false, true);
		
		if(list==null) {
			parent.toggleEnabledButtons(false);
			JOptionPane.showMessageDialog(parent, "Se ha perdido la conexión con la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}
		
		btnDeleteProveedor.setEnabled(!list.isEmpty());
		btnUpdateProveedor.setEnabled(!list.isEmpty());
		
		list.forEach((it)->{dtm.addRow(new String[] {it.getDNI(), it.getNombre()});});
		table.setModel(dtm);
		
		table.getSelectionModel().setSelectionInterval(0, 0);
		
	}

}
