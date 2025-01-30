import com.carballeira.practica1.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProbarConexionTestBBDD {

    private static final String URL = "jdbc:sqlite:data/bbdd_practica1.db";
    private static final String NOMBRE = "NombreTest";
    private static final String EMAIL = "EmailTest";
    private static final String TELEFONO = "TelefonoTest";
    private static final String CONTRASEÑA = "ContraseñaTest";
    private static final String ADMIN = "AdminTest";
    private Connection conn;

    // Método que se ejecuta antes de cada prueba: Establecer la conexión a la base de datos
    @BeforeEach
    public void setUp() {
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Conexión establecida.");
        } catch (Exception e) {
            e.printStackTrace();
            //fail("Error al establecer la conexión antes de la prueba.");
        }
    }
    // Método que se ejecuta después de cada prueba: Comprobar si la fila fue insertada correctamente
    @Test
    public void tearDown() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            // Verificar si el usuario ya existe
            String checkSql = "SELECT COUNT(*) FROM USUARIOS WHERE EMAIL = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, EMAIL);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("El usuario con email " + EMAIL + " ya existe en la base de datos.");
                        return;
                    }
                }
            }

            // Sentencia SQL para insertar un nuevo usuario
            String sql = "INSERT INTO USUARIOS (NOMBRE, EMAIL, TELEFONO, CONTRASEÑA, ADMIN) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, NOMBRE);
                pstmt.setString(2, EMAIL);
                pstmt.setString(3, TELEFONO);
                pstmt.setString(4, CONTRASEÑA);
                pstmt.setString(5, ADMIN);
                int filasAfectadas = pstmt.executeUpdate();
                System.out.println("Filas insertadas: " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tearDown1() {

        // Consulta para obtener todos los usuarios e imprimirlos
        String sql = "SELECT NOMBRE, EMAIL, TELEFONO, CONTRASEÑA, ADMIN FROM USUARIOS";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setTelefono(rs.getString("TELEFONO"));
                usuario.setContraseña(rs.getString("CONTRASEÑA"));
                usuario.setAdmin(rs.getString("ADMIN"));
                System.out.println(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
