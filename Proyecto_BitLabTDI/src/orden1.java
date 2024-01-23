import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class orden1 extends javax.swing.JFrame {

    boolean SeEncuentraOrden = false;
    String NumeroOrdenEncontrada;

    public orden1() {
        initComponents();

        this.setExtendedState(MAXIMIZED_BOTH);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int ancho = (int) d.getWidth();
        int alto = (int) d.getHeight();
        jScrollPane1.setSize(ancho, alto);
        this.setTitle("ORDEN DE SERVICIO");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        lbl10.setText(String.valueOf(dtf.format(LocalDateTime.now())));
        setIconImage(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.jpeg")).getImage());

        login btn = new login();
        btneliminar.setIcon(btn.setIcono("/imagenes/eliminar.png", btneliminar));
        btneliminar.setPressedIcon(btn.setIconoPresionado("/imagenes/eliminar.png", btneliminar, 10, 10));

        btnterminar.setIcon(btn.setIcono("/imagenes/finalizar.png", btnterminar));
        btnterminar.setPressedIcon(btn.setIconoPresionado("/imagenes/finalizar.png", btnterminar, 10, 10));

        btnPdf.setIcon(btn.setIcono("/imagenes/pdf.png", btnPdf));
        btnPdf.setPressedIcon(btn.setIconoPresionado("/imagenes/pdf.png", btnPdf, 10, 10));

        btneditar.setIcon(btn.setIcono("/imagenes/edita.png", btneditar));
        btneditar.setPressedIcon(btn.setIconoPresionado("/imagenes/edita.png", btneditar, 10, 10));

        jButton3.setIcon(btn.setIcono("/imagenes/cerrarsesion.png", jButton3));
        jButton3.setPressedIcon(btn.setIconoPresionado("/imagenes/cerrarsesion.png", jButton3, 10, 10));
    }

    public static void getID() {
        String datos[] = new String[11];
        login connect = new login();
        try {
            try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
                String sql = "SELECT id_orden , id_departamento, fechacrea, fechacierre, total, partes, status, diagnostico, tip_pago, descuentos from orden WHERE id_dispo = " + txtid_dispo.getText();
                ResultSet res = st.executeQuery(sql);
                while (res.next()) {
                    datos[0] = res.getString("id_orden");
                    datos[1] = res.getString("id_departamento");
                    datos[2] = res.getString("fechacrea");
                    datos[3] = res.getString("fechacierre");
                    datos[4] = res.getString("total");
                    datos[5] = res.getString("partes");
                    datos[6] = res.getString("status");
                    datos[7] = res.getString("diagnostico");
                    datos[8] = res.getString("tip_pago");
                    datos[9] = res.getString("descuentos");
                }

                if (datos[0] == null) {
                    try (java.sql.Statement stB = connect.obtenConexion().createStatement()) {
                        String sqly = "SELECT COALESCE(MAX(id_orden), 0) + 1 AS siguiente_id FROM orden";
                        ResultSet result = stB.executeQuery(sqly);
                        while (result.next()) {
                            datos[10] = result.getString("siguiente_id");
                            txtdescripcion.setText("N/A");
                            txtprecio.setText("0.0");
                            btnAgregarCosto.doClick();
                            btnterminar.doClick();
                        }
                    } catch (Exception e) {
                    }
                    txtid_orden.setText(datos[10]);
                } else {
                    int id = Integer.parseInt(datos[0]);
                    int ultimo = id;
                    String ult = String.valueOf(ultimo);
                    txtid_orden.setText(ult);
                    txttotalf.setText(datos[4]);
                    txtpartes.setText(datos[5]);
                    txtdiagnostico.setText(datos[7]);
                    txtdescuentos.setText("0");
                    cb.setSelectedItem(datos[6]);
                    int index = Integer.parseInt(datos[1]);
                    cb2.setSelectedIndex(index - 1);
                    cb3.setSelectedItem(datos[8]);
                    txtdescripcion.setText("N/A");
                    txtprecio.setText("0");
                    btnAgregarCosto.doClick();
                    btnterminar.doClick();
                }
                connect.obtenConexion().close();
            }
        } catch (NumberFormatException | SQLException e) {
        }
    }

    protected void insertarCosto() {
        DefaultTableModel modelo = (DefaultTableModel) tabla_precios.getModel();
        String info[] = new String[6];
        info[0] = txtdescripcion.getText();
        if (btniva.isSelected()) {
            BigDecimal aux = new BigDecimal(txtprecio.getText());
            BigDecimal res1 = new BigDecimal(".16");
            BigDecimal res = res1.multiply(aux);
            BigDecimal fin = aux.add(res);
            String ultimo = fin.toPlainString();
            info[1] = ultimo;
            info[2] = "Incluido";
        } else {
            info[1] = txtprecio.getText();
            info[2] = "No incluido";
        }
        modelo.addRow(info);
        txtdescripcion.setText(null);
        txtprecio.setText(null);
    }

    protected void sumarColumna() {
        DefaultTableModel modelo = (DefaultTableModel) tabla_precios.getModel();
        DecimalFormat formato2 = new DecimalFormat("#.##");
        double fila = 0;
        double total = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            fila = Double.parseDouble(modelo.getValueAt(i, 1).toString());
            total += fila;
        }

        String str = Double.toString(total);
        txttotal.setText(String.valueOf(String.format("%.2f", total)));
    }

    protected void sumarColumna2() {
        DefaultTableModel modelo = (DefaultTableModel) tabla2.getModel();
        DecimalFormat formato2 = new DecimalFormat("#.##");
        double fila = 0;
        double total = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            fila = Double.parseDouble(modelo.getValueAt(i, 1).toString());
            total += fila;
        }
        int dis2 = tabla2.getRowCount();
        int dis = dispositvos.tabla.getRowCount();
        if (dis >= 3) {
            double des = Double.parseDouble(txtdescuentos.getText());
            double aux = des + total * 0.10;
            double descuento = total - aux;
            txtdescuentos.setText(String.valueOf(String.format("%.2f", aux)));
            String str = Double.toString(total);
            txttotalf.setText(String.valueOf(String.format("%.2f", descuento)));
        } else if (dis2 >= 3) {
            double des = Double.parseDouble(txtdescuentos.getText());
            double aux = des + total * 0.10;
            double descuento = total - aux;
            txtdescuentos.setText(String.valueOf(String.format("%.2f", aux)));
            String str = Double.toString(total);
            txttotalf.setText(String.valueOf(String.format("%.2f", descuento)));
        } else if (dis >= 3 && dis2 >= 3) {
            double des = Double.parseDouble(txtdescuentos.getText());
            double aux = des + total * 0.20;
            double descuento = total - aux;
            txtdescuentos.setText(String.valueOf(String.format("%.2f", aux)));
            String str = Double.toString(total);
            txttotalf.setText(String.valueOf(String.format("%.2f", descuento)));
        } else {
            txttotalf.setText(String.valueOf(String.format("%.2f", total)));
        }
    }

    protected void guardarOrden() {
        login connect = new login();
        String datos[] = new String[1];

        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select id_orden FROM orden WHERE id_dispo = '" + txtid_dispo.getText() + "'";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("id_orden");
                if (null != datos[0]) {
                    SeEncuentraOrden = true;
                    NumeroOrdenEncontrada = datos[0];
                    String query = "UPDATE orden SET  id_departamento = ?, fechacierre = ?, total = ?, partes = ?, status = ?, diagnostico = ?, tip_pago = ?, descuentos = ? WHERE id_dispo = " + txtid_dispo.getText();
                    try (PreparedStatement instruccion = connect.obtenConexion().prepareStatement(query)) {

                        String dateString = lbl10.getText(); // Suponiendo que esta es tu fecha en formato de cadena
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); // Ajusta el formato según tu cadena
                        java.util.Date parsedDate = dateFormat.parse(dateString);
                        Date sqlDate = new Date(parsedDate.getTime());
                        String descuentoStr = txtdescuentos.getText();
                        BigDecimal descuento = new BigDecimal(descuentoStr);
                        String totalfStr1 = txttotalf.getText();
                        BigDecimal totalf = new BigDecimal(totalfStr1);

                        instruccion.setInt(1, cb2.getSelectedIndex() + 1);
                        instruccion.setDate(2, sqlDate);
                        instruccion.setBigDecimal(3, totalf);
                        instruccion.setString(4, txtpartes.getText());
                        instruccion.setString(5, cb.getSelectedItem().toString());
                        instruccion.setString(6, txtdiagnostico.getText());
                        instruccion.setString(7, cb3.getSelectedItem().toString());
                        instruccion.setBigDecimal(8, (descuento));
                        instruccion.executeUpdate();
                        connect.obtenConexion().close();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error " + e);
                    }

                }
            }
            connect.obtenConexion().close();
            if (null == datos[0]) {
                try {
                    try (java.sql.Statement st1 = connect.obtenConexion().createStatement()) {
                        String sql1 = "insert into orden (id_dispo, id_cliente, fechacrea, fechacierre, partes, diagnostico, status, id_departamento, descuentos, tip_pago, total)"
                                + "values (" + txtid_dispo.getText() + "," + txtid_cliente.getText() + ",'" + lbl10.getText() + "','" + lbl10.getText() + "'"
                                + ",'" + txtpartes.getText() + "','" + txtdiagnostico.getText() + "','" + cb.getSelectedItem().toString() + "'," + cb2.getSelectedIndex() + 1 + "," + txtdescuentos.getText() + ",'" + cb3.getSelectedItem().toString() + "'," + txttotalf.getText() + ")";
                        st1.executeUpdate(sql1);
                        connect.obtenConexion().close();
                    }
                } catch (SQLException e) {
                }
            }
            st.executeUpdate(sql);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    protected void descuento1() {
        DefaultTableModel modelo = (DefaultTableModel) tabla_precios.getModel();
        DecimalFormat formato2 = new DecimalFormat("#.##");
        double fila = 0;
        double total = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            fila = Double.parseDouble(modelo.getValueAt(i, 1).toString());
            total += fila;
        }
        int dis = dispositvos.tabla.getRowCount();
        if (dis >= 3) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txttotalf = new javax.swing.JTextField();
        lbl10 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lb9 = new javax.swing.JLabel();
        txtid_cliente = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        cb2 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cb3 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        txtdescuentos = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbl9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtid20 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtid21 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jLabel30 = new javax.swing.JLabel();
        txtid_dispo = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtid_orden = new javax.swing.JTextField();
        cb = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla2 = new javax.swing.JTable();
        btneditar = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtdiagnostico = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtpartes = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbl11 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtprecio = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btniva = new javax.swing.JToggleButton();
        btnAgregarCosto = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextArea();
        txttotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_precios = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        btnterminar = new javax.swing.JButton();
        btnPdf = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(62, 95, 138));
        jPanel1.setPreferredSize(new java.awt.Dimension(1358, 1050));

        jPanel2.setBackground(new java.awt.Color(87, 131, 188));
        jPanel2.setPreferredSize(new java.awt.Dimension(962, 640));
        jPanel2.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ORDEN DE SERVICIO");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(30, 30, 270, 32);

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("ID - Dispositivo");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(210, 90, 160, 28);

        jLabel16.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Tipo pago");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(380, 690, 120, 28);

        jLabel17.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Diagnostico general");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(310, 210, 230, 28);

        txttotalf.setBackground(new java.awt.Color(240, 240, 240));
        txttotalf.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txttotalf.setEnabled(false);
        jPanel2.add(txttotalf);
        txttotalf.setBounds(400, 810, 120, 47);

        lbl10.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        lbl10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(lbl10);
        lbl10.setBounds(320, 30, 210, 40);

        jLabel22.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("ID - Cliente");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(390, 90, 140, 28);

        lb9.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        lb9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(lb9);
        lb9.setBounds(400, 20, 160, 40);

        txtid_cliente.setBackground(new java.awt.Color(240, 240, 240));
        txtid_cliente.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid_cliente.setEnabled(false);
        jPanel2.add(txtid_cliente);
        txtid_cliente.setBounds(390, 130, 140, 47);

        jLabel26.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Departamento");
        jPanel2.add(jLabel26);
        jLabel26.setBounds(300, 380, 150, 28);

        cb2.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        cb2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "iLabTDI", "Reparaciones", "Análisis", "Recepción", "Otros" }));
        cb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb2ActionPerformed(evt);
            }
        });
        jPanel2.add(cb2);
        cb2.setBounds(300, 410, 230, 50);

        jLabel18.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Estatus");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(40, 380, 90, 28);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(762, 400, 480, 130);

        jLabel19.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Costos");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(50, 480, 90, 28);

        jLabel20.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Total Final.");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(400, 770, 120, 28);

        cb3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Efectivo", "Tarjeta", "Transferencia", "Pay-Pal", "Otro" }));
        cb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb3ActionPerformed(evt);
            }
        });
        jPanel2.add(cb3);
        cb3.setBounds(380, 720, 120, 40);

        jLabel21.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Descuentos");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(220, 690, 120, 28);

        txtdescuentos.setBackground(new java.awt.Color(240, 240, 240));
        txtdescuentos.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtdescuentos.setText("0");
        txtdescuentos.setEnabled(false);
        jPanel2.add(txtdescuentos);
        txtdescuentos.setBounds(220, 720, 120, 40);

        jPanel3.setBackground(new java.awt.Color(87, 131, 188));
        jPanel3.setPreferredSize(new java.awt.Dimension(962, 640));
        jPanel3.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("COSTOS");
        jPanel3.add(jLabel1);
        jLabel1.setBounds(30, 30, 270, 32);

        lbl9.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        lbl9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(lbl9);
        lbl9.setBounds(550, 20, 170, 30);

        jLabel23.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("IVA");
        jPanel3.add(jLabel23);
        jLabel23.setBounds(350, 180, 50, 28);

        txtid20.setBackground(new java.awt.Color(240, 240, 240));
        txtid20.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid20.setEnabled(false);
        jPanel3.add(txtid20);
        txtid20.setBounds(30, 110, 190, 150);

        jLabel24.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Descripción");
        jPanel3.add(jLabel24);
        jLabel24.setBounds(30, 80, 170, 28);

        txtid21.setBackground(new java.awt.Color(240, 240, 240));
        txtid21.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid21.setEnabled(false);
        jPanel3.add(txtid21);
        txtid21.setBounds(300, 120, 160, 47);

        jLabel25.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Precio");
        jPanel3.add(jLabel25);
        jLabel25.setBounds(300, 80, 170, 28);

        jToggleButton5.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jToggleButton5.setForeground(new java.awt.Color(25, 55, 87));
        jToggleButton5.setText("Agregar costo");
        jPanel3.add(jToggleButton5);
        jToggleButton5.setBounds(150, 280, 210, 40);

        jToggleButton8.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jToggleButton8.setForeground(new java.awt.Color(25, 55, 87));
        jToggleButton8.setText("Activo");
        jPanel3.add(jToggleButton8);
        jToggleButton8.setBounds(320, 220, 100, 40);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(760, 40, 480, 330);

        jLabel30.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Partes utilizadas");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(40, 210, 180, 28);

        txtid_dispo.setBackground(new java.awt.Color(240, 240, 240));
        txtid_dispo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid_dispo.setEnabled(false);
        jPanel2.add(txtid_dispo);
        txtid_dispo.setBounds(210, 130, 140, 47);

        jLabel31.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("ID - Orden");
        jPanel2.add(jLabel31);
        jLabel31.setBounds(40, 90, 120, 28);

        txtid_orden.setBackground(new java.awt.Color(240, 240, 240));
        txtid_orden.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid_orden.setEnabled(false);
        jPanel2.add(txtid_orden);
        txtid_orden.setBounds(40, 130, 140, 47);

        cb.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recibido", "Pendiente", "Reparado", "No Reparado", "Traer Después", "Revisado", "Otro" }));
        jPanel2.add(cb);
        cb.setBounds(40, 410, 210, 50);

        tabla2.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        tabla2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción", "Precio", "IVA"
            }
        ));
        tabla2.setRowHeight(24);
        jScrollPane5.setViewportView(tabla2);
        if (tabla2.getColumnModel().getColumnCount() > 0) {
            tabla2.getColumnModel().getColumn(0).setMinWidth(200);
        }

        jPanel2.add(jScrollPane5);
        jScrollPane5.setBounds(40, 520, 490, 160);

        btneditar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btneditar.setForeground(new java.awt.Color(25, 55, 87));
        btneditar.setToolTipText("Editar");
        btneditar.setBorderPainted(false);
        btneditar.setContentAreaFilled(false);
        btneditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btneditar.setEnabled(false);
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });
        jPanel2.add(btneditar);
        btneditar.setBounds(90, 690, 70, 70);

        txtdiagnostico.setColumns(20);
        txtdiagnostico.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtdiagnostico.setRows(5);
        txtdiagnostico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdiagnosticoKeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(txtdiagnostico);

        jPanel2.add(jScrollPane6);
        jScrollPane6.setBounds(310, 250, 220, 110);

        txtpartes.setColumns(20);
        txtpartes.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtpartes.setRows(5);
        txtpartes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtpartesKeyTyped(evt);
            }
        });
        jScrollPane7.setViewportView(txtpartes);

        jPanel2.add(jScrollPane7);
        jScrollPane7.setBounds(40, 250, 220, 110);

        jPanel4.setBackground(new java.awt.Color(87, 131, 188));
        jPanel4.setPreferredSize(new java.awt.Dimension(962, 640));
        jPanel4.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("COSTOS");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(30, 30, 270, 32);

        lbl11.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        lbl11.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(lbl11);
        lbl11.setBounds(550, 20, 170, 30);

        jLabel27.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("IVA");
        jPanel4.add(jLabel27);
        jLabel27.setBounds(350, 180, 50, 28);

        jLabel28.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Descripción");
        jPanel4.add(jLabel28);
        jLabel28.setBounds(30, 80, 170, 28);

        txtprecio.setBackground(new java.awt.Color(240, 240, 240));
        txtprecio.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtprecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtprecioKeyTyped(evt);
            }
        });
        jPanel4.add(txtprecio);
        txtprecio.setBounds(290, 120, 160, 47);

        jLabel29.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Precio");
        jPanel4.add(jLabel29);
        jLabel29.setBounds(300, 80, 170, 28);

        btniva.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btniva.setForeground(new java.awt.Color(25, 55, 87));
        btniva.setText("Agregar");
        jPanel4.add(btniva);
        btniva.setBounds(300, 220, 130, 40);

        btnAgregarCosto.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnAgregarCosto.setForeground(new java.awt.Color(25, 55, 87));
        btnAgregarCosto.setText("Agregar costo");
        btnAgregarCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCostoActionPerformed(evt);
            }
        });
        jPanel4.add(btnAgregarCosto);
        btnAgregarCosto.setBounds(130, 280, 230, 40);

        txtdescripcion.setColumns(20);
        txtdescripcion.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtdescripcion.setRows(5);
        jScrollPane2.setViewportView(txtdescripcion);

        jPanel4.add(jScrollPane2);
        jScrollPane2.setBounds(20, 110, 220, 140);

        txttotal.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txttotal.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TOTAL");

        tabla_precios.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        tabla_precios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción", "Precio", "IVA"
            }
        ));
        tabla_precios.setRowHeight(24);
        jScrollPane4.setViewportView(tabla_precios);
        if (tabla_precios.getColumnModel().getColumnCount() > 0) {
            tabla_precios.getColumnModel().getColumn(0).setMinWidth(200);
        }

        jButton3.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(25, 55, 87));
        jButton3.setToolTipText("Regresar");
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnterminar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnterminar.setForeground(new java.awt.Color(25, 55, 87));
        btnterminar.setToolTipText("Terminar");
        btnterminar.setBorderPainted(false);
        btnterminar.setContentAreaFilled(false);
        btnterminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnterminar.setEnabled(false);
        btnterminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnterminarActionPerformed(evt);
            }
        });

        btnPdf.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        btnPdf.setForeground(new java.awt.Color(25, 55, 87));
        btnPdf.setToolTipText("Generar orden");
        btnPdf.setBorderPainted(false);
        btnPdf.setContentAreaFilled(false);
        btnPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        btneliminar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(25, 55, 87));
        btneliminar.setToolTipText("Eliminar");
        btneliminar.setBorderPainted(false);
        btneliminar.setContentAreaFilled(false);
        btneliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btneliminar.setEnabled(false);
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(btnterminar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 439, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnterminar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46))
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(140, 140, 140))
        );

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, -60, 1360, 1000);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispositvos ven = new dispositvos();
        clientes ven1 = new clientes();
        ven1.insertarTabla2(txtid_cliente.getText());
        dispositvos.txtidcliente.setText(txtid_cliente.getText());
        ven.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtprecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtprecioKeyTyped
        char tecla;
        tecla = evt.getKeyChar();
        String sueldo = txtprecio.getText();
        int punto = sueldo.indexOf(".") + 1;
        if (punto == 0) {
            if (!Character.isDigit(tecla) && tecla != KeyEvent.VK_BACK_SPACE && tecla != KeyEvent.VK_PERIOD) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Solo números.", "", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (!Character.isDigit(tecla) && tecla != KeyEvent.VK_BACK_SPACE) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Solo números.", "", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtprecioKeyTyped

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        String rutaCarpeta;
        if (txtpartes.getText().isEmpty() || txttotalf.getText().isEmpty() || txtdiagnostico.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            String datos[] = new String[8];
            login connect = new login();
            try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
                String sql = "SELECT * FROM cliente WHERE id_cliente = " + txtid_cliente.getText();
                ResultSet res = st.executeQuery(sql);
                while (res.next()) {
                    datos[0] = res.getString("nombre");
                    datos[1] = res.getString("direccion");
                    datos[2] = res.getString("colonia");
                    datos[3] = res.getString("ciudad");
                    datos[4] = res.getString("cp");
                    datos[5] = res.getString("correo");
                    datos[6] = res.getString("telefono");
                    datos[7] = res.getString("id_cliente");
                }
                st.executeUpdate(sql);
                connect.obtenConexion().close();
            } catch (Exception e) {
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                rutaCarpeta = chooser.getSelectedFile().getAbsolutePath();
                Document documento = new Document();
                if (rutaCarpeta.isEmpty()) {
                } else {
                    try {
                        //tring ruta = System.getProperty("user.home");
                        PdfWriter.getInstance(documento, new FileOutputStream(rutaCarpeta + "/OS " + txtid_orden.getText() + " - " + datos[0] + ".pdf"));
                        documento.open();

                        URL urlImagen = orden1.class.getClassLoader().getResource("IMG/BITLABTDI.png");
                        if (urlImagen != null) {
                            Image imagen = null;
                            try {
                                imagen = Image.getInstance(urlImagen);
                            } catch (BadElementException ex) {
                                Logger.getLogger(orden1.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(orden1.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            // Establecer la posición de la imagen en el PDF (coordenadas x, y)
                            imagen.setAbsolutePosition(50, 750);
                            // Escalar la imagen si es necesario (opcional)
                            imagen.scaleAbsolute(65, 65); // Anchura x Altura
                            // Agregar la imagen al documento
                            documento.add(imagen);

                            Paragraph p1 = new Paragraph();
                            Paragraph p2 = new Paragraph();
                            Paragraph p3 = new Paragraph();

                            Font font = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
                            Font font01 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
                            Font font012 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK);
                            Font font0 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.RED);
                            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
                            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC, BaseColor.BLACK);

                            Phrase title = new Phrase("                           ", font);
                            Phrase titlea = new Phrase("facebook.com/ILabTDI                    "
                                    + "                              ", font01);
                            //Phrase titleas = new Phrase("\nSucursal.\n", font01);
                            Phrase titlea1 = new Phrase("\nBlvd. Marcelino García Barragán #1421, esq Calzada Olímpica.\n"
                                    + "Guadalajara, Jal, México, C.P. 44430.\n"
                                    + "Tel. (33) 3025-8430.\n\n", font012);
                            Phrase titleo = new Phrase("OS # ", font1);
                            Phrase titleos = new Phrase(txtid_orden.getText(), font0);

                            Phrase title1 = new Phrase("\n" + datos[0] + "  ", font1);
                            Phrase title2 = new Phrase("Mail: ", font1);
                            Phrase title3 = new Phrase(datos[5] + "  ", font2);
                            Phrase title4 = new Phrase("Tel: ", font1);
                            Phrase title5 = new Phrase(datos[6] + "  ", font2);
                            Phrase title6 = new Phrase("Cliente: ", font1);
                            Phrase title7 = new Phrase(datos[7] + "\n", font2);

                            Phrase title8 = new Phrase("Dirección: ", font1);
                            Phrase title9 = new Phrase(datos[1] + " ", font2);
                            Phrase title10 = new Phrase("Colonia: ", font1);
                            Phrase title11 = new Phrase(datos[2] + " ", font2);
                            Phrase title12 = new Phrase("Ciudad: ", font1);
                            Phrase title13 = new Phrase(datos[3] + " ", font2);
                            Phrase title14 = new Phrase("C.P ", font1);
                            Phrase title15 = new Phrase(datos[4] + " ", font2);

                            p1.add(title);
                            p1.add(titlea);
                            p1.add(titleo);
                            p1.add(titleos);
                            //p1.add(titleas);
                            p1.add(titlea1);
                            p1.setAlignment(Element.ALIGN_CENTER);
                            documento.add(p1);

                            p2.add(title1);
                            p2.add(title2);
                            p2.add(title3);
                            p2.add(title4);
                            p2.add(title5);
                            p2.add(title6);
                            p2.add(title7);
                            p2.setAlignment(Element.ALIGN_LEFT);
                            documento.add(p2);

                            p3.add(title8);
                            p3.add(title9);
                            p3.add(title10);
                            p3.add(title11);
                            p3.add(title12);
                            p3.add(title13);
                            p3.add(title14);
                            p3.add(title15);
                            p3.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(p3);

                            String datosDis[] = new String[10];
                            try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
                                String sql = "SELECT * FROM dispositivo WHERE id_dispo = " + txtid_dispo.getText();
                                ResultSet res = st.executeQuery(sql);
                                while (res.next()) {
                                    datosDis[0] = res.getString("id_dispo");
                                    datosDis[1] = res.getString("sn");
                                    datosDis[2] = res.getString("tipo_dis");
                                    datosDis[3] = res.getString("modelo");
                                    datosDis[4] = res.getString("estado_fisi");
                                    datosDis[5] = res.getString("color");
                                    datosDis[6] = res.getString("marca");
                                    datosDis[7] = res.getString("caso");
                                    datosDis[8] = res.getString("fecha");
                                    datosDis[9] = res.getString("inventario");
                                }
                                st.executeUpdate(sql);
                                connect.obtenConexion().close();
                            } catch (Exception e) {
                            }

                            Paragraph new0 = new Paragraph();

                            Phrase ren0 = new Phrase("\nDispositivo : #", font1);
                            Phrase ren1 = new Phrase(datosDis[0] + "  ", font2);

                            Phrase ren2 = new Phrase("Tipo: ", font1);
                            Phrase ren3 = new Phrase(datosDis[2] + "  ", font2);

                            Phrase ren4 = new Phrase("Modelo: ", font1);
                            Phrase ren5 = new Phrase(datosDis[3] + "  ", font2);

                            Phrase ren6 = new Phrase("S/N: ", font1);
                            Phrase ren7 = new Phrase(datosDis[1] + "  ", font2);

                            Phrase ren8 = new Phrase("Marca: ", font1);
                            Phrase ren9 = new Phrase(datosDis[6] + "  ", font2);

                            Phrase ren10 = new Phrase("Color: ", font1);
                            Phrase ren11 = new Phrase(datosDis[5] + "  ", font2);

                            Phrase ren12 = new Phrase("Inventario: ", font1);
                            Phrase ren13 = new Phrase(datosDis[9] + "  ", font2);

                            Phrase ren14 = new Phrase("Estado Físico: ", font1);
                            Phrase ren15 = new Phrase(datosDis[4] + "  ", font2);

                            Phrase ren16 = new Phrase("Estatus: ", font1);
                            Phrase ren17 = new Phrase(cb.getSelectedItem() + "  ", font2);

                            Phrase ren18 = new Phrase("Departamento: ", font1);
                            Phrase ren19 = new Phrase(cb2.getSelectedItem() + "  ", font2);

                            Phrase ren20 = new Phrase("Fecha: ", font1);
                            Phrase ren21 = new Phrase(lbl10.getText() + "  ", font2);

                            Phrase ren22 = new Phrase("\n\nDescripción: ", font1);
                            Phrase ren23 = new Phrase(datosDis[7] + "  ", font2);

                            Phrase ren24 = new Phrase("\n\nSolución: ", font1);
                            Phrase ren25 = new Phrase(txtdiagnostico.getText() + "  ", font2);

                            Phrase ren26 = new Phrase("\n\nCostos: \n ", font1);

                            new0.add(ren0);
                            new0.add(ren1);
                            new0.add(ren2);
                            new0.add(ren3);
                            new0.add(ren4);
                            new0.add(ren5);
                            new0.add(ren6);
                            new0.add(ren7);
                            new0.add(ren8);
                            new0.add(ren9);
                            new0.add(ren10);
                            new0.add(ren11);
                            new0.add(ren12);
                            new0.add(ren13);
                            new0.add(ren14);
                            new0.add(ren15);
                            new0.add(ren16);
                            new0.add(ren17);
                            new0.add(ren18);
                            new0.add(ren19);
                            new0.add(ren20);
                            new0.add(ren21);
                            new0.add(ren22);
                            new0.add(ren23);
                            new0.add(ren24);
                            new0.add(ren25);
                            new0.add(ren26);
                            new0.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(new0);

                            Font fuente = new Font(Font.FontFamily.COURIER, 11, Font.BOLDITALIC, BaseColor.BLACK);
                            PdfPTable tabla5 = new PdfPTable(3);
                            PdfPCell c1 = new PdfPCell(new Phrase("Descripción", fuente));
                            PdfPCell c2 = new PdfPCell(new Phrase("Precio", fuente));
                            PdfPCell c3 = new PdfPCell(new Phrase("IVA", fuente));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            tabla5.addCell(c1);
                            tabla5.addCell(c2);
                            tabla5.addCell(c3);
                            DefaultTableModel modelo2 = (DefaultTableModel) tabla2.getModel();

                            for (int i = 0; i < modelo2.getRowCount(); i++) {
                                Object f[] = new Object[modelo2.getColumnCount()];
                                for (int j = 0; j < modelo2.getColumnCount(); j++) {
                                    f[j] = modelo2.getValueAt(i, j);
                                    tabla5.addCell((String) f[j]);
                                }
                            }
                            documento.add(tabla5);

                            Paragraph new1 = new Paragraph();
                            Paragraph new2 = new Paragraph();
                            Paragraph new3 = new Paragraph();
                            Paragraph new4 = new Paragraph();
                            Paragraph new5 = new Paragraph();
                            Paragraph new6 = new Paragraph();
                            Paragraph new7 = new Paragraph();
                            Paragraph new8 = new Paragraph();
                            Paragraph new9 = new Paragraph();
                            Paragraph new10 = new Paragraph();

                            Phrase reng0 = new Phrase("\n\nForma de pago: ", font1);
                            Phrase reng1 = new Phrase(cb3.getSelectedItem() + "  ", font2);

                            Phrase reng2 = new Phrase("\n\nDescuento: ", font1);
                            Phrase reng3 = new Phrase(txtdescuentos.getText() + "  ", font2);

                            Font fonte = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLDITALIC, BaseColor.BLACK);
                            Font fonte1 = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLDITALIC, BaseColor.BLUE);
                            Phrase reng4 = new Phrase("\nTotal: ", fonte);
                            Phrase reng5 = new Phrase("$ " + txttotalf.getText() + "  ", fonte1);

                            //Font funte = new Font(Font.FontFamily.COURIER, 12, Font.BOLD, BaseColor.BLACK);
                            //Phrase reng6 = new Phrase("\nEn caso de requerir refacciones se necesita pagar el 50% de anticipo. ", funte);
                            //Font fue = new Font(Font.FontFamily.COURIER, 11, Font.UNDERLINE, BaseColor.BLACK);
                            //Phrase reng7 = new Phrase("\nNOTA: ", funte);
                            //Phrase reng8 = new Phrase("EN CASO DE NO ACEPTAR EL PRESUPUESTO,SE HARÁ UN CARGO POR REVISIÓN DE :", fue);
                            Font fue1 = new Font(Font.FontFamily.COURIER, 14, Font.BOLD, BaseColor.BLACK);
                            //Phrase reng9 = new Phrase("$ 300.00" + "  ", fue1);

                            Phrase reng10 = new Phrase("\n\n\nTÉCNICO" + "                                        "
                                    + "                    ", font1);
                            Phrase reng11 = new Phrase("CLIENTE", font1);
                            Phrase reng12 = new Phrase("\n\n________________________________________" + "  ", font1);
                            Phrase reng13 = new Phrase("________________________________________" + "  ", font1);

                            Phrase reng14 = new Phrase(datos[0] + "  ", font1);

                            Font fueE = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
                            Phrase reng15 = new Phrase("\nNO NOS RESPONZABILIZAMOS después de "
                                    + "haber reparado o diagnosticado el equipo y notificado al cliente.", fueE);

                            Font fueñ = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
                            //Phrase reng16 = new Phrase("QUE NO TE CONFUNDAN:", fueñ);
                            Font fueñ1 = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.BLACK);
                            //Phrase reng17 = new Phrase("   Nuestros precios de reparación de laptops son accesibles, mejoramos cualquier cotización.", fueñ1);

                            Phrase reng18 = new Phrase("NOTA:   ", fueñ);
                            Phrase reng19 = new Phrase("Para prestarte un mejor servicio, favor de Leer las POLITICAS de SERVICIO y GARANTIA.\n"
                                    + "Reparamos7 COMPAQ,DELL,HP,GATEWAY,IBM,MAC,SONY etc", fueñ1);

                            /*
                            new1.add(reng0);
                            new1.add(reng1);
                            new1.add(reng2);
                            new1.add(reng3);
                            new1.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(new1);
                             */
                            new2.add(reng4);
                            new2.add(reng5);
                            new2.setAlignment(Element.ALIGN_RIGHT);
                            documento.add(new2);

                            /*
                        new3.add(reng6);
                        new3.setAlignment(Element.ALIGN_CENTER);
                        documento.add(new3);

                        new4.add(reng7);
                        new4.add(reng8);
                        new4.setAlignment(Element.ALIGN_LEFT);
                        documento.add(new4);
                        
                        new5.add(reng9);
                        new5.setAlignment(Element.ALIGN_RIGHT);
                        documento.add(new5);
                             */
                            new6.add(reng10);
                            new6.add(reng11);
                            new6.add(reng12);
                            new6.add(reng13);
                            new6.setAlignment(Element.ALIGN_CENTER);
                            documento.add(new6);

                            new7.add(reng14);
                            new7.setAlignment(Element.ALIGN_RIGHT);
                            documento.add(new7);

                            new8.add(reng15);
                            new8.setAlignment(Element.ALIGN_LEFT);
                            documento.add(new8);

                            /*
                        new9.add(reng16);
                        new9.add(reng17);
                        new9.setAlignment(Element.ALIGN_LEFT);
                        documento.add(new9);
                             */
                            new10.add(reng18);
                            new10.add(reng19);
                            new10.setAlignment(Element.ALIGN_LEFT);
                            documento.add(new10);

                            documento.newPage();
                            // Establecer la posición de la imagen en el PDF (coordenadas x, y)
                            imagen.setAbsolutePosition(50, 750);
                            // Escalar la imagen si es necesario (opcional)
                            imagen.scaleAbsolute(65, 65); // Anchura x Altura
                            // Agregar la imagen al documento
                            documento.add(imagen);

                            Paragraph parrafo1H2 = new Paragraph();
                            Paragraph parrafo2H2 = new Paragraph();
                            Paragraph parrafo3H2 = new Paragraph();
                            Paragraph parrafo4H2 = new Paragraph();

                            Paragraph parrafo5H2 = new Paragraph();
                            Paragraph parrafo6H2 = new Paragraph();
                            Paragraph parrafo7H2 = new Paragraph();
                            Paragraph parrafo8H2 = new Paragraph();

                            Paragraph parrafo9H2 = new Paragraph();
                            Paragraph parrafo10H2 = new Paragraph();
                            Paragraph parrafo11H2 = new Paragraph();
                            Paragraph parrafo12H2 = new Paragraph();

                            Paragraph parrafo13H2 = new Paragraph();
                            Paragraph parrafo14H2 = new Paragraph();
                            Paragraph parrafo15H2 = new Paragraph();
                            Paragraph parrafo16H2 = new Paragraph();

                            Paragraph parrafo17H2 = new Paragraph();
                            Paragraph parrafo18H2 = new Paragraph();
                            Paragraph parrafo19H2 = new Paragraph();
                            Paragraph parrafo20H2 = new Paragraph();

                            Font Fuente1H2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
                            Font Fuente2H2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
                            Font Fuente3H2 = new Font(Font.FontFamily.COURIER, 12, Font.BOLD, BaseColor.BLACK);

                            Font Fuente4H2 = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.BLACK);
                            Font Fuente5H2 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
                            Font Funete6H2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

                            Paragraph espacio = new Paragraph(20f, " ");
                            Paragraph espacio1 = new Paragraph(10f, " ");
                            Paragraph espacio2 = new Paragraph(4f, " ");

                            Phrase Frase1H2 = new Phrase("                                                  ");
                            Phrase Frase2H2 = new Phrase("Fecha: ", Fuente1H2);
                            Phrase Frase3H2 = new Phrase(lbl10.getText(), Fuente2H2);
                            Phrase Frase4H2 = new Phrase("\nPolíticas de Servicio.", Fuente3H2);
                            Phrase Frase5H2 = new Phrase("Reconozco y acepto las siguientes condiciones y términos relacionados con el servicio de reparación de computadoras ofrecido por el taller ", Fuente4H2);
                            Phrase Frase6H2 = new Phrase("BitLabTDI:", Fuente5H2);
                            Phrase Frase7H2 = new Phrase("• Servicio Gratuito: ", Funete6H2);
                            Phrase Frase8H2 = new Phrase("Entiendo que el servicio de reparación de computadora ofrecido es completamente gratuito y no conlleva ningún costo para el cliente.", Fuente4H2);
                            Phrase Frase9H2 = new Phrase("• Responsabilidad Limitada: ", Funete6H2);
                            Phrase Frase10H2 = new Phrase("Estoy consciente los técnicos se esforzarán por reparar mi equipo de la mejor manera posible. Sin embargo, comprendo que no se garantiza la reparación exitosa y que el taller no se hace responsable de cualquier daño adicional que pueda ocurrir durante el proceso de reparación.", Fuente4H2);
                            Phrase Frase11H2 = new Phrase("• Pérdida de Datos: ", Funete6H2);
                            Phrase Frase12H2 = new Phrase("Reconozco que el proceso de reparación puede resultar en la pérdida de datos. Es mi responsabilidad realizar copias de seguridad de cualquier información importante antes de entregar mi equipo para su reparación. El taller no se hace responsable de la pérdida de datos.", Fuente4H2);
                            Phrase Frase13H2 = new Phrase("• Consentimiento para Reparación: ", Funete6H2);
                            Phrase Frase14H2 = new Phrase("Autorizo al personal a realizar las reparaciones necesarias en mi equipo. Comprendo que el personal del taller puede abrir el equipo y realizar modificaciones para diagnosticar y solucionar problemas.", Fuente4H2);
                            Phrase Frase15H2 = new Phrase("• Recogida del Equipo: ", Funete6H2);
                            Phrase Frase16H2 = new Phrase("Acepto que seré responsable de recoger mi equipo reparado en el plazo especificado por el taller. Entiendo que, si no recojo mi equipo en el plazo indicado, el taller no se hace responsable de la seguridad o integridad de este.", Fuente4H2);
                            Phrase Frase17H2 = new Phrase("• Renuncia de Garantía: ", Funete6H2);
                            Phrase Frase18H2 = new Phrase("Estoy de acuerdo en que cualquier garantía o servicio de reparación que pueda haber estado en vigor previamente queda anulada mientras mi equipo esté siendo reparado por el taller de reparación de computadoras.", Fuente4H2);
                            Phrase Frase19H2 = new Phrase("• Información de Contacto: ", Funete6H2);
                            Phrase Frase20H2 = new Phrase("Aporto información de contacto precisa para que el taller pueda comunicarse conmigo sobre el estado de la reparación y otras cuestiones relacionadas con el servicio.", Fuente4H2);
                            Phrase Frase21H2 = new Phrase("• Fotos y Documentación: ", Funete6H2);
                            Phrase Frase22H2 = new Phrase("Acepto que el taller puede tomar fotografías del estado del equipo antes, durante y después del proceso de reparación con fines de documentación interna. Estas fotos serán utilizadas exclusivamente con el propósito de llevar un registro del servicio prestado.", Fuente4H2);
                            Phrase Frase23H2 = new Phrase("• Fallas Adicionales: ", Funete6H2);
                            Phrase Frase24H2 = new Phrase("En caso de que durante la revisión o reparación del equipo se identifique alguna falla adicional, se le informará al cliente sobre la naturaleza de dicha falla.", Fuente4H2);
                            Phrase Frase25H2 = new Phrase("• Accesorios: ", Funete6H2);
                            Phrase Frase26H2 = new Phrase("No asumimos responsabilidad por accesorios que no estén registrados en la hoja de orden de servicio. Esto incluye, pero no se limita a, cubiertas rotas, tapas rayadas, software y otros elementos que no estén explícitamente detallados en el registro de servicio.", Fuente4H2);
                            Phrase Frase27H2 = new Phrase("• Materiales para Reparación: ", Funete6H2);
                            Phrase Frase28H2 = new Phrase("En caso de que se requieran materiales específicos para llevar a cabo la reparación, estos deberán ser proporcionados por el usuario, a menos que se acuerde lo contrario de manera explícita y por escrito. El taller no se hará responsable de la adquisición de materiales necesarios para la reparación, a menos que se acuerde un arreglo diferente en la hoja de orden de servicio.", Fuente4H2);
                            Phrase Frase29H2 = new Phrase("• Garantía: ", Funete6H2);
                            Phrase Frase30H2 = new Phrase("Se informa al cliente que no se ofrece garantía para los servicios de reparación realizados. El taller no se responsabiliza de posibles fallos futuros o problemas relacionados con la reparación efectuada en el equipo. Al aceptar este servicio, el cliente comprende y acepta esta condición sin reservas.", Fuente4H2);
                            Phrase Frase31H2 = new Phrase("Al firmar esta responsiva, confirmo que he leído, comprendido y aceptado los términos y condiciones establecidos anteriormente. Además, libero al taller de reparación de computadoras, sus técnicos y la ", Fuente4H2);
                            Phrase Frase32H2 = new Phrase("BitLabTDI ", Fuente5H2);
                            Phrase Frase33H2 = new Phrase("de cualquier responsabilidad legal relacionada con el servicio de reparación de mi equipo.", Fuente4H2);
                            Phrase Frase34H2 = new Phrase("\n________________________________________", font1);
                            Phrase Frase35H2 = new Phrase("\n" + datos[0], font1);

                            parrafo1H2.add(Frase1H2);
                            parrafo1H2.add(Frase2H2);
                            parrafo1H2.add(Frase3H2);
                            parrafo1H2.setAlignment(Element.ALIGN_RIGHT);
                            documento.add(parrafo1H2);

                            parrafo2H2.add(Frase4H2);
                            parrafo2H2.setAlignment(Element.ALIGN_CENTER);
                            documento.add(parrafo2H2);

                            documento.add(espacio);
                            parrafo3H2.add(Frase5H2);
                            parrafo3H2.add(Frase6H2);
                            parrafo3H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo3H2);

                            documento.add(espacio1);
                            parrafo4H2.add(Frase7H2);
                            parrafo4H2.add(Frase8H2);
                            parrafo4H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo4H2);

                            documento.add(espacio2);
                            parrafo5H2.add(Frase9H2);
                            parrafo5H2.add(Frase10H2);
                            parrafo5H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo5H2);

                            documento.add(espacio2);
                            parrafo6H2.add(Frase11H2);
                            parrafo6H2.add(Frase12H2);
                            parrafo6H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo6H2);

                            documento.add(espacio2);
                            parrafo7H2.add(Frase13H2);
                            parrafo7H2.add(Frase14H2);
                            parrafo7H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo7H2);

                            documento.add(espacio2);
                            parrafo8H2.add(Frase15H2);
                            parrafo8H2.add(Frase16H2);
                            parrafo8H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo8H2);

                            documento.add(espacio2);
                            parrafo9H2.add(Frase17H2);
                            parrafo9H2.add(Frase18H2);
                            parrafo9H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo9H2);

                            documento.add(espacio2);
                            parrafo10H2.add(Frase19H2);
                            parrafo10H2.add(Frase20H2);
                            parrafo10H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo10H2);

                            documento.add(espacio2);
                            parrafo11H2.add(Frase21H2);
                            parrafo11H2.add(Frase22H2);
                            parrafo11H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo11H2);

                            documento.add(espacio2);
                            parrafo12H2.add(Frase23H2);
                            parrafo12H2.add(Frase24H2);
                            parrafo12H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo12H2);

                            documento.add(espacio2);
                            parrafo13H2.add(Frase25H2);
                            parrafo13H2.add(Frase26H2);
                            parrafo13H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo13H2);

                            documento.add(espacio2);
                            parrafo14H2.add(Frase27H2);
                            parrafo14H2.add(Frase28H2);
                            parrafo14H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo14H2);

                            documento.add(espacio2);
                            parrafo15H2.add(Frase29H2);
                            parrafo15H2.add(Frase30H2);
                            parrafo15H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo15H2);

                            documento.add(espacio2);
                            parrafo16H2.add(Frase31H2);
                            parrafo16H2.add(Frase32H2);
                            parrafo16H2.add(Frase33H2);
                            parrafo16H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo16H2);

                            parrafo18H2.add(Frase34H2);
                            parrafo18H2.add(Frase35H2);
                            parrafo18H2.setAlignment(Element.ALIGN_CENTER);
                            documento.add(parrafo18H2);
                            /*
                            parrafo9H2.add("");
                            parrafo9H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo9H2);
                            
                            parrafo10H2.add("");
                            parrafo10H2.setAlignment(Element.ALIGN_JUSTIFIED);
                            documento.add(parrafo10H2);
                             */
                            documento.close();
                            guardarOrden();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Orden generada con éxito.", "", JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            JOptionPane.showMessageDialog(null, "La imagen no se encontró en el classpath.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (DocumentException | FileNotFoundException e) {
                    }
                    dispositvos ven = new dispositvos();
                    clientes ven1 = new clientes();
                    ven1.insertarTabla2(txtid_cliente.getText());
                    dispositvos.txtidcliente.setText(txtid_cliente.getText());
                    ven.setVisible(true);
                    dispose();
                }
            }

        }
    }//GEN-LAST:event_btnPdfActionPerformed

    private void btnAgregarCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCostoActionPerformed

        if (txtdescripcion.getText().isEmpty() || txtprecio.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            insertarCosto();
            sumarColumna();
            btnterminar.setEnabled(true);
            btneliminar.setEnabled(true);
        }
    }//GEN-LAST:event_btnAgregarCostoActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tabla_precios.getModel();
        int fila = tabla_precios.getSelectedRow();
        if (fila >= 0) {
            modelo.removeRow(fila);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Seleccione una fila.", "", JOptionPane.INFORMATION_MESSAGE);
        }

        if (tabla_precios.getRowCount() == 0) {
            btneliminar.setEnabled(false);
            btnterminar.setEnabled(false);
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "No hay elementos en la tabla.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnterminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnterminarActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tabla_precios.getModel();
        DefaultTableModel modelo2 = (DefaultTableModel) tabla2.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object f[] = new Object[modelo.getColumnCount()];
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                f[j] = modelo.getValueAt(i, j);
            }
            modelo2.addRow(f);
        }

        int fila = modelo.getRowCount();
        for (int i = fila - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
        txttotal.setText(null);
        sumarColumna2();
        btneditar.setEnabled(true);
        btneliminar.setEnabled(false);
        btnterminar.setEnabled(false);
    }//GEN-LAST:event_btnterminarActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tabla_precios.getModel();
        DefaultTableModel modelo2 = (DefaultTableModel) tabla2.getModel();

        for (int i = 0; i < modelo2.getRowCount(); i++) {
            Object f[] = new Object[modelo2.getColumnCount()];
            for (int j = 0; j < modelo2.getColumnCount(); j++) {
                f[j] = modelo2.getValueAt(i, j);
            }
            modelo.addRow(f);
        }

        int fila = modelo2.getRowCount();
        for (int i = fila - 1; i >= 0; i--) {
            modelo2.removeRow(i);
        }

        txtdescuentos.setText("0");
        btneliminar.setEnabled(true);
        btnterminar.setEnabled(true);
        btneditar.setEnabled(false);
    }//GEN-LAST:event_btneditarActionPerformed

    private void txtpartesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpartesKeyTyped
        if (txtpartes.getText().length() >= 300) {
            evt.consume();
        }
    }//GEN-LAST:event_txtpartesKeyTyped

    private void txtdiagnosticoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdiagnosticoKeyTyped
        if (txtdiagnostico.getText().length() >= 500) {
            evt.consume();
        }
    }//GEN-LAST:event_txtdiagnosticoKeyTyped

    private void cb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb2ActionPerformed

    private void cb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(orden1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(orden1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(orden1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(orden1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new orden1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btnAgregarCosto;
    private javax.swing.JButton btnPdf;
    private javax.swing.JButton btneditar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JToggleButton btniva;
    private static javax.swing.JButton btnterminar;
    private static javax.swing.JComboBox<String> cb;
    private static javax.swing.JComboBox<String> cb2;
    private static javax.swing.JComboBox<String> cb3;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel9;
    private static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable2;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JLabel lb9;
    private javax.swing.JLabel lbl10;
    private javax.swing.JLabel lbl11;
    private javax.swing.JLabel lbl9;
    private javax.swing.JTable tabla2;
    private javax.swing.JTable tabla_precios;
    private static javax.swing.JTextArea txtdescripcion;
    private static javax.swing.JTextField txtdescuentos;
    private static javax.swing.JTextArea txtdiagnostico;
    private javax.swing.JTextField txtid20;
    private javax.swing.JTextField txtid21;
    public static javax.swing.JTextField txtid_cliente;
    public static javax.swing.JTextField txtid_dispo;
    public static javax.swing.JTextField txtid_orden;
    private static javax.swing.JTextArea txtpartes;
    private static javax.swing.JTextField txtprecio;
    private static javax.swing.JTextField txttotal;
    private static javax.swing.JTextField txttotalf;
    // End of variables declaration//GEN-END:variables
}
