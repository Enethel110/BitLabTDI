
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

public class Credenciales extends javax.swing.JFrame {

    DefaultTableModel modelo;
    String codigoE;

    protected static final String PASSWORD_REGEX
            = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

    protected static final Pattern PASSWORD_PATTERN
            = Pattern.compile(PASSWORD_REGEX);

    public Credenciales() {
        this.setTitle("C R E D E N C I A L E S.");
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        Color azul = new Color(62, 95, 138); // Color azul
        this.getContentPane().setBackground(azul); //Cambiar color de fondo
        setIconImage(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.jpeg")).getImage());
        modelo = new DefaultTableModel();
        JTableHeader th;
        th = TablaCred.getTableHeader();
        Font fuente = new Font("Comic Sans MS", Font.BOLD, 13);
        th.setFont(fuente);
        modelo.addColumn("ID_Empleado");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo");
        modelo.addColumn("Departamento");
        this.TablaCred.setModel(modelo);
        LlenarTabla();
        TablaCred.addMouseListener(clickear());

        login log = new login();
        btnActualizar.setIcon(log.setIcono("/imagenes/actualizar.png", btnActualizar));
        btnActualizar.setPressedIcon(log.setIconoPresionado("/imagenes/actualizar.png", btnActualizar, 10, 10));

        btnLimpiar.setIcon(log.setIcono("/imagenes/limpiar.png", btnLimpiar));
        btnLimpiar.setPressedIcon(log.setIconoPresionado("/imagenes/limpiar.png", btnLimpiar, 10, 10));
        Cerrar();
    }

    protected void LlenarTabla() {
        try {
            login connect = new login();
            connect.obtenConexion();
            String sql = "select id_empleado, nombre, tipo, id_departamento from empleado order by id_empleado desc";
            Statement sent = connect.obtenConexion().createStatement();
            ResultSet rs = sent.executeQuery(sql);

            String fila[] = new String[4];
            while (rs.next()) {
                fila[0] = rs.getString("id_empleado");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("tipo");
                fila[3] = rs.getString("id_departamento");
                modelo.addRow(fila);
            }
            TablaCred.setModel(modelo);
            rs.close();
            connect.obtenConexion().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MouseListener clickear() {
        MouseAdapter clickea = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int fila = TablaCred.getSelectedRow();
                codigoE = (String) modelo.getValueAt(fila, 0);
                Limpiar();
                labelID.setText(codigoE);
                buscar(codigoE);
            }
        };
        return clickea;
    }

    private void Limpiar() {
        labelID.setText(null);
        ConbOpc.setSelectedIndex(0);
        txtUser.setText(null);
        txtPass.setText(null);
        bClientes.ReiniciarJTable(TablaCred);
        LlenarTabla();
    }

    private void buscar(String Id) {
        String secretKey = "Cadena a cifrar de manera correcta";
        String datos[] = new String[3];
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select username, contra, tipo FROM empleado WHERE id_empleado = '" + Id + "'";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("username");
                datos[1] = res.getString("contra");
                datos[2] = res.getString("tipo");
            }
            if (datos[0] == null || datos[1] == null || datos[2] == null) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Sin creenciales", "", JOptionPane.WARNING_MESSAGE);
            } else {
                labelID.setText(Id);
                txtUser.setText(datos[0]);
                txtPass.setText(Desencriptar(secretKey, datos[1]));
                int dato = Integer.parseInt(datos[2]);
                ConbOpc.setSelectedIndex(dato);
            }

