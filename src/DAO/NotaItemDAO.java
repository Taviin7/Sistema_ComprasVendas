package DAO;

import Connection.Conexao;
import Models.Nota;
import Models.NotaItem;
import Models.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * NotaItemDAO - CRUD Completo
 *
 * @author 2830482411045
 */
public class NotaItemDAO {

    private Conexao conexao;
    private Connection conn;


    public NotaItemDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

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

                System.out.println("✅ Item cadastrado na nota com sucesso!");
            }

        } catch (SQLException ex) {
            String errorMsg = "Erro ao inserir item na Nota:\n" + ex.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg);
            throw ex;
        }
    }

    public NotaItem buscarItemPorId(int id) throws SQLException {
        String query = "SELECT * FROM Notas_Item WHERE nti_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        NotaItem item = null;

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                item = new NotaItem();
                item.setId(rs.getInt("nti_id"));
                item.setQuantidade(rs.getInt("nti_qntd"));
                item.setPreco(rs.getFloat("nti_preco"));

                // Carregar produto
                int proId = rs.getInt("pro_id");
                ProdutoDAO pDAO = new ProdutoDAO();
                Produto produto = pDAO.getproduto(proId);
                item.setProduto(produto);

                // Carregar nota (sem itens para evitar recursão)
                int ntsId = rs.getInt("nts_id");
                NotaDAO nDAO = new NotaDAO();
                Nota nota = nDAO.buscarNotaPorId(ntsId);
                item.setNota(nota);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
        }

        return item;
    }

    public List<NotaItem> listarItensPorNota(int notaId) throws SQLException {
        String query = "SELECT * FROM Notas_Item WHERE nts_id = ? ORDER BY nti_id";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<NotaItem> listaItens = new ArrayList<>();

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, notaId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                NotaItem item = new NotaItem();
                item.setId(rs.getInt("nti_id"));
                item.setQuantidade(rs.getInt("nti_qntd"));
                item.setPreco(rs.getFloat("nti_preco"));

                // Carregar produto
                int proId = rs.getInt("pro_id");
                ProdutoDAO pDAO = new ProdutoDAO();
                Produto produto = pDAO.getproduto(proId);
                item.setProduto(produto);

                listaItens.add(item);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return listaItens;
    }

    public List<NotaItem> listarTodosItens() throws SQLException {
        String query = "SELECT * FROM Notas_Item ORDER BY nts_id, nti_id";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<NotaItem> listaItens = new ArrayList<>();

        try {
            stmt = this.conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                NotaItem item = new NotaItem();
                item.setId(rs.getInt("nti_id"));
                item.setQuantidade(rs.getInt("nti_qntd"));
                item.setPreco(rs.getFloat("nti_preco"));

                int proId = rs.getInt("pro_id");
                ProdutoDAO pDAO = new ProdutoDAO();
                item.setProduto(pDAO.getproduto(proId));

                int ntsId = rs.getInt("nts_id");
                NotaDAO nDAO = new NotaDAO();
                item.setNota(nDAO.buscarNotaPorId(ntsId));

                listaItens.add(item);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return listaItens;
    }

    public void atualizarItem(NotaItem itemNovo, NotaItem itemAntigo) throws SQLException {
        String query = "UPDATE Notas_Item SET pro_id = ?, nti_qntd = ?, nti_preco = ? WHERE nti_id = ?";
        PreparedStatement stmt = null;

        try {
            // 1. Reverter o estoque do item antigo
            reverterEstoqueItem(itemAntigo);

            // 2. Atualizar o item no banco
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, itemNovo.getProduto().getId());
            stmt.setInt(2, itemNovo.getQuantidade());
            stmt.setFloat(3, itemNovo.getPreco());
            stmt.setInt(4, itemNovo.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // 3. Aplicar o novo estoque
                atualizarEstoqueProduto(itemNovo);

                JOptionPane.showMessageDialog(null, "Item atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Item não encontrado!");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar item: " + ex.getMessage());
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }


    public void excluirItensPorNota(int notaId) throws SQLException {
        String query = "DELETE FROM Notas_Item WHERE nts_id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, notaId);
            stmt.executeUpdate();

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void atualizarEstoqueProduto(NotaItem item) throws SQLException {
        ProdutoDAO produtoDAO = new ProdutoDAO();

        String tipoNota = item.getNota().getTipo();
        int produtoId = item.getProduto().getId();
        int quantidade = item.getQuantidade();

        if ("E".equalsIgnoreCase(tipoNota)) {
            // ENTRADA (Compra): AUMENTA o estoque
            produtoDAO.aumentarEstoque(produtoId, quantidade);
            System.out.println("📥 ENTRADA: +" + quantidade + " unidades adicionadas ao estoque");

        } else if ("S".equalsIgnoreCase(tipoNota)) {
            // SAÍDA (Venda): DIMINUI o estoque
            produtoDAO.diminuirEstoque(produtoId, quantidade);
            System.out.println("📤 SAÍDA: -" + quantidade + " unidades removidas do estoque");
        }
    }

    //Reverte o estoque de um item (usado ao excluir ou atualizar)
    private void reverterEstoqueItem(NotaItem item) throws SQLException {
        ProdutoDAO produtoDAO = new ProdutoDAO();

        String tipoNota = item.getNota().getTipo();
        int produtoId = item.getProduto().getId();
        int quantidade = item.getQuantidade();

        if ("E".equalsIgnoreCase(tipoNota)) {
            // Era ENTRADA: DIMINUI o estoque (reverte)
            produtoDAO.diminuirEstoque(produtoId, quantidade);
            System.out.println("🔄 Revertendo ENTRADA: -" + quantidade + " unidades");

        } else if ("S".equalsIgnoreCase(tipoNota)) {
            // Era SAÍDA: AUMENTA o estoque (reverte)
            produtoDAO.aumentarEstoque(produtoId, quantidade);
            System.out.println("🔄 Revertendo SAÍDA: +" + quantidade + " unidades");
        }
    }

    // Reverte o estoque de todos os itens de uma nota (antes de deletar)
    public void reverterEstoqueAntesDeletar(int notaId) throws SQLException {
        // 1. **MUDANÇA CRUCIAL:** Carregar o objeto Nota principal.
        // Usamos o construtor padrão do NotaDAO para fechar a conexão automaticamente.
        NotaDAO notaDAO = new NotaDAO();
        Nota notaPrincipal = notaDAO.buscarNotaPorId(notaId);

        if (notaPrincipal == null) {
            System.out.println("⚠️ ALERTA: Nota não encontrada para o ID " + notaId + ". Reversão de estoque ignorada.");
            return;
        }

        // 2. Carregar os itens (eles virão com item.getNota() = null)
        List<NotaItem> itens = listarItensPorNota(notaId);

        // 3. Iterar e anexar a Nota principal antes de reverter
        for (NotaItem item : itens) {
            // Anexa o objeto Nota, que tem o getTipo() preenchido
            item.setNota(notaPrincipal); // <--- CORREÇÃO AQUI

            reverterEstoqueItem(item);
        }
    }
}
