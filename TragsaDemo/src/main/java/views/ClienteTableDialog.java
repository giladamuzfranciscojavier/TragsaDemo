package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.ProductoController;
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
	private JButton btnDeleteCliente;
	private JButton btnNewButton_2;
	private JButton btnProductosComprados;
	private MainMenu parent;
	
	private ClienteTableDialog self;

	public ClienteTableDialog(MainMenu parent) {
		super(parent,true);
		setResizable(false);
		setTitle("Clientes");
		this.parent=parent;
		self = this;
		setLocationRelativeTo(parent);
		setBounds(100, 100, 510, 401);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 590, 362);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		
		JButton btnAddCliente = new JButton("Añadir Cliente");
		btnAddCliente.setBounds(10, 35, 124, 23);
		btnAddCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddUsuarioDialog(self, true, false).setVisible(true);
				refreshTable();
			}
		});
		contentPanel.setLayout(null);
		contentPanel.add(btnAddCliente);
		
		btnDeleteCliente = new JButton("Borrar Cliente");
		btnDeleteCliente.setBounds(144, 35, 124, 23);
		btnDeleteCliente.addActionListener(new ActionListener() {
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
				
				refreshTable();
			}
		});
		contentPanel.add(btnDeleteCliente);
		
		btnProductosComprados = new JButton("Ver Productos Comprados");
		btnProductosComprados.setBounds(290, 35, 200, 23);
		btnProductosComprados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ProductosCompradosDialog(self, table.getModel().getValueAt(table.getSelectedRow(), 0).toString()).setVisible(true);
			}
		});
		contentPanel.add(btnProductosComprados);
		
		btnNewButton_2 = new JButton("Comprar Producto");
		btnNewButton_2.setBounds(339, 87, 151, 23);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ProductoController.readAllProductosSinComprar().size()>0) {
					new ComprarProductoDialog(self, table.getModel().getValueAt(table.getSelectedRow(), 0).toString()).setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(parent, "No hay productos disponibles para comprar", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		contentPanel.add(btnNewButton_2);
		{
			table = new JTable();
			table.setBounds(5, 121, 485, 236);
			contentPanel.add(table);
		}
		
		refreshTable();
				
	}
	
	private void refreshTable() {
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
			self.dispose();
		}
		
		list.forEach((it)->{dtm.addRow(new String[] {it.getDNI(), it.getNombre()});});
		table.setModel(dtm);
		table.getSelectionModel().setSelectionInterval(0, 0);
	}

}
