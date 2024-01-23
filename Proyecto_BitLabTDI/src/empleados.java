
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class empleados extends javax.swing.JFrame {

    String IdEmpleado;
    FileInputStream fis;
    int longitud;
    Image imgImagen;
    ImageIcon icon;

    public empleados() {
        initComponents();
        GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = Gd.getDisplayMode().getWidth();
        int height = Gd.getDisplayMode().getHeight();
        this.setSize(width, height);
        this.setTitle("E M P L E A D O S");
        Color azul = new Color(62, 95, 138); // Color azul
        this.getContentPane().setBackground(azul); //Cambiar color de fondo
        setExtendedState(MAXIMIZED_BOTH);

        login btn = new login();
        setIconImage(new ImageIcon(getClass().getResource("/IMG/BITLABTDI.jpeg")).getImage());

        btneliminar.setIcon(btn.setIcono("/imagenes/eliminar.png", btneliminar));
        btneliminar.setPressedIcon(btn.setIconoPresionado("/imagenes/eliminar.png", btneliminar, 10, 10));

        btnguardar.setIcon(btn.setIcono("/imagenes/agregar1.png", btnguardar));
        btnguardar.setPressedIcon(btn.setIconoPresionado("/imagenes/agregar1.png", btnguardar, 10, 10));

        btnactualizar.setIcon(btn.setIcono("/imagenes/actualizar.png", btnactualizar));
        btnactualizar.setPressedIcon(btn.setIconoPresionado("/imagenes/actualizar.png", btnactualizar, 10, 10));

        btnnuevo.setIcon(btn.setIcono("/imagenes/editar.png", btnnuevo));
        btnnuevo.setPressedIcon(btn.setIconoPresionado("/imagenes/editar.png", btnnuevo, 10, 10));

        btncancelar.setIcon(btn.setIcono("/imagenes/cancelar.png", btncancelar));
        btncancelar.setPressedIcon(btn.setIconoPresionado("/imagenes/cancelar.png", btncancelar, 10, 10));

        btnBuscar.setIcon(btn.setIcono("/imagenes/buscar.png", btnBuscar));
        btnBuscar.setPressedIcon(btn.setIconoPresionado("/imagenes/buscar.png", btnBuscar, 10, 10));

        btnSalir.setIcon(btn.setIcono("/imagenes/cerrarsesion.png", btnSalir));
        btnSalir.setPressedIcon(btn.setIconoPresionado("/imagenes/cerrarsesion.png", btnSalir, 10, 10));

        jbImagen.addActionListener(BuscarImagen());
        jbImagen.setIcon(btn.setIcono("/imagenes/AImage.png", jbImagen));
        jbImagen.setPressedIcon(btn.setIconoPresionado("/imagenes/AImage.png", jbImagen, 10, 10));

        jlImagen.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        btnRol.setIcon(btn.setIcono("/imagenes/password.png", btnRol));
        btnRol.setPressedIcon(btn.setIconoPresionado("/imagenes/password.png", btnRol, 10, 10));
        validarNum(txtsueldo);
    }

    private ActionListener BuscarImagen() {
        ActionListener Buscar = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent ae) {

                JFileChooser laImagen = new JFileChooser();
                laImagen.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filtro = new FileNameExtensionFilter("JPG y JPEG", "jpg", "jpeg");
                laImagen.setFileFilter(filtro);

                int estado = laImagen.showOpenDialog(null);
                if (estado == JFileChooser.APPROVE_OPTION) {

                    try {
                        fis = new FileInputStream(laImagen.getSelectedFile());
                        longitud = (int) laImagen.getSelectedFile().length();
                        try {
                            Image icono = ImageIO.read(laImagen.getSelectedFile()).getScaledInstance(jlImagen.getWidth(), jlImagen.getHeight(), Image.SCALE_DEFAULT);
                            jlImagen.setIcon(new ImageIcon(icono));
                            jlImagen.updateUI();
                            //YnImagen = true;
                        } catch (NullPointerException ioe) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(rootPane, "Imagen: " + "Formato de imagen incorrecto.", "Error.", JOptionPane.ERROR_MESSAGE);
                            //YnImagen = false;
                        } catch (IOException ex) {
                            Logger.getLogger(empleados.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    }
                }
            }
        };
        return Buscar;
    }

    private void guardarEmpleado() {
        login connect = new login();  //Creamos un objeto de la clase
        int item = cb.getSelectedIndex();
        item = item + 1;

        String sql = "insert into empleado (id_departamento, nombre, direccion, colonia, ciudad, cp, rfc, correo, sueldo, nss, imagen)"
                + "values (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement instruccion = connect.obtenConexion().prepareStatement(sql)) {
            instruccion.setInt(1, item);
            instruccion.setString(2, txtnombre.getText().toUpperCase());
            instruccion.setString(3, txtdireccion.getText());
            instruccion.setString(4, txtcolonia.getText());
            instruccion.setString(5, txtciudad.getText());
            instruccion.setString(6, txtcp.getText());
            instruccion.setString(7, txtrfc.getText().toUpperCase());
            instruccion.setString(8, txtcorreo.getText());
            instruccion.setFloat(9, Float.parseFloat(txtsueldo.getText()));
            instruccion.setString(10, txtnss.getText());
            instruccion.setBinaryStream(11, fis, longitud);

            instruccion.executeUpdate();

        } catch (Exception e) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(connect, e);
        }
        try {
            connect.obtenConexion().close();
        } catch (SQLException ex) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(connect, ex);
        }
    }

    private void buscar(String Id) {
        String datos[] = new String[12];
        login connect = new login();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select id_empleado,nombre,direccion,colonia,ciudad,cp,rfc,correo,sueldo,nss,id_departamento, tipo FROM empleado WHERE id_empleado ='" + Id + "'";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("id_empleado");
                datos[1] = res.getString("nombre");
                datos[2] = res.getString("direccion");
                datos[3] = res.getString("colonia");
                datos[4] = res.getString("ciudad");
                datos[5] = res.getString("cp");
                datos[6] = res.getString("rfc");
                datos[7] = res.getString("correo");
                datos[8] = res.getString("sueldo");
                datos[9] = res.getString("nss");
                datos[10] = res.getString("id_departamento");
                datos[11] = res.getString("tipo");
            }
            jlImagen.setEnabled(true);
            txtid.setText(datos[0]);
            txtnombre.setText(datos[1]);
            txtdireccion.setText(datos[2]);
            txtcolonia.setText(datos[3]);
            txtciudad.setText(datos[4]);
            txtcp.setText(datos[5]);
            txtrfc.setText(datos[6]);
            txtcorreo.setText(datos[7]);
            txtsueldo.setText(datos[8]);
            txtnss.setText(datos[9]);
            int dato = Integer.parseInt(datos[10]);
            cb.setSelectedIndex(dato - 1);

            connect.obtenConexion().close();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    private void MaxId() {
        String datos[] = new String[1];
        login connect = new login();

        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "select max(id_empleado) FROM empleado ";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                datos[0] = res.getString("max");
            }
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "El ID del empleado es: " + datos[0], "ID de empleado.", JOptionPane.INFORMATION_MESSAGE);
            st.executeUpdate(sql);
            connect.obtenConexion().close();

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    private byte[] obtenerImagen(int codigo) {

        byte[] longitud = new byte[127];
        login conexion = new login();
        conexion.obtenConexion();
        try {
            if (conexion != null) {
                String query = "select imagen from empleado where id_empleado = ?";
                PreparedStatement instruccion = conexion.obtenConexion().prepareStatement(query);
                instruccion.setInt(1, codigo);
                ResultSet rs = instruccion.executeQuery();
                while (rs.next()) {
                    longitud = rs.getBytes("imagen");
                }
                rs.close();
            }
        } catch (SQLException ex) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Exepcion " + ex);
        } finally {
            try {
                conexion.obtenConexion().close();
            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, ex, "Mensaje", JOptionPane.WARNING_MESSAGE);
            }
        }
        return longitud;
    }

    private void mostrar_Imagen(int codigo) {
        byte[] longitud = obtenerImagen(codigo);

        if (longitud == null) {
            jlImagen.setIcon(null);
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "El empleado no tiene imagen.", "SIN IMAGEN", JOptionPane.INFORMATION_MESSAGE);
            //YnImagen = false;
        } else {

            try {
                //YnImagen = true;
                imgImagen = conv_Imagen(longitud);
                Icon icono = new ImageIcon(imgImagen.getScaledInstance(jlImagen.getWidth(), jlImagen.getHeight(), Image.SCALE_DEFAULT));
                jlImagen.setIcon(icono);

            } catch (IOException ioe) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "Error abriendo la imagen: " + ioe, "", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Image conv_Imagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Iterator lector = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader leerImg = (ImageReader) lector.next();
        Object sourse = bais;
        ImageInputStream iis = ImageIO.createImageInputStream(sourse);
        leerImg.setInput(iis, true);
        ImageReadParam param = leerImg.getDefaultReadParam();
        return leerImg.read(0, param);
    }

    private void actualizar() {
        login connect = new login();
        connect.obtenConexion();

        int item = cb.getSelectedIndex();
        int res = item + 1;
        try {
            if (fis == null && longitud == 0) {
                String query = "UPDATE empleado SET id_departamento = ?, nombre = ?, direccion = ?, colonia = ?, ciudad = ?, cp= ?, rfc = ?, correo = ?, sueldo = ?, nss = ? WHERE id_empleado = " + txtid.getText();

                try (PreparedStatement instruccion = connect.obtenConexion().prepareStatement(query)) {
                    instruccion.setInt(1, res);
                    instruccion.setString(2, txtnombre.getText().toUpperCase());
                    instruccion.setString(3, txtdireccion.getText());
                    instruccion.setString(4, txtcolonia.getText());
                    instruccion.setString(5, txtciudad.getText());
                    instruccion.setString(6, txtcp.getText());
                    instruccion.setString(7, txtrfc.getText().toUpperCase());
                    instruccion.setString(8, txtcorreo.getText());
                    instruccion.setFloat(9, Float.parseFloat(txtsueldo.getText()));
                    instruccion.setString(10, txtnss.getText());

                    instruccion.executeUpdate();
                }
            } else {
                String query = "UPDATE empleado SET id_departamento = ?, nombre = ?, direccion = ?, colonia = ?, ciudad = ?, cp= ?, rfc = ?, correo = ?, sueldo = ?, nss = ?, imagen = ? WHERE id_empleado = " + txtid.getText();

                try (PreparedStatement instruccion = connect.obtenConexion().prepareStatement(query)) {
                    instruccion.setInt(1, item);
                    instruccion.setString(2, txtnombre.getText().toUpperCase());
                    instruccion.setString(3, txtdireccion.getText());
                    instruccion.setString(4, txtcolonia.getText());
                    instruccion.setString(5, txtciudad.getText());
                    instruccion.setString(6, txtcp.getText());
                    instruccion.setString(7, txtrfc.getText());
                    instruccion.setString(8, txtcorreo.getText());
                    instruccion.setInt(9, Integer.parseInt(txtsueldo.getText()));
                    instruccion.setString(10, txtnss.getText());
                    instruccion.setBinaryStream(11, fis, longitud);

                    instruccion.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(connect, ex);
        } finally {
            try {
                connect.obtenConexion().close();
            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(connect, ex);
            }
        }

    }

    private void eliminar() {
        login connect = new login();
        connect.obtenConexion();
        try (java.sql.Statement st = connect.obtenConexion().createStatement()) {
            String sql = "delete from empleado where id_empleado = " + txtid.getText();
            st.executeUpdate(sql);
            connect.obtenConexion().close();
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(connect, e);
        }
    }

    private void habilitarTF() {
        txtnombre.setEnabled(true);
        txtcolonia.setEnabled(true);
        txtcp.setEnabled(true);
        txtdireccion.setEnabled(true);
        txtciudad.setEnabled(true);
        txtrfc.setEnabled(true);
        txtcorreo.setEnabled(true);
        txtnss.setEnabled(true);
        txtsueldo.setEnabled(true);
        cb.setEnabled(true);
    }

    private void deshabilitarTF() {
        txtnombre.setEnabled(false);
        txtcolonia.setEnabled(false);
        txtcp.setEnabled(false);
        txtdireccion.setEnabled(false);
        txtciudad.setEnabled(false);
        txtrfc.setEnabled(false);
        txtcorreo.setEnabled(false);
        txtnss.setEnabled(false);
        txtsueldo.setEnabled(false);
        cb.setEnabled(false);
        txtdepa.setEnabled(false);
    }

    private void borrar() {
        txtnombre.setText(null);
        txtcolonia.setText(null);
        txtcp.setText(null);
        txtdireccion.setText(null);
        txtciudad.setText(null);
        txtrfc.setText(null);
        txtcorreo.setText(null);
        txtnss.setText(null);
        txtsueldo.setText(null);
        txtdepa.setText(null);
        jlImagen.setIcon(null);
        longitud = 0;
        fis = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtid10 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtcolonia = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtnss = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtcorreo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtcp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtciudad = new javax.swing.JTextField();
        txtsueldo = new javax.swing.JTextField();
        txtrfc = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtdepa = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cb = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jlImagen = new javax.swing.JLabel();
        jbImagen = new javax.swing.JButton();
        btnRol = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btnnuevo = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        txtid10.setBackground(new java.awt.Color(240, 240, 240));
        txtid10.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(111, 115, 210));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(87, 131, 188));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("EMPLEADOS");

        txtnombre.setBackground(new java.awt.Color(240, 240, 240));
        txtnombre.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtnombre.setEnabled(false);
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        txtnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnombreKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID");

        txtcolonia.setBackground(new java.awt.Color(240, 240, 240));
        txtcolonia.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcolonia.setEnabled(false);
        txtcolonia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcoloniaKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CP");

        txtid.setBackground(new java.awt.Color(240, 240, 240));
        txtid.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtid.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Ciudad");

        txtdireccion.setBackground(new java.awt.Color(240, 240, 240));
        txtdireccion.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtdireccion.setEnabled(false);
        txtdireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdireccionKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Direccion");

        txtnss.setBackground(new java.awt.Color(240, 240, 240));
        txtnss.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtnss.setEnabled(false);
        txtnss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnssActionPerformed(evt);
            }
        });
        txtnss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnssKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Colonia");

        txtcorreo.setBackground(new java.awt.Color(240, 240, 240));
        txtcorreo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcorreo.setEnabled(false);
        txtcorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcorreoActionPerformed(evt);
            }
        });
        txtcorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcorreoKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("RFC");

        txtcp.setBackground(new java.awt.Color(240, 240, 240));
        txtcp.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtcp.setEnabled(false);
        txtcp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcpKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Sueldo");

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Correo");

        txtciudad.setBackground(new java.awt.Color(240, 240, 240));
        txtciudad.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtciudad.setEnabled(false);
        txtciudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtciudadKeyTyped(evt);
            }
        });

        txtsueldo.setBackground(new java.awt.Color(240, 240, 240));
        txtsueldo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtsueldo.setEnabled(false);
        txtsueldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsueldoActionPerformed(evt);
            }
        });

        txtrfc.setBackground(new java.awt.Color(240, 240, 240));
        txtrfc.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtrfc.setEnabled(false);
        txtrfc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtrfcKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtrfcKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("NSS");

        txtdepa.setBackground(new java.awt.Color(240, 240, 240));
        txtdepa.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        txtdepa.setEnabled(false);
        txtdepa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdepaActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("ID Dpt.");

        cb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        cb.setEnabled(false);
        cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nombre");

        jlImagen.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jlImagen.setEnabled(false);

        jbImagen.setToolTipText("Buscar Imagen");
        jbImagen.setBorderPainted(false);
        jbImagen.setContentAreaFilled(false);
        jbImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbImagen.setEnabled(false);

        btnRol.setToolTipText("Ingresar Credenciales");
        btnRol.setBorderPainted(false);
        btnRol.setContentAreaFilled(false);
        btnRol.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRolActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(jLabel12)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtdepa, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(btnRol, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(13, 13, 13)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtcp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(txtrfc, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtnss, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(txtsueldo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtcolonia, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel1)))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel9)))
                .addGap(185, 193, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRol, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtcolonia, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtcp, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtrfc, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnss, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtsueldo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtdepa, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))))
                .addContainerGap())
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(40, 40, 970, 640);

        btneliminar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(25, 55, 87));
        btneliminar.setToolTipText("Eliminar");
        btneliminar.setBorderPainted(false);
        btneliminar.setContentAreaFilled(false);
        btneliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btneliminar.setDefaultCapable(false);
        btneliminar.setEnabled(false);
        btneliminar.setFocusPainted(false);
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btneliminar);
        btneliminar.setBounds(1240, 340, 80, 80);

        btnBuscar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(25, 55, 87));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setSelected(true);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscar);
        btnBuscar.setBounds(1070, 230, 80, 80);

        btnactualizar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnactualizar.setForeground(new java.awt.Color(25, 55, 87));
        btnactualizar.setToolTipText("Actualizar");
        btnactualizar.setBorderPainted(false);
        btnactualizar.setContentAreaFilled(false);
        btnactualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnactualizar.setDefaultCapable(false);
        btnactualizar.setEnabled(false);
        btnactualizar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnactualizar);
        btnactualizar.setBounds(1240, 230, 100, 70);

        btnnuevo.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnnuevo.setForeground(new java.awt.Color(25, 55, 87));
        btnnuevo.setToolTipText("Nuevo");
        btnnuevo.setBorderPainted(false);
        btnnuevo.setContentAreaFilled(false);
        btnnuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnuevo.setDefaultCapable(false);
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnnuevo);
        btnnuevo.setBounds(1070, 130, 90, 70);

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
        btncancelar.setBounds(1070, 340, 90, 80);

        btnguardar.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnguardar.setForeground(new java.awt.Color(25, 55, 87));
        btnguardar.setToolTipText("Guardar");
        btnguardar.setActionCommand("Guardar");
        btnguardar.setBorderPainted(false);
        btnguardar.setContentAreaFilled(false);
        btnguardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnguardar.setEnabled(false);
        btnguardar.setFocusCycleRoot(true);
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnguardar);
        btnguardar.setBounds(1241, 120, 100, 80);

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
        btnSalir.setBounds(1290, 610, 50, 50);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        int opc;
        if (txtid.getText().isEmpty()) {
            getToolkit().beep();
            JOptionPane.showConfirmDialog(null, "Primero busque un empleado.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            getToolkit().beep();
            opc = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el registro?", "?", JOptionPane.YES_NO_OPTION);
            if (opc == 0) {
                if (Integer.parseInt(txtid.getText()) != 1) {
                    eliminar();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Se ha eliminado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                    txtid.setText(null);
                    deshabilitarTF();
                    borrar();
                    btneliminar.setEnabled(false);
                    btnactualizar.setEnabled(false);
                    btneliminar.setEnabled(false);
                    btnnuevo.setEnabled(true);
                    btnguardar.setEnabled(false);
                    btncancelar.setEnabled(false);
                    jbImagen.setEnabled(false);
                } else {
                    getToolkit().beep();
                    JOptionPane.showConfirmDialog(null, "No puede eliminar al administrador principal.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
                }
            }
    }//GEN-LAST:event_btneliminarActionPerformed
    }
    private void txtcorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcorreoActionPerformed

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        habilitarTF();
        borrar();
        btnRol.setEnabled(false);
        btnguardar.setEnabled(true);
        txtid.setText(null);
        btncancelar.setEnabled(true);
        btnBuscar.setEnabled(false);
        btnnuevo.setEnabled(false);
        txtid.setEnabled(false);
        btnactualizar.setEnabled(false);
        btneliminar.setEnabled(false);
        jbImagen.setEnabled(true);
        jlImagen.setEnabled(true);

    }//GEN-LAST:event_btnnuevoActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        int valor;
        if (txtnombre.getText().isEmpty() || txtdireccion.getText().isEmpty() || txtcolonia.getText().isEmpty()
                || txtciudad.getText().isEmpty() || txtcorreo.getText().isEmpty() || txtcp.getText().isEmpty() || txtrfc.getText().isEmpty()
                || txtnss.getText().isEmpty() || txtsueldo.getText().isEmpty() || txtdepa.getText().isEmpty()
                || longitud == 0 && fis == null || longitud == 1 && fis == null || longitud == 0 && fis != null) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            if (esCorreo(txtcorreo.getText())) {
                getToolkit().beep();
                valor = JOptionPane.showConfirmDialog(null, "¿Desea guardar el registro?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (valor == JOptionPane.YES_OPTION) {
                    guardarEmpleado();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Guardado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                    borrar();
                    MaxId();
                    txtid.setText(null);
                    btnnuevo.setEnabled(false);
                    btnnuevo.setEnabled(true);
                    btnBuscar.setEnabled(true);
                    btncancelar.setEnabled(false);
                    btnguardar.setEnabled(false);
                    jbImagen.setEnabled(false);
                    btnnuevo.setEnabled(true);
                    btnRol.setEnabled(true);
                    jlImagen.setEnabled(false);
                    deshabilitarTF();
                }
            } else {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Correo no válido.", "Error.", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnguardarActionPerformed

    private void validarNum(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char tecla;
                tecla = evt.getKeyChar();
                String sueldo = txtsueldo.getText();
                int punto = sueldo.indexOf("") + 1;
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
            }
        });
    }

    private boolean esCorreo(String correo) {
        Pattern patroncito = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher comparar = patroncito.matcher(correo);
        return comparar.find();
    }
    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        getToolkit().beep();
        IdEmpleado = JOptionPane.showInputDialog(null, "¿ID del empleado a buscar?", "", JOptionPane.QUESTION_MESSAGE);
        if (IdEmpleado != null && !IdEmpleado.isEmpty()) {
            try {
                int numero = Integer.parseInt(IdEmpleado);
                buscar(IdEmpleado);
                if ("".equals(txtid.getText())) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "No se encontró el ID del empleado.", "", JOptionPane.INFORMATION_MESSAGE);
                    btnRol.setEnabled(true);
                    btncancelar.setEnabled(false);
                    deshabilitarTF();
                    btneliminar.setEnabled(false);
                    btnactualizar.setEnabled(false);
                    jbImagen.setEnabled(false);
                    borrar();
                } else {
                    btnRol.setEnabled(false);
                    btncancelar.setEnabled(true);
                    habilitarTF();
                    btneliminar.setEnabled(true);
                    btnactualizar.setEnabled(true);
                    jbImagen.setEnabled(true);
                    obtenerImagen(Integer.parseInt(txtid.getText()));
                    mostrar_Imagen(Integer.parseInt(txtid.getText()));
                }
            } catch (NumberFormatException e) {
                getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Ingrese un numero entero.", "Advertencia.", JOptionPane.WARNING_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        int opc;
        if (txtnombre.getText().isEmpty() || txtdireccion.getText().isEmpty() || txtcolonia.getText().isEmpty()
                || txtciudad.getText().isEmpty() || txtcorreo.getText().isEmpty() || txtcp.getText().isEmpty() || txtrfc.getText().isEmpty()
                || txtnss.getText().isEmpty() || txtsueldo.getText().isEmpty() || txtdepa.getText().isEmpty() //YnImagen == false
                ) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ningún campo puede quedar vacío.", "Advertencia.", JOptionPane.WARNING_MESSAGE);
        } else {
            if (esCorreo(txtcorreo.getText())) {
                getToolkit().beep();
                opc = JOptionPane.showConfirmDialog(null, "¿Desea actualizar el registro?", "?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opc == 0) {
                    actualizar();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Actualizado con éxito.", "", JOptionPane.INFORMATION_MESSAGE);
                    borrar();
                    deshabilitarTF();
                    btneliminar.setEnabled(false);
                    btnactualizar.setEnabled(false);
                    btnnuevo.setEnabled(true);
                    btnguardar.setEnabled(false);
                    btncancelar.setEnabled(false);
                    btnBuscar.setEnabled(true);
                    txtid.setText(null);
                    jbImagen.setEnabled(false);
                }
            } else {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Correo no válido.", "Error.", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        borrar();
        deshabilitarTF();
        btnRol.setEnabled(true);
        txtid.setText(null);
        btneliminar.setEnabled(true);
        btnactualizar.setEnabled(false);
        btneliminar.setEnabled(false);
        btnguardar.setEnabled(false);
        btncancelar.setEnabled(true);
        btnBuscar.setEnabled(true);
        btncancelar.setEnabled(false);
        jbImagen.setEnabled(false);
        jlImagen.setEnabled(false);
        btnnuevo.setEnabled(true);
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        login ven = new login();
        ven.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActionPerformed
        if (cb.getSelectedIndex() == 0) {
            txtdepa.setText("iLabTDI");
        } else if (cb.getSelectedIndex() == 1) {
            txtdepa.setText("Reparaciones");
        } else if (cb.getSelectedIndex() == 2) {
            txtdepa.setText("Análisis");
        } else if (cb.getSelectedIndex() == 3) {
            txtdepa.setText("Recepción");
        } else if (cb.getSelectedIndex() == 4) {
            txtdepa.setText("Otros");
        }
    }//GEN-LAST:event_cbActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreActionPerformed

    private void txtsueldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsueldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsueldoActionPerformed

    private void btnRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRolActionPerformed
        Credenciales pass = new Credenciales();
        pass.setVisible(true);
        dispose();
// TODO add your handling code here:
    }//GEN-LAST:event_btnRolActionPerformed

    private void txtrfcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtrfcKeyTyped
        char car = evt.getKeyChar();
        if (!(Character.isLetterOrDigit(car) || car == KeyEvent.VK_BACK_SPACE || car == KeyEvent.VK_DELETE)) {
            evt.consume();
            getToolkit().beep();
            if ((int) car < 32) {
                // No mostrar el JOptionPane para teclas de control (ASCII < 32)
                return;
            }
            JOptionPane.showMessageDialog(null, "Ingresa solo números y letras.", "", JOptionPane.INFORMATION_MESSAGE);
        }

        if (txtrfc.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtrfcKeyTyped

    private void txtcpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcpKeyTyped
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            super.processKeyEvent(evt);
        } else {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ingresa solo números.", "", JOptionPane.INFORMATION_MESSAGE);
        }

        if (txtcp.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtcpKeyTyped

    private void txtnssActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnssActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnssActionPerformed

    private void txtnssKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnssKeyTyped
        char c = evt.getKeyChar();
        if (Character.isDigit(c) || c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            super.processKeyEvent(evt);
        } else {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Ingresa solo números.", "", JOptionPane.INFORMATION_MESSAGE);
        }

        if (txtnss.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtnssKeyTyped

    private void txtcorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcorreoKeyTyped
        if (txtcorreo.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txtcorreoKeyTyped

    private void txtdepaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdepaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdepaActionPerformed

    private void txtnombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyTyped
        if (txtnombre.getText().length() >= 50) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreKeyTyped

    private void txtdireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdireccionKeyTyped
        if (txtdireccion.getText().length() >= 50) {
            evt.consume();
        }
    }//GEN-LAST:event_txtdireccionKeyTyped

    private void txtcoloniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcoloniaKeyTyped
        if (txtcolonia.getText().length() >= 50) {
            evt.consume();
        }    }//GEN-LAST:event_txtcoloniaKeyTyped

    private void txtciudadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtciudadKeyTyped
        if (txtciudad.getText().length() >= 50) {
            evt.consume();
        }    }//GEN-LAST:event_txtciudadKeyTyped

    private void txtrfcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtrfcKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtrfcKeyPressed

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
            java.util.logging.Logger.getLogger(empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new empleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRol;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JComboBox<String> cb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbImagen;
    private javax.swing.JLabel jlImagen;
    private javax.swing.JTextField txtciudad;
    private javax.swing.JTextField txtcolonia;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtcp;
    private javax.swing.JTextField txtdepa;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtid10;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtnss;
    private javax.swing.JTextField txtrfc;
    private javax.swing.JTextField txtsueldo;
    // End of variables declaration//GEN-END:variables
}
