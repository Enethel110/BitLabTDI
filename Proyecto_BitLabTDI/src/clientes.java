
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class clientes extends javax.swing.JFrame {

    String nomabus;

    public clientes() {

        initComponents();

        login btn = new login();
        GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = Gd.getDisplayMode().getWidth();
        int height = Gd.getDisplayMode().getHeight();
        this.setSize(width, height);
        Color azul = new Color(62, 95, 138); // Color azul
        this.getContentPane().setBackground(azul); //Cambiar color de fondo
        setExtendedState(MAXIMIZED_BOTH);

        this.setTitle("C L I E N T E S");
        setIconImage(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.jpeg")).getImage());

        btnnuevo.setIcon(btn.setIcono("/imagenes/editar.png", btnnuevo));
        btnnuevo.setPressedIcon(btn.setIconoPresionado("/imagenes/editar.png", btnnuevo, 10, 10));

        btnguardar.setIcon(btn.setIcono("/imagenes/agregar1.png", btnguardar));
        btnguardar.setPressedIcon(btn.setIconoPresionado("/imagenes/agregar1.png", btnguardar, 10, 10));

        btnBuscar.setIcon(btn.setIcono("/imagenes/buscar.png", btnBuscar));
        btnBuscar.setPressedIcon(btn.setIconoPresionado("/imagenes/buscar.png", btnBuscar, 10, 10));

        btnactualizar.setIcon(btn.setIcono("/imagenes/actualizar.png", btnactualizar));
        btnactualizar.setPressedIcon(btn.setIconoPresionado("/imagenes/actualizar.png", btnactualizar, 10, 10));

        btncancelar.setIcon(btn.setIcono("/imagenes/cancelar.png", btncancelar));
        btncancelar.setPressedIcon(btn.setIconoPresionado("/imagenes/cancelar.png", btncancelar, 10, 10));

        btnAgregarDispo.setIcon(btn.setIcono("/imagenes/device.png", btnAgregarDispo));
        btnAgregarDispo.setPressedIcon(btn.setIconoPresionado("/imagenes/device.png", btnAgregarDispo, 10, 10));

        btnSalir.setIcon(btn.setIcono("/imagenes/cerrarsesion.png", btnSalir));
        btnSalir.setPressedIcon(btn.setIconoPresionado("/imagenes/cerrarsesion.png", btnSalir, 10, 10));

    }

    private void guardarCliente() {
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "insert into cliente (nombre, direccion, colonia, ciudad, cp, correo, telefono, telefono2)"
                    + "values ('" + txtnombre.getText().toUpperCase() + "','" + txtdireccion.getText() + "','" + txtcolonia.getText() + "','" + txtciudad.getText() + "'"
                    + ",'" + txtcp.getText() + "','" + txtcorreo.getText() + "','" + txttelefono.getText() + "','" + txttelefono2.getText() + "')";
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {

        }
    }

    private void getID() {
        String datos[] = new String[1];
        login connect = new login();
        try {
            try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
                String sql = "SELECT id_cliente from cliente WHERE id_cliente = (select max(id_cliente) from cliente)";
                ResultSet res = st.executeQuery(sql);
                while (res.next()) {
                    datos[0] = res.getString("id_cliente");
                }
                if (datos[0] == null) {
                    txtid.setText("1");
                } else {
                    int id = Integer.parseInt(datos[0]);
                    int ultimo = id + 1;
                    String ult = String.valueOf(ultimo);
                    txtid.setText(ult);
                }

                connect.obtenConexion().close();
            }
        } catch (NumberFormatException | SQLException e) {

        }
    }

    public void buscar(String Id) {
        String datos[] = new String[9];
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select id_cliente,nombre,direccion,colonia,ciudad,cp,correo,telefono,telefono2 FROM cliente WHERE id_cliente ='" + Id + "'";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("id_cliente");
                datos[1] = res.getString("nombre");
                datos[2] = res.getString("direccion");
                datos[3] = res.getString("colonia");
                datos[4] = res.getString("ciudad");
                datos[5] = res.getString("cp");
                datos[6] = res.getString("correo");
                datos[7] = res.getString("telefono");
                datos[8] = res.getString("telefono2");
            }
            txtid.setText(datos[0]);
            txtnombre.setText(datos[1]);
            txtdireccion.setText(datos[2]);
            txtcolonia.setText(datos[3]);
            txtciudad.setText(datos[4]);
            txtcp.setText(datos[5]);
            txtcorreo.setText(datos[6]);
            txttelefono.setText(datos[7]);
            txttelefono2.setText(datos[8]);
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    private void actualizar() {
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "update cliente set nombre = '" + txtnombre.getText().toUpperCase() + "',direccion='" + txtdireccion.getText() + "', colonia = '" + txtcolonia.getText() + "'"
                    + ",ciudad = '" + txtciudad.getText() + "',cp = '" + txtcp.getText() + "',correo = '" + txtcorreo.getText() + "',telefono = '" + txttelefono.getText() + "'"
                    + ",telefono2 = '" + txttelefono2.getText() + "' where id_cliente = " + txtid.getText();
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    public void insertarTabla2(String Idc) {
        DefaultTableModel modelo = (DefaultTableModel) dispositvos.tabla.getModel();
        String datos[] = new String[12];
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select * from dispositivo where id_cliente = " + Idc + " order by id_dispo";
            ResultSet res = st.executeQuery(sql);
            int filas = dispositvos.tabla.getRowCount();
            while (res.next()) {
                int i = 1;
                datos[0] = res.getString("id_dispo");
                datos[1] = res.getString("id_cliente");
                datos[2] = res.getString("fecha");
                datos[3] = res.getString("sn");
                datos[4] = res.getString("caso");
                datos[5] = res.getString("tipo_dis");
                datos[6] = res.getString("modelo");
                datos[7] = res.getString("estado_fisi");
                datos[8] = res.getString("marca");
                datos[9] = res.getString("esta_recep");
                datos[10] = res.getString("color");
                datos[11] = res.getString("inventario");
                modelo.addRow(datos);
            }

            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    public void habilitarTF() {
        txtnombre.setEnabled(true);
        txtcolonia.setEnabled(true);
        txtcp.setEnabled(true);
        txtdireccion.setEnabled(true);
        txtciudad.setEnabled(true);
        txtcorreo.setEnabled(true);
        txttelefono.setEnabled(true);
        txttelefono2.setEnabled(true);
    }

    private void deshabilitarTF() {
        txtnombre.setEnabled(false);
        txtcolonia.setEnabled(false);
        txtcp.setEnabled(false);
        txtdireccion.setEnabled(false);
        txtciudad.setEnabled(false);
        txtcorreo.setEnabled(false);
        txttelefono.setEnabled(false);
        txttelefono2.setEnabled(false);
    }

    private void borrar() {
        txtid.setText(null);
        txtnombre.setText(null);
        txtcolonia.setText(null);
        txtcp.setText(null);
        txtdireccion.setText(null);
        txtciudad.setText(null);
        txtcorreo.setText(null);
        txttelefono.setText(null);
        txttelefono2.setText(null);
        txtid.setText(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        txtdireccion = new javax.swing.JTextField();
        txtcolonia = new javax.swing.JTextField();
        txtciudad = new javax.swing.JTextField();
        txtcp = new javax.swing.JTextField();
        txttelefono = new javax.swing.JTextField();
        txttelefono2 = new javax.swing.JTextField();
        txtcorreo = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btnnuevo = new javax.swing.JButton();
        btnAgregarDispo = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(87, 131, 188));
        jPanel1.setPreferredSize(new java.awt.Dimension(962, 640));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CLIENTES");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(30, 30, 114, 32);

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(50, 90, 21, 28);

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(280, 90, 130, 28);

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Dirección");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(50, 210, 130, 28);

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Colonia");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(440, 210, 130, 28);

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Ciudad");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(50, 340, 130, 40);

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("C.P");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(410, 350, 130, 28);

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Teléfono");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(660, 350, 130, 28);

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Segundo Teléfono");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(50, 480, 280, 28);

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Correo");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(420, 490, 280, 28);

        txtid.setBackground(new java.awt.Color(240, 240, 240));
        txtid.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid.setEnabled(false);
        txtid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidActionPerformed(evt);
            }
        });
        jPanel1.add(txtid);
        txtid.setBounds(50, 130, 150, 47);

        txtdireccion.setBackground(new java.awt.Color(240, 240, 240));
        txtdireccion.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtdireccion.setEnabled(false);
        txtdireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdireccionKeyTyped(evt);
            }
        });
        jPanel1.add(txtdireccion);
        txtdireccion.setBounds(50, 250, 310, 47);

        txtcolonia.setBackground(new java.awt.Color(240, 240, 240));
        txtcolonia.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcolonia.setEnabled(false);
        txtcolonia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcoloniaKeyTyped(evt);
            }
        });
        jPanel1.add(txtcolonia);
        txtcolonia.setBounds(430, 250, 340, 47);

        txtciudad.setBackground(new java.awt.Color(240, 240, 240));
        txtciudad.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtciudad.setEnabled(false);
        txtciudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtciudadKeyTyped(evt);
            }
        });
        jPanel1.add(txtciudad);
        txtciudad.setBounds(50, 390, 320, 47);

        txtcp.setBackground(new java.awt.Color(240, 240, 240));
        txtcp.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcp.setEnabled(false);
        txtcp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcpKeyTyped(evt);
            }
        });
        jPanel1.add(txtcp);
        txtcp.setBounds(410, 390, 210, 47);

        txttelefono.setBackground(new java.awt.Color(240, 240, 240));
        txttelefono.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txttelefono.setEnabled(false);
        txttelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txttelefonoKeyTyped(evt);
            }
        });
        jPanel1.add(txttelefono);
        txttelefono.setBounds(650, 390, 250, 47);

        txttelefono2.setBackground(new java.awt.Color(240, 240, 240));
        txttelefono2.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txttelefono2.setEnabled(false);
        txttelefono2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txttelefono2KeyTyped(evt);
            }
        });
        jPanel1.add(txttelefono2);
        txttelefono2.setBounds(50, 530, 320, 47);

        txtcorreo.setBackground(new java.awt.Color(240, 240, 240));
        txtcorreo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcorreo.setEnabled(false);
        txtcorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcorreoKeyTyped(evt);
            }
        });
        jPanel1.add(txtcorreo);
        txtcorreo.setBounds(420, 530, 310, 47);

        txtnombre.setBackground(new java.awt.Color(240, 240, 240));
        txtnombre.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtnombre.setEnabled(false);
        txtnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnombreKeyTyped(evt);
            }
        });
        jPanel1.add(txtnombre);
        txtnombre.setBounds(280, 130, 420, 47);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(43, 42, 962, 640);

        btnSalir.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(25, 55, 87));
        btnSalir.setToolTipText("Cerrar Sesión");
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir);
        btnSalir.setBounds(1270, 610, 50, 50);

        btnactualizar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnactualizar.setForeground(new java.awt.Color(25, 55, 87));
        btnactualizar.setToolTipText("Actualizar");
        btnactualizar.setBorderPainted(false);
        btnactualizar.setContentAreaFilled(false);
        btnactualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnactualizar.setEnabled(false);
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar);
        btnactualizar.setBounds(1220, 250, 100, 70);

        btnnuevo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnnuevo.setForeground(new java.awt.Color(25, 55, 87));
        btnnuevo.setToolTipText("Nuevo");
        btnnuevo.setBorderPainted(false);
        btnnuevo.setContentAreaFilled(false);
        btnnuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnnuevo);
        btnnuevo.setBounds(1080, 110, 90, 70);

        btnAgregarDispo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnAgregarDispo.setForeground(new java.awt.Color(25, 55, 87));
        btnAgregarDispo.setToolTipText("Nuevo Dispositivo");
        btnAgregarDispo.setBorderPainted(false);
        btnAgregarDispo.setContentAreaFilled(false);
        btnAgregarDispo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarDispo.setEnabled(false);
        btnAgregarDispo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarDispoActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregarDispo);
        btnAgregarDispo.setBounds(1210, 380, 110, 80);

        btnBuscar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(25, 55, 87));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscar);
        btnBuscar.setBounds(1070, 240, 80, 80);

        btncancelar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btncancelar.setForeground(new java.awt.Color(25, 55, 87));
        btncancelar.setToolTipText("Cancelar");
        btncancelar.setBorderPainted(false);
        btncancelar.setContentAreaFilled(false);
        btncancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btncancelar.setEnabled(false);
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btncancelar);
        btncancelar.setBounds(1060, 380, 90, 80);

        btnguardar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnguardar.setForeground(new java.awt.Color(25, 55, 87));
        btnguardar.setToolTipText("Guardar");
        btnguardar.setBorderPainted(false);
        btnguardar.setContentAreaFilled(false);
        btnguardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnguardar.setEnabled(false);
        btnguardar.setFocusPainted(false);
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnguardar);
        btnguardar.setBounds(1220, 100, 100, 80);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        login ven = new login();
        ven.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
    private boolean esCorreo(String correo) {
        Pattern patroncito = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher comparar = patroncito.matcher(correo);
        return comparar.find();
    }
    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        int opc;
        if (txtnombre.getText().isEmpty() || txtdireccion.getText().isEmpty() || txtcolonia.getText().isEmpty()
                || txtciudad.getText().isEmpty() || txtcorreo.getText().isEmpty() || txtcp.getText().isEmpty()
                || txttelefono2.getText().isEmpty() || txttelefono.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            if (TelVer()) {
                if (esCorreo(txtcorreo.getText())) {
                    getToolkit().beep();
                    opc = JOptionPane.showConfirmDialog(null, "¿Desea guardar el registro?", "?", JOptionPane.YES_NO_OPTION);
                    if (opc == 0) {
                        guardarCliente();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Guardado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                        deshabilitarTF();
                        borrar();
                        txtid.setText(null);
                        btnnuevo.setEnabled(true);
                        btnBuscar.setEnabled(true);
                        btncancelar.setEnabled(false);
                        btnguardar.setEnabled(false);
                    }
                } else {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Correo no válido.", "Error.", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        int opc;
        if (txtnombre.getText().isEmpty() || txtdireccion.getText().isEmpty() || txtcolonia.getText().isEmpty()
                || txtciudad.getText().isEmpty() || txtcorreo.getText().isEmpty() || txtcp.getText().isEmpty() || txttelefono.getText().isEmpty()
                || txttelefono2.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            if (TelVer()) {
                if (esCorreo(txtcorreo.getText())) {
                    getToolkit().beep();
                    opc = JOptionPane.showConfirmDialog(null, "¿Desea actualizar el registro?", "?", JOptionPane.YES_NO_OPTION);
                    if (opc == 0) {
                        actualizar();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Actualizado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                        borrar();
                        deshabilitarTF();
                        txtid.setText(null);
                        btnactualizar.setEnabled(false);
                        btnnuevo.setEnabled(true);
                        btnguardar.setEnabled(false);
                        btncancelar.setEnabled(false);
                        btnBuscar.setEnabled(true);
                        btnAgregarDispo.setEnabled(false);

                    }
                } else {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Correo no válido.", "Error.", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnactualizarActionPerformed

    private boolean TelVer() {
        boolean estado;
        if (txttelefono.getText().length() < 10 || txttelefono2.getText().length() < 10) {
            estado = false;
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Numero de teléfono no valido.", "Error.", JOptionPane.ERROR_MESSAGE);
        } else {
            estado = true;
        }
        return estado;
    }
    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        habilitarTF();
        borrar();
        getID();
        btnguardar.setEnabled(true);
        btnactualizar.setEnabled(false);
        btncancelar.setEnabled(true);
        btnBuscar.setEnabled(false);
        btnAgregarDispo.setEnabled(false);
    }//GEN-LAST:event_btnnuevoActionPerformed

    private void btnAgregarDispoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarDispoActionPerformed
        if (txtid.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Primero busque un cliente", "", JOptionPane.INFORMATION_MESSAGE);
        } else {
            dispositvos ven = new dispositvos();
            ven.setVisible(true);
            dispose();
            insertarTabla2(txtid.getText());
            dispositvos.txtidcliente.setText(txtid.getText());

            //insertarTabla2();
        }
    }//GEN-LAST:event_btnAgregarDispoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        nomabus = JOptionPane.showInputDialog(null, "¿Nombre del cliente a buscar?", "", JOptionPane.QUESTION_MESSAGE);
        try {
            if (nomabus.isEmpty()) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "No ingresó ningún nombre.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                dispose();
                bClientes clie = new bClientes();
                clie.NombreC(nomabus.toUpperCase());
                if (clie.getContaC() == 0) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "No se encontró ningún cliente.", "", JOptionPane.INFORMATION_MESSAGE);
                    clie.dispose();
                    clientes cliente = new clientes();
                    cliente.setVisible(true);
                }
            }
        } catch (NullPointerException e) {
        }
    }//GEN-LAST:event_btnBuscarActionPerformed
    public void btnActivedSearch() {
        btnactualizar.setEnabled(true);
        btncancelar.setEnabled(true);
        btnAgregarDispo.setEnabled(true);
    }
    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        borrar();
        deshabilitarTF();
        btnactualizar.setEnabled(false);
        btnnuevo.setEnabled(true);
        btnguardar.setEnabled(false);
        btncancelar.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnAgregarDispo.setEnabled(false);
        btncancelar.setEnabled(false);
    }//GEN-LAST:event_btncancelarActionPerformed

    private void txtidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidActionPerformed

    private void txtcpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcpKeyTyped
        if (txtcp.getText().length() >= 25) {
            evt.consume();
        }
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            super.processKeyEvent(evt);
        } else {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ingresa solo números.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_txtcpKeyTyped

    private void txttelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttelefonoKeyTyped

        if (txttelefono.getText().length() >= 10) {
            evt.consume();
        }
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            super.processKeyEvent(evt);
        } else {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ingresa solo números.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_txttelefonoKeyTyped

    private void txttelefono2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttelefono2KeyTyped
        if (txttelefono2.getText().length() >= 10) {
            evt.consume();
        }
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            super.processKeyEvent(evt);
        } else {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ingresa solo números.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_txttelefono2KeyTyped

    private void txtcorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcorreoKeyTyped
        if (txtcorreo.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtcorreoKeyTyped

    private void txtnombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyTyped
        if (txtnombre.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtnombreKeyTyped

    private void txtdireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdireccionKeyTyped
        if (txtdireccion.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtdireccionKeyTyped

    private void txtcoloniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcoloniaKeyTyped
        if (txtcolonia.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtcoloniaKeyTyped

    private void txtciudadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtciudadKeyTyped
        if (txtciudad.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtciudadKeyTyped

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
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new clientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarDispo;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtciudad;
    private javax.swing.JTextField txtcolonia;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtcp;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttelefono;
    private javax.swing.JTextField txttelefono2;
    // End of variables declaration//GEN-END:variables
}
