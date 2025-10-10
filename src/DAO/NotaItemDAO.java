package DAO;

import Connection.Conexao;
import Models.NotaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class NotaItemDAO {

    private Conexao conexao;
    private Connection conn;

    public NotaItemDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    /**
     * Salva um item da nota E atualiza o estoque automaticamente
     */
    public void salvarNotaItem(NotaItem item) throws SQLException {
        String query = "INSERT INTO Notas_Item (pro_id, nts_id, nti_qntd, nti_preco) VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = null;

        try {
            // 1. Inserir o item na nota
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, item.getProduto().getId());
            stmt.setInt(2, item.getNota().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setFloat(4, item.getPreco());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // 2. Atualizar o estoque do produto
                atualizarEstoqueProduto(item);
                
                System.out.println("Item cadastrado na nota com sucesso!");
            }

        } catch (SQLException ex) {
            String errorMsg = "Erro ao inserir item na Nota:\n" + ex.getMessage();
            
            if (ex.getMessage().contains("nti_preco") || ex.getMessage().contains("Unknown column")) {
                errorMsg += "\n\nDICA: Verifique o nome da coluna no banco de dados.\n" +
                           "Execute: SHOW COLUMNS FROM Notas_Item;";
            }
            
            JOptionPane.showMessageDialog(null, errorMsg);
            throw ex;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Atualiza o estoque do produto baseado no tipo da nota
     */
    private void atualizarEstoqueProduto(NotaItem item) throws SQLException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        
        String tipoNota = item.getNota().getTipo();
        int produtoId = item.getProduto().getId();
        int quantidade = item.getQuantidade();
        
        if ("E".equalsIgnoreCase(tipoNota)) {
            // ENTRADA (Compra): AUMENTA o estoque
            produtoDAO.aumentarEstoque(produtoId, quantidade);
            System.out.println("ENTRADA: +" + quantidade + " unidades adicionadas ao estoque");
            
        } else if ("S".equalsIgnoreCase(tipoNota)) {
            // SAÍDA (Venda): DIMINUI o estoque
            produtoDAO.diminuirEstoque(produtoId, quantidade);
            System.out.println("SAÍDA: -" + quantidade + " unidades removidas do estoque");
        }
    }
}