            connect.obtenConexion().close();

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    protected void actualizar() {
        String secretKey = "Cadena a cifrar de manera correcta";
        login connect = new login();
        int item = ConbOpc.getSelectedIndex();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "update empleado set username = '" + txtUser.getText() + "',contra='" + Encriptar(secretKey, txtPass.getText()) + "', tipo = '" + item + "'"
                    + "where id_empleado = " + labelID.getText();
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    protected String Encriptar(String secretKey, String cadena) {
        String encriptacion = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] llavePassword = md5.digest(secretKey.getBytes("utf-8"));
            byte[] BytesKey = Arrays.copyOf(llavePassword, 24);
            SecretKey key = new SecretKeySpec(BytesKey, "DESede");
            Cipher cifrado = Cipher.getInstance("DESede");
            cifrado.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainTextBytes = cadena.getBytes("utf-8");
            byte[] buf = cifrado.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            encriptacion = new String(base64Bytes);
        } catch (Exception ex) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Algo salió mal", "Error.", JOptionPane.ERROR_MESSAGE);
        }
        return encriptacion;
    }

    protected String Desencriptar(String secretKey, String cadenaEncriptada) {
        String desencriptacion = "";
        try {
            byte[] message = Base64.decodeBase64(cadenaEncriptada.getBytes("utf-8"));
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md5.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = decipher.doFinal(message);
            desencriptacion = new String(plainText, "UTF-8");

        } catch (Exception ex) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Algo salió mal");
        }
        return desencriptacion;
    }

    protected int NumUserRep(String NomUser) {
        int contador = 0;
        String datos[] = new String[2];
        login connect = new login();

        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select count(id_empleado) FROM empleado where username = '" + NomUser + "'";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("count");
                contador = Integer.parseInt(datos[0]);
            }
            connect.obtenConexion().close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
        return contador;
    }

    protected int UserRepetido(String NomUser, String Id) {
        int contador = 0;
        String datos[] = new String[2];
        login connect = new login();

        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select id_empleado, username FROM empleado";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("id_empleado");
                datos[1] = res.getString("username");
                if (datos[0].equals(Id) && datos[1] == null && NumUserRep(txtUser.getText()) == 0) {
                    contador = 0;
                }
                if (datos[0].equals(Id) && datos[1].equals(NomUser)) {
                    contador = 0;
                } else if (datos[1] == null ? NomUser == null : datos[1].equals(NomUser)) {
                    contador = 1;
                }
            }
            connect.obtenConexion().close();

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
        return contador;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLFondo = new javax.swing.JLabel();
        Label3 = new javax.swing.JLabel();
        Label1 = new javax.swing.JLabel();
        Label2 = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        ConbOpc = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtPass = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtUser = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        TablaCred = new javax.swing.JTable();
        LID = new javax.swing.JLabel();
        labelID = new javax.swing.JLabel();

        jLFondo.setBackground(new java.awt.Color(0, 0, 204));
        jLFondo.setForeground(new java.awt.Color(255, 102, 255));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 0, 255));

        Label3.setBackground(new java.awt.Color(0, 0, 0));
        Label3.setFont(new java.awt.Font("Sitka Subheading", 1, 12)); // NOI18N
        Label3.setForeground(new java.awt.Color(255, 255, 255));
        Label3.setText("Rol");

        Label1.setBackground(new java.awt.Color(0, 0, 0));
        Label1.setFont(new java.awt.Font("Sitka Subheading", 1, 12)); // NOI18N
        Label1.setForeground(new java.awt.Color(255, 255, 255));
        Label1.setText("Username");

        Label2.setBackground(new java.awt.Color(0, 0, 0));
        Label2.setFont(new java.awt.Font("Sitka Subheading", 1, 12)); // NOI18N
        Label2.setForeground(new java.awt.Color(255, 255, 255));
        Label2.setText("Password");

        btnLimpiar.setToolTipText("Limpiar");
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setContentAreaFilled(false);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnActualizar.setToolTipText("Actualizar");
        btnActualizar.setBorderPainted(false);
        btnActualizar.setContentAreaFilled(false);
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        ConbOpc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "...", "Administrador", "Empleado" }));
        ConbOpc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConbOpcActionPerformed(evt);
            }
        });

        txtPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPassKeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(txtPass);

        txtUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUserKeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(txtUser);

        TablaCred.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(TablaCred);

        LID.setFont(new java.awt.Font("Sitka Subheading", 1, 12)); // NOI18N
        LID.setForeground(new java.awt.Color(255, 255, 255));
        LID.setText("ID");

        labelID.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        labelID.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Label3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(8, 8, 8)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(ConbOpc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Label1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LID, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelID, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LID, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelID, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane5)
                        .addGap(8, 8, 8))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Label1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ConbOpc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        private void Cerrar() {
        try {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    empleados emple = new empleados();
                    emple.setVisible(true);
                    dispose();
                }
            });
            this.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void ConbOpcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConbOpcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConbOpcActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        int confirmar;
        if (txtUser.getText().equals("") || txtPass.getText().equals("") || ConbOpc.getSelectedIndex() == 0 || labelID.getText() == null) {
            getToolkit().beep();
            JOptionPane.showConfirmDialog(null, "Ningún campo debe quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            if (UserRepetido(txtUser.getText(), labelID.getText()) > 0) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "El nombre de usuario: [" + txtUser.getText() + "] ya esta ocupado.");
            } else if (PASSWORD_PATTERN.matcher(txtPass.getText()).matches()) {
                getToolkit().beep();
                confirmar = JOptionPane.showConfirmDialog(null, "¿Deseas actualizar el registro?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirmar == 0) {
                    actualizar();
                    Limpiar();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Actualizado con exito", "", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "La contraseña: ( " + txtPass.getText() + " ) no cumple los requerimientos ", "Advertencia.", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserKeyTyped
        if (txtUser.getText().length() >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtUserKeyTyped

    private void txtPassKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassKeyTyped
        if (txtPass.getText().length() >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPassKeyTyped

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
            java.util.logging.Logger.getLogger(Credenciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Credenciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Credenciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Credenciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Credenciales().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ConbOpc;
    private javax.swing.JLabel LID;
    private javax.swing.JLabel Label1;
    private javax.swing.JLabel Label2;
    private javax.swing.JLabel Label3;
    private javax.swing.JTable TablaCred;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLFondo;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel labelID;
    private javax.swing.JTextPane txtPass;
    private javax.swing.JTextPane txtUser;
    // End of variables declaration//GEN-END:variables
}
