package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.ClienteController;
import controllers.ProductoController;
import controllers.UsuarioController;
import models.Producto;

import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProductosCompradosDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private ProductosCompradosDialog self;
	private JTable table;
	private JDialog parent;
	private String dni;


	public ProductosCompradosDialog(JDialog parent, String dni) {
		super(parent, true);
		setResizable(false);
		setTitle("Productos Comprados");
		setBounds(100, 100, 450, 300);
		self = this;
		this.parent=parent;
		this.dni=dni;
		getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(0, 77, 434, 184);
		getContentPane().add(table);
		
		JButton btnCancelBuy = new JButton("Cancelar Compra");
		btnCancelBuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
				
				if(ProductoController.restablecerCompra(id)) {
					JOptionPane.showMessageDialog(self, "Producto devuelto con éxito","Éxito", JOptionPane.INFORMATION_MESSAGE);
					refreshTable();
				}
				
				else {
					JOptionPane.showMessageDialog(self, "Error al devolver el producto","Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnCancelBuy.setBounds(267, 43, 157, 23);
		getContentPane().add(btnCancelBuy);
		
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
		
		List<Producto> list = ClienteController.listProductosComprados(dni);
		
		if(list==null) {
			JOptionPane.showMessageDialog(parent, "Se ha perdido la conexión con la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}
		
		list.forEach((it)->{dtm.addRow(new String[] {String.valueOf(it.getProducto_ID()), it.getNombre(), String.valueOf(it.getPrecio()), it.getProveedor_dni()});});
		
		table.setModel(dtm);
	}

}
