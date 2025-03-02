package views;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.ProductoController;
import controllers.UsuarioController;
import models.Producto;
import models.usuarios.Usuario;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddProductoDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	private JDialog parent;
	private JTextField txtNombre;
	private JFormattedTextField txtPrecio = new JFormattedTextField(0.00);
	private AddProductoDialog self;

	public AddProductoDialog(JDialog parent) {
		super(parent, true);
		setResizable(false);
		self = this;
		setTitle("Añadir Producto");
		setBounds(100, 100, 450, 300);
		this.parent = parent;
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JButton okButton = new JButton("Aceptar");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addProducto();

				}
			});
			okButton.setActionCommand("OK");
			okButton.setBounds(230, 30, 87, 23);
			contentPanel.add(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancelar");
			cancelButton.setActionCommand("Cancel");
			cancelButton.setBounds(337, 30, 87, 23);
			contentPanel.add(cancelButton);
		}
		{
			table = new JTable();
			table.setBounds(0, 91, 434, 170);
			contentPanel.add(table);
		}

		txtNombre = new JTextField();
		txtNombre.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(txtNombre.getText().length()>49 && !e.isActionKey()) {
					e.consume();
				}
			}
		});
		txtNombre.setBounds(80, 8, 140, 20);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setBounds(10, 11, 60, 14);
		contentPanel.add(lblNewLabel);
		txtPrecio.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if((e.getKeyChar()<'0' || e.getKeyChar()>'9') && !(e.getKeyChar()=='.' && !txtPrecio.getText().contains(".")) && !e.isActionKey()) {
					e.consume();
				}
			}
		});

		txtPrecio.setBounds(80, 39, 140, 20);
		contentPanel.add(txtPrecio);

		JLabel lblPrecio = new JLabel("Precio");
		lblPrecio.setBounds(10, 42, 60, 14);
		contentPanel.add(lblPrecio);

		refreshTable();

	}

	//Refresca la tabla de datos. Se realiza al inicio y después de potenciales cambios en los datos
	private void refreshTable() {
		String[] cols = { "DNI", "Nombre" };
		DefaultTableModel dtm = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		List<Usuario> list = UsuarioController.readAllUsuarios(false, true);

		if (list == null) {
			JOptionPane.showMessageDialog(parent, "Se ha perdido la conexión con la base de datos", "Error",
					JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}

		if (list.isEmpty()) {
			JOptionPane.showMessageDialog(parent, "No hay proveedores disponibles para suministrar el producto",
					"Error", JOptionPane.WARNING_MESSAGE);
			self.dispose();
		}

		list.forEach((it) -> {
			dtm.addRow(new String[] { it.getDNI(), it.getNombre() });
		});
		table.setModel(dtm);

		table.getSelectionModel().setSelectionInterval(0, 0);

	}
	
	
	//Añade un producto a la base de datos
	private void addProducto() {
		
		String nombre = txtNombre.getText().strip();
		
		if(nombre.isBlank()) {
			JOptionPane.showMessageDialog(self, "No se puede crear un producto sin nombre", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String dni = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
		if (ProductoController.createUpdateProducto(
				new Producto(nombre, Double.parseDouble(txtPrecio.getText()), dni))) {
			JOptionPane.showMessageDialog(self, "Producto creado con éxito", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			self.dispose();
		}

		else {
			JOptionPane.showMessageDialog(self, "Error al crear el producto", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
