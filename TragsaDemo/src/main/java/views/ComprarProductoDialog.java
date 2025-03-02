package views;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controllers.ClienteController;
import controllers.ProductoController;
import models.Producto;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ComprarProductoDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private ComprarProductoDialog self;
	private JTable table;

	private JDialog parent;

	public ComprarProductoDialog(JDialog parent, String dni) {
		super(parent, true);
		setResizable(false);
		setTitle("Comprar Producto");
		setBounds(100, 100, 450, 300);
		this.parent = parent;
		getContentPane().setLayout(null);

		table = new JTable();
		table.setBounds(0, 75, 434, 175);
		getContentPane().add(table);

		JButton btnComprar = new JButton("Comprar");
		btnComprar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compraProducto(dni);
			}
		});
		btnComprar.setBounds(335, 41, 89, 23);
		getContentPane().add(btnComprar);

		self = this;
		refreshTable();

	}

	
	//Refresca la tabla de datos. Se realiza al inicio y después de potenciales cambios en los datos
	private void refreshTable() {
		String[] cols = new String[] { "id", "nombre", "precio", "proveedor" };

		DefaultTableModel dtm = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		List<Producto> list = ProductoController.readAllProductosSinComprar();

		if (list == null) {
			JOptionPane.showMessageDialog(parent, "Se ha perdido la conexión con la base de datos", "Error",
					JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}
		

			list.forEach((it) -> {
				dtm.addRow(new String[] { String.valueOf(it.getProducto_ID()), it.getNombre(),
						String.valueOf(it.getPrecio()), it.getProveedor_dni() });
			});

		table.setModel(dtm);
		
		if (list.isEmpty()) {
			JOptionPane.showMessageDialog(parent, "No hay productos disponibles para comprar", "Error",
					JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}
		
	}
	
	
	//Asigna el cliente al producto seleccionado
	private void compraProducto(String dni) {
		int id = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());

		if (ClienteController.comprarProducto(dni, id)) {
			JOptionPane.showMessageDialog(self, "Producto comprado con éxito", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			refreshTable();
		}

		else {
			JOptionPane.showMessageDialog(self, "Error al comprar el producto", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

}
