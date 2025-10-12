package DAO;

import Connection.Conexao;
import Models.Cliente;
import Models.Fornecedor;
import Models.Nota;
import Models.NotaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * NotaDAO - CRUD Completo
 *
 * @author 2830482411045
 */
public class NotaDAO {

    private Conexao conexao;
    private Connection conn;

    public NotaDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirNota(Nota nota) throws SQLException {
        String query = "INSERT INTO Notas(nts_data, nts_tipo, cli_id, for_id) VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setDate(1, nota.getData());
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
                        "Nota cadastrada com sucesso!\n\n"
                        + "Código: %d\n"
                        + "Tipo: %s\n"
                        + "Estoque atualizado automaticamente!",
                        notaId, tipoNota
                );
                JOptionPane.showMessageDialog(null, mensagem);
            } else {
                throw new SQLException("Falha ao obter o ID da nota inserida.");
            }

        } catch (SQLException ex) {
            String errorMsg = "Erro ao inserir Nota: " + ex.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg);
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Nota buscarNota(int id) throws SQLException {
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

                // Carregar cliente se existir
                int cliId = rs.getInt("cli_id");
                if (!rs.wasNull()) {
                    ClienteDAO cDAO = new ClienteDAO();
                    Cliente cliente = cDAO.getCliente(cliId);
                    nota.setCliente(cliente);
                }

                // Carregar fornecedor se existir
                int forId = rs.getInt("for_id");
                if (!rs.wasNull()) {
                    FornecedorDAO fDAO = new FornecedorDAO();
                    Fornecedor fornecedor = fDAO.getFornecedor(forId);
                    nota.setFornecedor(fornecedor);
                }
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return nota;
    }

    public List<Nota> listarTodasNotas() throws SQLException {
        String query = "SELECT * FROM Notas ORDER BY nts_data DESC, nts_id DESC";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Nota> listaNotas = new ArrayList<>();

        try {
            stmt = this.conn.prepareStatement(query);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Nota nota = new Nota();
                nota.setId(rs.getInt("nts_id"));
                nota.setData(rs.getDate("nts_data"));
                nota.setTipo(rs.getString("nts_tipo"));

                // Carregar cliente se existir
                int cliId = rs.getInt("cli_id");
                if (!rs.wasNull()) {
                    ClienteDAO cDAO = new ClienteDAO();
                    Cliente cliente = cDAO.getCliente(cliId);
                    nota.setCliente(cliente);
                }

                // Carregar fornecedor se existir
                int forId = rs.getInt("for_id");
                if (!rs.wasNull()) {
                    FornecedorDAO fDAO = new FornecedorDAO();
                    Fornecedor fornecedor = fDAO.getFornecedor(forId);
                    nota.setFornecedor(fornecedor);
                }

                listaNotas.add(nota);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar todos as Notas: " + ex.getMessage());
            return null;
        }

        return listaNotas;
    }

    /**
     * Atualiza uma nota existente (NÃO atualiza os itens)
     */
    public Nota atualizarNota(Nota nota) throws SQLException {

        String query = "UPDATE Notas SET nts_data = ? WHERE nts_id = ?;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setDate(1, nota.getData());

            /*
            if (nota.getCliente() != null) {
                stmt.setInt(2, nota.getCliente().getID());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (nota.getFornecedor() != null) {
                stmt.setInt(3, nota.getFornecedor().getID());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            */
            stmt.setInt(2, nota.getId());

            JOptionPane.showMessageDialog(null, "Dados da Nota atualizados com sucesso!");
            return nota;

        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Exclui uma nota e todos os seus itens IMPORTANTE: Reverte o estoque dos
     * produtos antes de excluir
     */
    public void excluirNota(int notaId) throws SQLException {
        PreparedStatement stmt = null;

        try {
            //reverter o estoque dos itens
            NotaItemDAO itemDAO = new NotaItemDAO();
            itemDAO.reverterEstoqueAntesDeletar(notaId);

            //deletar os itens da nota
            itemDAO.excluirItens(notaId);

            //Deletar a nota
            String query = "DELETE FROM Notas WHERE nts_id = ?";
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, notaId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null,
                        "Nota excluída com sucesso!\n"
                        + "Estoque dos produtos foi revertido.");
            } else {
                JOptionPane.showMessageDialog(null, "Nota não encontrada!");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir nota: " + ex.getMessage());
            throw ex;
        }
    }
}
