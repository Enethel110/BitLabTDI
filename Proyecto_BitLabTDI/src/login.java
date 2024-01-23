
import java.awt.Image;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.apache.commons.codec.binary.Base64;

public class login extends javax.swing.JFrame {

    protected int tipo = 0;
    static Logger logger = Logger.getLogger(login.class.getName());
    protected String LLAVE = "LaMejorAplicacion";
    protected int error = 0; 
    
    public Connection obtenConexion() {
        Connection conn = null;
        error = 0;
        try {
            Class.forName("org.postgresql.Driver");
            String miBaseD = "jdbc:postgresql://148.202.11.221:5432/BitLabTDI";
            //String miBaseD = "jdbc:postgresql://localhost:5432/iFixCom";
            conn = DriverManager.getConnection(miBaseD, "postgres", "##!&$=)/=Admin");
        } catch (ClassNotFoundException cnfe) {
            //logger.log(Level.SEVERE, null, cnfe);
            mostrarMensajeError("Error al cargar el controlador de base de datos: " + cnfe.getMessage());
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, null, ex);
            mostrarMensajeError("Error al establecer la conexión a la base de datos: " + ex.getMessage());
        }
        return conn;
    }

    private void mostrarMensajeError(String mensaje) {
        error = 1;
        getToolkit().beep();
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public login() {
        initComponents();
        this.setSize(600, 700);
        this.setTitle("L O G I N");
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        ((JPanel) getContentPane()).setOpaque(false);
        ImageIcon uno = new ImageIcon(getClass().getResource("/imagenes/Fondo1.jpg"));
        JLabel fondo = new JLabel();
        fondo.setIcon(uno);
        getLayeredPane().add(fondo, JLayeredPane.FRAME_CONTENT_LAYER);
        fondo.setBounds(0, 0, uno.getIconWidth(), uno.getIconHeight());

        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/IMG/BITLABTDI.png"));
        JLabel imginv = new JLabel(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.png")));
        imginv.setBounds(210, 210, 700, 270);
        panel1.setOpaque(false);
        imginv.setIcon(new ImageIcon(imagen1.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
        panel1.add(imginv);

        jButtonLogin.setIcon(setIcono("/imagenes/acceso.png", jButtonLogin));
        jButtonLogin.setPressedIcon(setIconoPresionado("/imagenes/acceso.png", jButtonLogin, 10, 10));

        setIconImage(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.jpeg")).getImage());
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
            JOptionPane.showMessageDialog(null, "Algo salió mal", "", JOptionPane.ERROR_MESSAGE);
        }
        return encriptacion;
    }

    protected void LogIn() {
        String user, pass, user1;
        String secretKey = "Cadena a cifrar de manera correcta";
        char[] arrayC = txtpass.getPassword();
        String pass1 = new String(arrayC);
        pass1 = Encriptar(secretKey, pass1);
        user1 = txtuser.getText();
        //Credenciales cred = new Credenciales();
        Connection conexion = obtenConexion();
        try {
            if (conexion != null) {
                try (java.sql.Statement st = conexion.createStatement()) {
                    String sql = "select * from empleado";
                    ResultSet res = st.executeQuery(sql);

                    while (res.next()) {
                        user = res.getString("username");
                        pass = res.getString("contra");
                        if ((user == null ? user1 == null : user.equals(user1)) && (pass == null ? pass1 == null : pass.equals(pass1))) {
                            tipo = Integer.parseInt(res.getString("tipo"));
                        }
                    }
                    st.executeUpdate(sql);
                    conexion.close();
                }
            }
        } catch (NumberFormatException | SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtuser = new javax.swing.JTextField();
        txtpass = new javax.swing.JPasswordField();
        jButtonLogin = new javax.swing.JButton();
        panel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Username");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 200, 30));

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Password");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 400, 210, 30));

        txtuser.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtuserActionPerformed(evt);
            }
        });
        getContentPane().add(txtuser, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 320, 360, 50));
        getContentPane().add(txtpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 450, 360, 50));

        jButtonLogin.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        jButtonLogin.setToolTipText("Iniciar Sesión");
        jButtonLogin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonLogin.setBorderPainted(false);
        jButtonLogin.setContentAreaFilled(false);
        jButtonLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLogin.setDefaultCapable(false);
        jButtonLogin.setDoubleBuffered(true);
        jButtonLogin.setFocusCycleRoot(true);
        jButtonLogin.setHideActionText(true);
        jButtonLogin.setIconTextGap(6);
        jButtonLogin.setMultiClickThreshhold(1L);
        jButtonLogin.setSelected(true);
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 530, 70, 60));

        panel1.setForeground(new java.awt.Color(240, 240, 240));
        panel1.setToolTipText("");
        getContentPane().add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 360, 220));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtuserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtuserActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        String valorPass = new String(txtpass.getPassword());
        if (txtuser.getText().isEmpty() || valorPass.isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            LogIn();
            switch (tipo) {
                case 0:
                    getToolkit().beep();
                    if(error == 0){
                        JOptionPane.showMessageDialog(null, "Username y/o password incorrecto.", "Error.", JOptionPane.ERROR_MESSAGE);
                    }
                    txtuser.setText(null);
                    txtpass.setText(null);
                    break;
                case 1:
                    empleados ven = new empleados();
                    ven.setVisible(true);
                    dispose();
                    break;
                case 2:
                    clientes cont = new clientes();
                    cont.setVisible(true);
                    dispose();
                    break;
            }
        }
    }//GEN-LAST:event_jButtonLoginActionPerformed

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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    public Icon setIcono(String url, JButton boton) {
        ImageIcon icon = new ImageIcon(getClass().getResource(url));
        int ancho = boton.getWidth();
        int alto = boton.getHeight();
        ImageIcon icono = new ImageIcon(icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT));
        return icono;
    }

    public Icon setIconoPresionado(String url, JButton boton, int ancho, int altura) {
        ImageIcon icon = new ImageIcon(getClass().getResource(url));
        int width = boton.getWidth() - ancho;
        int height = boton.getHeight() - altura;
        ImageIcon icono = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        return icono;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panel1;
    private javax.swing.JPasswordField txtpass;
    private javax.swing.JTextField txtuser;
    // End of variables declaration//GEN-END:variables

}
