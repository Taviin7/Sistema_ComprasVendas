package DAO;

import Connection.Conexao;
import Models.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * ProdutoDAO - ATUALIZADO com métodos de gestão de estoque
 *
 * @author 2830482411045
 */
public class ProdutoDAO {

    private Conexao conexao;
    private Connection conn;

    public ProdutoDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirProduto(Produto produto) {
        String query = "INSERT INTO Produto(pro_nome, pro_descricao, pro_precoUnitario, pro_qntdEstoque) "
                + " VALUES(?, ?, ?, ?);";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setFloat(3, produto.getPrecoUnitario());
            stmt.setInt(4, produto.getEstoque());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir Produto: " + ex.getMessage());
        }
    }

    public Produto getproduto(int id) {
        String query = "SELECT * FROM Produto WHERE pro_id = ?;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Produto p = new Produto();
            rs.first();
            p.setId(id);
            p.setNome(rs.getString("pro_nome"));
            p.setDescricao(rs.getString("pro_descricao"));
            p.setPrecoUnitario(rs.getFloat("pro_precoUnitario"));
            p.setEstoque(rs.getInt("pro_qntdEstoque"));

            return p;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Produto: " + ex.getMessage());
            return null;
        }
    }

    public List<Produto> listarProdutos() {
        String query = "SELECT * FROM Produto;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery();
            List<Produto> listaProdutos = new ArrayList();

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("pro_id"));
                p.setNome(rs.getString("pro_nome"));
                p.setDescricao(rs.getString("pro_descricao"));
                p.setPrecoUnitario(rs.getFloat("pro_precoUnitario"));
                p.setEstoque(rs.getInt("pro_qntdEstoque"));
                listaProdutos.add(p);
            }
            return listaProdutos;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar todos os Produto: " + ex.getMessage());
            return null;
        }
    }

    public Produto editarProduto(Produto produto) {
        String query = "UPDATE Produto SET pro_nome = ?, pro_descricao = ?, pro_precoUnitario = ?, pro_qntdEstoque = ?"
                + " WHERE pro_id = ?;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setFloat(3, produto.getPrecoUnitario());
            stmt.setInt(4, produto.getEstoque());
            stmt.setInt(5, produto.getId());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Dados do produto atualizados com sucesso!");
            return produto;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atulizar os dados do Produto: " + ex.getMessage());
            return null;
        }
    }

    public void excluirProduto(int id) {
        try {
            String sql = "DELETE FROM Produto WHERE pro_id = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();

            JOptionPane.showMessageDialog(null, "Produto exlcluido com sucesso!");

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir Produto: " + ex.getMessage());
        }
    }

    // gestão do estoque na notas
    public void aumentarEstoque(int produtoId, int quantidade) throws SQLException {
        String query = "UPDATE Produto SET pro_qntdEstoque = pro_qntdEstoque + ? WHERE pro_id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Estoque aumentado: +" + quantidade + " unidades (Produto ID: " + produtoId + ")");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao aumentar estoque: " + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                // NÃO fechar conn aqui, será fechado depois
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void diminuirEstoque(int produtoId, int quantidade) throws SQLException {
        // Primeiro verificar se há estoque suficiente
        if (!verificarEstoqueDisponivel(produtoId, quantidade)) {
            throw new SQLException("Estoque insuficiente! Não é possível vender " + quantidade + " unidades.");
        }

        String query = "UPDATE Produto SET pro_qntdEstoque = pro_qntdEstoque - ? WHERE pro_id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Estoque diminuído: -" + quantidade + " unidades (Produto ID: " + produtoId + ")");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao diminuir estoque: " + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                // NÃO fechar conn aqui, será fechado depois
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Verifica se há estoque disponível para venda
    public boolean verificarEstoqueDisponivel(int produtoId, int quantidadeDesejada) throws SQLException {
        String query = "SELECT pro_qntdEstoque FROM Produto WHERE pro_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean temEstoque = false;

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, produtoId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int estoqueAtual = rs.getInt("pro_qntdEstoque");
                temEstoque = estoqueAtual >= quantidadeDesejada;

                if (!temEstoque) {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ ESTOQUE INSUFICIENTE!\n\n"
                            + "Estoque atual: " + estoqueAtual + " unidades\n"
                            + "Quantidade solicitada: " + quantidadeDesejada + " unidades\n"
                            + "Faltam: " + (quantidadeDesejada - estoqueAtual) + " unidades");
                }
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                // NÃO fechar conn aqui
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return temEstoque;
    }

    //complemento do mostrarEstoqueProdutoSelecionado() na notas
    public Produto buscarProdutoPorNome(String nome) throws SQLException {
        String query = "SELECT * FROM Produto WHERE pro_nome = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto produto = null;

        try {
            stmt = this.conn.prepareStatement(query);
            stmt.setString(1, nome);
            rs = stmt.executeQuery();

            if (rs.next()) {
                produto = new Produto();
                produto.setId(rs.getInt("pro_id"));
                produto.setNome(rs.getString("pro_nome"));
                produto.setDescricao(rs.getString("pro_descricao"));
                produto.setPrecoUnitario(rs.getFloat("pro_precoUnitario"));
                produto.setEstoque(rs.getInt("pro_qntdEstoque"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar produto: " + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return produto;
    }
}
