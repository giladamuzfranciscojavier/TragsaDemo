package views;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	private JButton btnUpdateProducto;

	public ProductoTableDialog(MainMenu frame) {
		super(frame, true);
		setResizable(false);
		setTitle("Comprar Producto");
		setBounds(100, 100, 500, 300);
		self = this;
		this.parent=frame;		
		getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(0, 75, 484, 186);
		getContentPane().add(table);
		
		JButton btnAddProducto = new JButton("Añadir Producto");
		btnAddProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addProducto();
				
			}
		});
		btnAddProducto.setBounds(324, 41, 150, 23);
		getContentPane().add(btnAddProducto);
		
		btnBorrarProducto = new JButton("Borrar Producto");
		btnBorrarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borraProducto();
			}
			
		});
		btnBorrarProducto.setBounds(10, 41, 150, 23);
		getContentPane().add(btnBorrarProducto);
		
		btnUpdateProducto = new JButton("Editar Producto");
		btnUpdateProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateProducto();
			}
		});
		btnUpdateProducto.setEnabled(true);
		btnUpdateProducto.setBounds(170, 41, 144, 23);
		getContentPane().add(btnUpdateProducto);
		
		refreshTable();
		
	}
	
	//Refresca la tabla de datos. Se realiza al inicio y después de potenciales cambios en los datos
	private void refreshTable() {
		String[] cols = new String[] {"id", "nombre", "precio", "proveedor", "cliente"};
		
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
		
		list.forEach((it)->{dtm.addRow(new String[] {String.valueOf(it.getProducto_ID()), it.getNombre(), String.valueOf(it.getPrecio()), it.getProveedor_dni(), String.valueOf(it.getCliente_dni())});});
		
		table.setModel(dtm);
		
		table.getSelectionModel().setSelectionInterval(0, 0);
	}
	
	//Abre el diálogo de creación de producto si existen proveedores para suministrarlo
	private void addProducto() {
		if(UsuarioController.readAllUsuarios(false, true).size()>0) {
			new AddProductoDialog(self).setVisible(true);	
			refreshTable();
		}
		else {
			JOptionPane.showMessageDialog(parent, "No hay proveedores disponibles para suministrar el producto",
					"Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	//Abre el diálogo de edición de producto
	private void updateProducto() {
		TableModel tm = table.getModel();
		
		Producto p = new Producto(
				Integer.parseInt(tm.getValueAt(table.getSelectedRow(), 0).toString()), 
				tm.getValueAt(table.getSelectedRow(), 1).toString(),
				Double.parseDouble(tm.getValueAt(table.getSelectedRow(), 2).toString()), 
				tm.getValueAt(table.getSelectedRow(), 3).toString(),
				tm.getValueAt(table.getSelectedRow(), 4).toString()
			);
		new UpdateProductoDialog(self, p).setVisible(true);	
		refreshTable();
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
