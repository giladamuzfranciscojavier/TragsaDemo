package views;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controllers.ProductoController;
import controllers.UsuarioController;
import models.Producto;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProductoTableDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private ProductoTableDialog self;
	private JTable table;
	private JButton btnBorrarProducto;

	private MainMenu parent;

	public ProductoTableDialog(MainMenu frame) {
		super(frame, true);
		setResizable(false);
		setTitle("Comprar Producto");
		setBounds(100, 100, 450, 300);
		self = this;
		this.parent=frame;		
		getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(0, 75, 434, 175);
		getContentPane().add(table);
		
		JButton btnAddProducto = new JButton("Añadir Producto");
		btnAddProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(UsuarioController.readAllUsuarios(false, true).size()>0) {
					new AddProductoDialog(self).setVisible(true);	
					refreshTable();
				}
				else {
					JOptionPane.showMessageDialog(parent, "No hay proveedores disponibles para suministrar el producto",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		btnAddProducto.setBounds(274, 41, 150, 23);
		getContentPane().add(btnAddProducto);
		
		btnBorrarProducto = new JButton("Borrar Producto");
		btnBorrarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borraProducto();
			}
			
		});
		btnBorrarProducto.setBounds(10, 41, 150, 23);
		getContentPane().add(btnBorrarProducto);
		
		refreshTable();
		
	}
	
	//Refresca la tabla de datos. Se realiza al inicio y después de potenciales cambios en los datos
	private void refreshTable() {
		String[] cols = new String[] {"id", "nombre", "precio", "proveedor"};
		
		DefaultTableModel dtm = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		List<Producto> list = ProductoController.readAllProductos();
		
		if(list==null) {
			JOptionPane.showMessageDialog(parent, "Se ha perdido la conexión con la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}
		
		btnBorrarProducto.setEnabled(!list.isEmpty());
		
		list.forEach((it)->{dtm.addRow(new String[] {String.valueOf(it.getProducto_ID()), it.getNombre(), String.valueOf(it.getPrecio()), it.getProveedor_dni()});});
		
		table.setModel(dtm);
	}
	
	
	
	//Borra el producto seleccionado y refresca la tabla
	private void borraProducto() {
		int id = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
		int opcion = JOptionPane.showConfirmDialog(self, "¿Borrar Producto?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
		if(opcion==JOptionPane.YES_OPTION) {
			ProductoController.deleteProducto(id);
		}
		else {
			return;
		}
		
		refreshTable();
	}
	
}
