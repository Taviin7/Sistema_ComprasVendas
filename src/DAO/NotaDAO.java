package DAO;

import Connection.Conexao;
import Models.Nota;
import Models.NotaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class NotaDAO {

    private Conexao conexao;
    private Connection conn;

    public NotaDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirNota(Nota nota) throws SQLException {
        // Incluir o campo nts_tipo na query
        String query = "INSERT INTO Notas(nts_data, nts_tipo, cli_id, for_id) VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Usar RETURN_GENERATED_KEYS para recuperar o ID gerado
            stmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setDate(1, nota.getData());
            
            // Definir o tipo da nota: E=Entrada (Compra), S=Saída (Venda)
            stmt.setString(2, nota.getTipo());
            
            // Cliente pode ser null em nota de entrada
            if (nota.getCliente() != null) {
                stmt.setInt(3, nota.getCliente().getID());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            // Fornecedor pode ser null em nota de saída
            if (nota.getFornecedor() != null) {
                stmt.setInt(4, nota.getFornecedor().getID());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate();

            // Recuperar o ID gerado
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int notaId = rs.getInt(1);
                nota.setId(notaId);
                
                // Salvar os itens usando o NotaItemDAO
                if (nota.getItens() != null && !nota.getItens().isEmpty()) {
                    NotaItemDAO itemDAO = new NotaItemDAO();
                    for (NotaItem item : nota.getItens()) {
                        item.setNota(nota);
                        itemDAO.salvarNotaItem(item);
                    }
                }
                
                String tipoNota = nota.getTipo().equals("E") ? "ENTRADA (Compra)" : "SAÍDA (Venda)";
                String mensagem = String.format(
                    "Nota cadastrada com sucesso!\n\n" +
                    "Código: %d\n" +
                    "Tipo: %s\n" +
                    "Estoque atualizado automaticamente!", 
                    notaId, tipoNota
                );
                JOptionPane.showMessageDialog(null, mensagem);
            } else {
                throw new SQLException("Falha ao obter o ID da nota inserida.");
            }

        } catch (SQLException ex) {
            String errorMsg = "Erro ao inserir Nota: " + ex.getMessage();
            
            // Mensagens de erro específicas
            if (ex.getMessage().contains("nts_tipo")) {
                errorMsg += "\n\nDICA: Execute o script SQL para adicionar a coluna 'nts_tipo'";
            }
            
            JOptionPane.showMessageDialog(null, errorMsg);
            throw ex;
        } finally {
            // Fechar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Busca uma nota por ID
     */
    public Nota buscarNotaPorId(int id) throws SQLException {
        String query = "SELECT * FROM Notas WHERE nts_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Nota nota = null;
        
        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                nota = new Nota();
                nota.setId(rs.getInt("nts_id"));
                nota.setData(rs.getDate("nts_data"));
                nota.setTipo(rs.getString("nts_tipo"));
                // Carregar cliente e fornecedor se necessário
            }
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        
        return nota;
    }
}