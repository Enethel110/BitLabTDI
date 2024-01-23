import java.awt.Color;
import java.awt.Font;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class dispositvos extends javax.swing.JFrame {

    DefaultTableModel modelo;

    public dispositvos() {
        initComponents();

        GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = Gd.getDisplayMode().getWidth();
        int height = Gd.getDisplayMode().getHeight();
        this.setSize(width, height);
        Color azul = new Color(62, 95, 138); // Color azul
        this.getContentPane().setBackground(azul); //Cambiar color de fondo
        setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("D I S P O S I T I V O S");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        lbl9.setText(String.valueOf(dtf.format(LocalDateTime.now())));

        JTableHeader th;
        th = tabla.getTableHeader();
        Font fuente = new Font("Comic Sans MS", Font.BOLD, 13);
        th.setFont(fuente);
        tabla.addMouseListener(modificar());

        setIconImage(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.jpeg")).getImage());

        login btn = new login();
        btnagregar.setIcon(btn.setIcono("/imagenes/agregar.png", btnagregar));
        btnagregar.setPressedIcon(btn.setIconoPresionado("/imagenes/agregar.png", btnagregar, 10, 10));

        btncancelar.setIcon(btn.setIcono("/imagenes/cancelar.png", btncancelar));
        btncancelar.setPressedIcon(btn.setIconoPresionado("/imagenes/cancelar.png", btncancelar, 10, 10));

        btneditar.setIcon(btn.setIcono("/imagenes/actualizar.png", btneditar));
        btneditar.setPressedIcon(btn.setIconoPresionado("/imagenes/actualizar.png", btneditar, 10, 10));

        btnGenOrden.setIcon(btn.setIcono("/imagenes/orden.png", btnGenOrden));
        btnGenOrden.setPressedIcon(btn.setIconoPresionado("/imagenes/orden.png", btnGenOrden, 10, 10));

        jBSalir.setIcon(btn.setIcono("/imagenes/cerrarsesion.png", jBSalir));
        jBSalir.setPressedIcon(btn.setIconoPresionado("/imagenes/cerrarsesion.png", jBSalir, 10, 10));

        btneliminar.setIcon(btn.setIcono("/imagenes/eliminar.png", btneliminar));
        btneliminar.setPressedIcon(btn.setIconoPresionado("/imagenes/eliminar.png", btneliminar, 10, 10));
    }

    private void agregar() {
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "insert into dispositivo(sn,caso,tipo_dis,modelo,estado_fisi,marca,esta_recep,color,inventario, id_cliente,fecha)"
                    + "values ('" + txtsn.getText() + "','" + txtcaso.getText() + "','" + txttipo.getText() + "','" + txtmodelo.getText() + "'"
                    + ",'" + txtef.getText() + "','" + txtmarca.getText() + "','" + txtrecibido.getText() + "','" + txtcolor.getText() + "'," + txtinv.getText() + ""
                    + "," + txtidcliente.getText() + ",'" + lbl9.getText() + "')";
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {

        }
    }

    private static void ReiniciarJTable(javax.swing.JTable Tabla) {
        DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }

    private void actualizar() {
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "update dispositivo set sn = '" + txtsn.getText() + "',tipo_dis='" + txttipo.getText() + "', modelo = '" + txtmodelo.getText() + "'"
                    + ",estado_fisi = '" + txtef.getText() + "',esta_recep = '" + txtrecibido.getText() + "',color = '" + txtcolor.getText() + "',marca = '" + txtmarca.getText() + "'"
                    + ",caso = '" + txtcaso.getText() + "',inventario = " + txtinv.getText() + " where id_dispo = " + txtid.getText();
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            String valor = tabla.getValueAt(fila, 0).toString();
            login connect = new login();
            try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
                String sql = "delete from dispositivo where id_dispo = " + Integer.parseInt(valor) + ";";
                int filasAfectadas = st.executeUpdate(sql);
                connect.obtenConexion().close();
                if (filasAfectadas > 0) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Eliminado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el dispositivo para eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "No es posible eliminar dispositivos que tienen asignada una orden de servicio.", "Información", JOptionPane.INFORMATION_MESSAGE);
                //e.printStackTrace(); // Esto imprime detalles del error en la consola para ayudarte a depurar el problema.
            }
        } else {
            // Mostrar un mensaje de error si no se ha seleccionado ninguna fila.
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un dispositivo para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private MouseListener modificar() {
        MouseAdapter clickea = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int fila = tabla.getSelectedRow();
                String id_dispo = tabla.getValueAt(fila, 0).toString();
                String id_cliente = tabla.getValueAt(fila, 1).toString();
                String fecha = tabla.getValueAt(fila, 2).toString();
                String sn = tabla.getValueAt(fila, 3).toString();
                String caso = tabla.getValueAt(fila, 4).toString();
                String tipo = tabla.getValueAt(fila, 5).toString();
                String modelo = tabla.getValueAt(fila, 6).toString();
                String estado_fisico = tabla.getValueAt(fila, 7).toString();
                String marca = tabla.getValueAt(fila, 8).toString();
                String estado_recibido = tabla.getValueAt(fila, 9).toString();
                String color = tabla.getValueAt(fila, 10).toString();
                String inventario = tabla.getValueAt(fila, 11).toString();

                txtid.setText(id_dispo);
                txtidcliente.setText(id_cliente);
                txtsn.setText(sn);
                txtcaso.setText(caso);
                txttipo.setText(tipo);
                txtmodelo.setText(modelo);
                txtef.setText(estado_fisico);
                txtmarca.setText(marca);
                txtrecibido.setText(estado_recibido);
                txtcolor.setText(color);
                txtinv.setText(inventario);

                btnagregar.setEnabled(false);
                btneditar.setEnabled(true);
                btneliminar.setEnabled(true);
                btnGenOrden.setEnabled(false);
                btncancelar.setEnabled(true);
                btneliminar.setEnabled(true);
                btnGenOrden.setEnabled(true);

            }
        };
        return clickea;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtidcliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        txttipo = new javax.swing.JTextField();
        txtmodelo = new javax.swing.JTextField();
        txtcolor = new javax.swing.JTextField();
        txtinv = new javax.swing.JTextField();
        txtsn = new javax.swing.JTextField();
        txtef = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnagregar = new javax.swing.JButton();
        lbl9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtmarca = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtcaso = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtrecibido = new javax.swing.JTextField();
        btncancelar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jBSalir = new javax.swing.JButton();
        btnGenOrden = new javax.swing.JButton();
        btneditar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(87, 131, 188));
        jPanel1.setPreferredSize(new java.awt.Dimension(962, 640));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DISPOSITIVOS");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(30, 30, 200, 32);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID-Cliente");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(470, 460, 280, 28);

        txtidcliente.setBackground(new java.awt.Color(240, 240, 240));
        txtidcliente.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtidcliente.setEnabled(false);
        txtidcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidclienteActionPerformed(evt);
            }
        });
        jPanel1.add(txtidcliente);
        txtidcliente.setBounds(470, 490, 200, 47);

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(40, 90, 21, 28);

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("S/N");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(220, 90, 130, 28);

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tipo");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(40, 210, 130, 28);

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Modelo");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(260, 220, 130, 28);

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Estado Físico");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(40, 330, 130, 40);

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Caso");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(460, 90, 130, 28);

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Inventario");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(280, 460, 130, 28);

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Estado recibido");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(470, 330, 280, 28);

        txtid.setBackground(new java.awt.Color(240, 240, 240));
        txtid.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid.setEnabled(false);
        jPanel1.add(txtid);
        txtid.setBounds(40, 130, 180, 47);

        txttipo.setBackground(new java.awt.Color(240, 240, 240));
        txttipo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txttipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txttipoKeyTyped(evt);
            }
        });
        jPanel1.add(txttipo);
        txttipo.setBounds(40, 250, 180, 47);

        txtmodelo.setBackground(new java.awt.Color(240, 240, 240));
        txtmodelo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtmodelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtmodeloKeyTyped(evt);
            }
        });
        jPanel1.add(txtmodelo);
        txtmodelo.setBounds(260, 250, 170, 47);

        txtcolor.setBackground(new java.awt.Color(240, 240, 240));
        txtcolor.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcolor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcolorKeyTyped(evt);
            }
        });
        jPanel1.add(txtcolor);
        txtcolor.setBounds(40, 490, 180, 47);

        txtinv.setBackground(new java.awt.Color(240, 240, 240));
        txtinv.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtinv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtinvActionPerformed(evt);
            }
        });
        txtinv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtinvKeyTyped(evt);
            }
        });
        jPanel1.add(txtinv);
        txtinv.setBounds(250, 490, 180, 47);

        txtsn.setBackground(new java.awt.Color(240, 240, 240));
        txtsn.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtsn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtsnKeyTyped(evt);
            }
        });
        jPanel1.add(txtsn);
        txtsn.setBounds(260, 130, 180, 47);

        txtef.setBackground(new java.awt.Color(240, 240, 240));
        txtef.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtefKeyTyped(evt);
            }
        });
        jPanel1.add(txtef);
        txtef.setBounds(40, 370, 180, 47);

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Color");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(40, 460, 80, 28);

        btnagregar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnagregar.setForeground(new java.awt.Color(25, 55, 87));
        btnagregar.setToolTipText("Agregar Dispositivo");
        btnagregar.setBorderPainted(false);
        btnagregar.setContentAreaFilled(false);
        btnagregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnagregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnagregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnagregar);
        btnagregar.setBounds(190, 550, 80, 60);

        lbl9.setFont(new java.awt.Font("Microsoft YaHei", 0, 24)); // NOI18N
        lbl9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lbl9);
        lbl9.setBounds(550, 20, 170, 30);

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Marca");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(250, 340, 280, 28);

        txtmarca.setBackground(new java.awt.Color(240, 240, 240));
        txtmarca.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtmarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtmarcaKeyTyped(evt);
            }
        });
        jPanel1.add(txtmarca);
        txtmarca.setBounds(250, 370, 180, 47);

        txtcaso.setColumns(20);
        txtcaso.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcaso.setRows(5);
        txtcaso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcasoKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(txtcaso);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(470, 130, 210, 170);

        txtrecibido.setBackground(new java.awt.Color(240, 240, 240));
        txtrecibido.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtrecibido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtrecibidoKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(txtrecibido);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(470, 370, 200, 50);

        btncancelar.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        btncancelar.setForeground(new java.awt.Color(25, 55, 87));
        btncancelar.setToolTipText("Cancelar");
        btncancelar.setBorderPainted(false);
        btncancelar.setContentAreaFilled(false);
        btncancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btncancelar);
        btncancelar.setBounds(350, 550, 70, 60);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(43, 42, 730, 640);

        btneliminar.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(25, 55, 87));
        btneliminar.setToolTipText("Eliminar Dispositivo");
        btneliminar.setBorderPainted(false);
        btneliminar.setContentAreaFilled(false);
        btneliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btneliminar.setEnabled(false);
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btneliminar);
        btneliminar.setBounds(1070, 500, 90, 70);

        jBSalir.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        jBSalir.setForeground(new java.awt.Color(25, 55, 87));
        jBSalir.setToolTipText("Regresar");
        jBSalir.setBorderPainted(false);
        jBSalir.setContentAreaFilled(false);
        jBSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalirActionPerformed(evt);
            }
        });
        getContentPane().add(jBSalir);
        jBSalir.setBounds(1230, 620, 70, 50);

        btnGenOrden.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        btnGenOrden.setForeground(new java.awt.Color(25, 55, 87));
        btnGenOrden.setToolTipText("Generar orden de servicio");
        btnGenOrden.setBorderPainted(false);
        btnGenOrden.setContentAreaFilled(false);
        btnGenOrden.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenOrden.setEnabled(false);
        btnGenOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenOrdenActionPerformed(evt);
            }
        });
        getContentPane().add(btnGenOrden);
        btnGenOrden.setBounds(1220, 510, 60, 60);

        btneditar.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 18)); // NOI18N
        btneditar.setForeground(new java.awt.Color(25, 55, 87));
        btneditar.setToolTipText("Actualizar");
        btneditar.setBorderPainted(false);
        btneditar.setContentAreaFilled(false);
        btneditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btneditar.setEnabled(false);
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });
        getContentPane().add(btneditar);
        btneditar.setBounds(930, 500, 90, 70);

        tabla.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id_dispositivo", "Id_cliente", "Fecha", "S/N", "Caso", "Tipo", "Modelo", "Estado físico", "Marca", "Estado recibido", "Color", "Inventario"
            }
        ));
        tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tabla);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(840, 50, 500, 290);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void borrar() {
        txtsn.setText(null);
        txtcaso.setText(null);
        txttipo.setText(null);
        txtcolor.setText(null);
        txtinv.setText(null);
        txtmarca.setText(null);
        txtrecibido.setText(null);
        txtef.setText(null);
        txtmodelo.setText(null);
        txtid.setText(null);
    }
    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        int opc;
        clientes tablaN = new clientes();
        getToolkit().beep();
        opc = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar el registro?", "?", JOptionPane.YES_NO_OPTION);
        if (opc == 0) {
            eliminar();
            borrar();
            ReiniciarJTable(tabla);
            tablaN.insertarTabla2(txtidcliente.getText());
            btnagregar.setEnabled(true);
            btneliminar.setEnabled(false);
            btneditar.setEnabled(false);
            btnGenOrden.setEnabled(false);
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void jBSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalirActionPerformed
        clientes ven = new clientes();
        ven.setVisible(true);
        dispose();
    }//GEN-LAST:event_jBSalirActionPerformed

    private void btnGenOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenOrdenActionPerformed
        if (txtid.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Primero busque un cliente", "", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String valor = tabla.getValueAt(fila, 0).toString();
                String valor2 = tabla.getValueAt(fila, 1).toString();
                orden1 ven = new orden1();
                ven.setVisible(true);
                dispose();
                orden1.txtid_cliente.setText(valor2);
                orden1.txtid_dispo.setText(valor);
                orden1.getID();
            } else {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Seleccione una fila", "", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnGenOrdenActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
        int opc;
        clientes tablaN = new clientes();
        if (txtsn.getText().isEmpty() || txtcaso.getText().isEmpty() || txttipo.getText().isEmpty()
                || txtmodelo.getText().isEmpty() || txtef.getText().isEmpty() || txtmarca.getText().isEmpty() || txtrecibido.getText().isEmpty()
                || txtcolor.getText().isEmpty() || txtinv.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        } else {
            getToolkit().beep();
            opc = JOptionPane.showConfirmDialog(null, "¿Deseas Actualizar el registro?", "?", JOptionPane.YES_NO_OPTION);
            if (opc == 0) {
                actualizar();
                borrar();
                ReiniciarJTable(tabla);
                tablaN.insertarTabla2(txtidcliente.getText());
                btneditar.setEnabled(false);
                btnagregar.setEnabled(true);
                btnGenOrden.setEnabled(false);
                btneliminar.setEnabled(false);
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Actualizado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }//GEN-LAST:event_btneditarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        btneditar.setEnabled(false);
        btnagregar.setEnabled(true);
        btneliminar.setEnabled(false);
        btnGenOrden.setEnabled(false);
        borrar();
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btnagregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnagregarActionPerformed
        int opc;
        clientes tablaN = new clientes();
        if (txtsn.getText().isEmpty() || txtcaso.getText().isEmpty() || txttipo.getText().isEmpty()
                || txtmodelo.getText().isEmpty() || txtef.getText().isEmpty() || txtmarca.getText().isEmpty() || txtrecibido.getText().isEmpty()
                || txtcolor.getText().isEmpty() || txtinv.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.INFORMATION_MESSAGE);
        } else {
            getToolkit().beep();
            opc = JOptionPane.showConfirmDialog(null, "¿Desea agregar el registro?", "?", JOptionPane.YES_NO_OPTION);
            if (opc == 0) {
                agregar();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Guardado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                borrar();
                ReiniciarJTable(tabla);
                tablaN.insertarTabla2(txtidcliente.getText());

            }
        }
    }//GEN-LAST:event_btnagregarActionPerformed

    private void txtinvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtinvKeyTyped
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            super.processKeyEvent(evt);
        } else {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ingresa solo números.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_txtinvKeyTyped

    private void txtidclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidclienteActionPerformed

    private void txtinvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtinvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtinvActionPerformed

    private void txtsnKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsnKeyTyped
        if (txtsn.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtsnKeyTyped

    private void txttipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttipoKeyTyped
        if (txttipo.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txttipoKeyTyped

    private void txtmodeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmodeloKeyTyped
        if (txtmodelo.getText().length() >= 40) {
            evt.consume();
        }
    }//GEN-LAST:event_txtmodeloKeyTyped

    private void txtefKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtefKeyTyped
        if (txtef.getText().length() >= 100) {
            evt.consume();
        }
    }//GEN-LAST:event_txtefKeyTyped

    private void txtrecibidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtrecibidoKeyTyped
        if (txtrecibido.getText().length() >= 100) {
            evt.consume();
        }
    }//GEN-LAST:event_txtrecibidoKeyTyped

    private void txtcolorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcolorKeyTyped
        if (txtcolor.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtcolorKeyTyped

    private void txtmarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmarcaKeyTyped
        if (txtmarca.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtmarcaKeyTyped

    private void txtcasoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcasoKeyTyped
        if (txtcaso.getText().length() >= 300) {
            evt.consume();
        }
    }//GEN-LAST:event_txtcasoKeyTyped
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
            java.util.logging.Logger.getLogger(dispositvos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dispositvos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dispositvos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dispositvos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dispositvos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenOrden;
    private javax.swing.JButton btnagregar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btneditar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton jBSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbl9;
    public static javax.swing.JTable tabla;
    private javax.swing.JTextArea txtcaso;
    private javax.swing.JTextField txtcolor;
    private javax.swing.JTextField txtef;
    private static javax.swing.JTextField txtid;
    public static javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtinv;
    private javax.swing.JTextField txtmarca;
    private javax.swing.JTextField txtmodelo;
    private javax.swing.JTextField txtrecibido;
    private javax.swing.JTextField txtsn;
    private javax.swing.JTextField txttipo;
    // End of variables declaration//GEN-END:variables
}
