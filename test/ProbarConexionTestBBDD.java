import com.carballeira.practica1.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProbarConexionTestBBDD {

    private static final String URL = "jdbc:sqlite:data/bbdd_practica1.db";
    private static final String NOMBRE = "NombreTest";
    private static final String EMAIL = "EmailTest";
    private static final String TELEFONO = "TelefonoTest";
    private static final String CONTRASEÑA = "ContraseñaTest";
    private static final String ADMIN = "AdminTest";
    private Connection conn;

    @BeforeEach
    public void setUp() {
        try {
            conn = DriverManager.getConnection(URL);
            assertNotNull(conn, "La conexión a la base de datos no debería ser nula.");
            System.out.println("Conexión establecida.");
        } catch (SQLException e) {
            fail("Error al establecer la conexión antes de la prueba: " + e.getMessage());
        }
    }

    @Test
    public void testInsertarUsuario() {
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

            // Insertar usuario
            String sql = "INSERT INTO USUARIOS (NOMBRE, EMAIL, TELEFONO, CONTRASEÑA, ADMIN) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, NOMBRE);
                pstmt.setString(2, EMAIL);
                pstmt.setString(3, TELEFONO);
                pstmt.setString(4, CONTRASEÑA);
                pstmt.setString(5, ADMIN);
                int filasAfectadas = pstmt.executeUpdate();

                assertTrue(filasAfectadas > 0, "La inserción del usuario debería haber afectado al menos una fila.");
                System.out.println("Filas insertadas: " + filasAfectadas);
            }
        } catch (SQLException e) {
            fail("Error en la inserción del usuario: " + e.getMessage());
        }
    }

    @Test
    public void testObtenerUsuarios() {
        String sql = "SELECT NOMBRE, EMAIL, TELEFONO, CONTRASEÑA, ADMIN FROM USUARIOS";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                Usuario usuario = new Usuario();
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setTelefono(rs.getString("TELEFONO"));
                usuario.setContraseña(rs.getString("CONTRASEÑA"));
                usuario.setAdmin(rs.getString("ADMIN"));
                System.out.println(usuario);
            }

            assertTrue(hayResultados, "La consulta debería devolver al menos un usuario.");
        } catch (SQLException e) {
            fail("Error al obtener usuarios: " + e.getMessage());
        }
    }
}
